package org.apache.flink.streaming.api.functions.sink.filesystem;


import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.KeytabHdfsFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.serialization.BulkWriter;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.OperatorStateStore;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.api.common.typeutils.base.array.BytePrimitiveArraySerializer;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.CheckpointListener;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeCallback;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeService;
import org.apache.flink.util.Preconditions;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.PrivilegedExceptionAction;

import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.PROD_CONFIG;


/**
 *
 * @param <IN>
 */

@PublicEvolving
public class SojHdfsSinkWithKeytab<IN>
        extends RichSinkFunction<IN>
        implements CheckpointedFunction, CheckpointListener, ProcessingTimeCallback {

    private static final long serialVersionUID = 1L;

    // -------------------------- state descriptors ---------------------------

    private static final ListStateDescriptor<byte[]> BUCKET_STATE_DESC =
            new ListStateDescriptor<>("bucket-states", BytePrimitiveArraySerializer.INSTANCE);

    private static final ListStateDescriptor<Long> MAX_PART_COUNTER_STATE_DESC =
            new ListStateDescriptor<>("max-part-counter", LongSerializer.INSTANCE);

    // ------------------------ configuration fields --------------------------

    private final long bucketCheckInterval;

    private final BucketsBuilder<IN, ?> bucketsBuilder;

    // --------------------------- runtime fields -----------------------------

    private transient Buckets<IN, ?> buckets;

    private transient ProcessingTimeService processingTimeService;

    // --------------------------- State Related Fields -----------------------------

    private transient ListState<byte[]> bucketStates;

    private transient ListState<Long> maxPartCountersState;
    private org.apache.hadoop.conf.Configuration conf;
    private transient UserGroupInformation ugi;
    /**
     * Creates a new {@code StreamingFileSink} that writes files to the given base directory.
     */
    private SojHdfsSinkWithKeytab(
            final BucketsBuilder<IN, ?> bucketsBuilder,
            final long bucketCheckInterval) {

        Preconditions.checkArgument(bucketCheckInterval > 0L);

        this.bucketsBuilder = Preconditions.checkNotNull(bucketsBuilder);
        this.bucketCheckInterval = bucketCheckInterval;
    }

    // ------------------------------------------------------------------------

    // --------------------------- Sink Builders  -----------------------------

    /**
     * Creates the builder for a {@code StreamingFileSink} with row-encoding format.
     * @param basePath the base path where all the buckets are going to be created as sub-directories.
     * @param encoder the {@link Encoder} to be used when writing elements in the buckets.
     * @param <IN> the type of incoming elements
     * @return The builder where the remaining of the configuration parameters for the sink can be configured.
     * In order to instantiate the sink, call {@link RowFormatBuilder#build()} after specifying the desired parameters.
     */
    public static <IN> RowFormatBuilder<IN, String> forRowFormat(
            final Path basePath, final Encoder<IN> encoder) {
        return new RowFormatBuilder<>(basePath, encoder, new DateTimeBucketAssigner<>());
    }

    /**
     * Creates the builder for a {@link StreamingFileSink} with row-encoding format.
     * @param basePath the base path where all the buckets are going to be created as sub-directories.
     * @param writerFactory the {@link BulkWriter.Factory} to be used when writing elements in the buckets.
     * @param <IN> the type of incoming elements
     * @return The builder where the remaining of the configuration parameters for the sink can be configured.
     * In order to instantiate the sink, call {@link RowFormatBuilder#build()} after specifying the desired parameters.
     */
    public static <IN> BulkFormatBuilder<IN, String> forBulkFormat(
            final org.apache.flink.core.fs.Path basePath, final BulkWriter.Factory<IN> writerFactory) {
        return new BulkFormatBuilder<>(basePath, writerFactory, new DateTimeBucketAssigner<>());
    }

    /**
     * The base abstract class for the {@link RowFormatBuilder} and {@link BulkFormatBuilder}.
     */
    private abstract static class BucketsBuilder<IN, BucketID> implements Serializable {

        private static final long serialVersionUID = 1L;

        abstract Buckets<IN, BucketID> createBuckets(final int subtaskIndex) throws IOException;
    }

    /**
     * A builder for configuring the sink for row-wise encoding formats.
     */
    @PublicEvolving
    public static class RowFormatBuilder<IN, BucketID> extends BucketsBuilder<IN, BucketID> {

        private static final long serialVersionUID = 1L;

        private final long bucketCheckInterval;

        private final org.apache.flink.core.fs.Path basePath;

        private final Encoder<IN> encoder;

        private final BucketAssigner<IN, BucketID> bucketAssigner;

        private final RollingPolicy<IN, BucketID> rollingPolicy;

        BucketFactory aa;
        private final BucketFactory<IN, BucketID> bucketFactory;

        RowFormatBuilder( org.apache.flink.core.fs.Path basePath, Encoder<IN> encoder, BucketAssigner<IN, BucketID> bucketAssigner) {
            this(basePath, encoder, bucketAssigner, DefaultRollingPolicy.create().build(), 60L * 1000L, new DefaultBucketFactoryImpl<>());
        }

        private RowFormatBuilder(
                org.apache.flink.core.fs.Path basePath,
                Encoder<IN> encoder,
                BucketAssigner<IN, BucketID> assigner,
                RollingPolicy<IN, BucketID> policy,
                long bucketCheckInterval,
                BucketFactory<IN, BucketID> bucketFactory) {
            this.basePath = Preconditions.checkNotNull(basePath);
            this.encoder = Preconditions.checkNotNull(encoder);
            this.bucketAssigner = Preconditions.checkNotNull(assigner);
            this.rollingPolicy = Preconditions.checkNotNull(policy);
            this.bucketCheckInterval = bucketCheckInterval;
            this.bucketFactory = Preconditions.checkNotNull(bucketFactory);
        }

        public RowFormatBuilder<IN, BucketID> withBucketCheckInterval( final long interval) {
            return new RowFormatBuilder<>(basePath, encoder, bucketAssigner, rollingPolicy, interval, bucketFactory);
        }

        public RowFormatBuilder<IN, BucketID> withBucketAssigner( final BucketAssigner<IN, BucketID> assigner) {
            return new RowFormatBuilder<>(basePath, encoder, Preconditions.checkNotNull(assigner), rollingPolicy, bucketCheckInterval, bucketFactory);
        }

        public RowFormatBuilder<IN, BucketID> withRollingPolicy( final RollingPolicy<IN, BucketID> policy) {
            return new RowFormatBuilder<>(basePath, encoder, bucketAssigner, Preconditions.checkNotNull(policy), bucketCheckInterval, bucketFactory);
        }

        public <ID> RowFormatBuilder<IN, ID> withBucketAssignerAndPolicy( final BucketAssigner<IN, ID> assigner, final RollingPolicy<IN, ID> policy) {
            return new RowFormatBuilder<>(basePath, encoder, Preconditions.checkNotNull(assigner), Preconditions.checkNotNull(policy), bucketCheckInterval, new DefaultBucketFactoryImpl<>());
        }

        /** Creates the actual sink. */
        public SojHdfsSinkWithKeytab<IN> build() {
            return new SojHdfsSinkWithKeytab<>(this, bucketCheckInterval);
        }

        @Override
        Buckets<IN, BucketID> createBuckets(int subtaskIndex) throws IOException {
            return new Buckets<>(
                    basePath,
                    bucketAssigner,
                    bucketFactory,
                    new RowWisePartWriter.Factory<>(encoder),
                    rollingPolicy,
                    subtaskIndex);
        }

        @VisibleForTesting
        RowFormatBuilder<IN, BucketID> withBucketFactory( final BucketFactory<IN, BucketID> factory) {
            return new RowFormatBuilder<>(basePath, encoder, bucketAssigner, rollingPolicy, bucketCheckInterval, Preconditions.checkNotNull(factory));
        }
    }

    /**
     * A builder for configuring the sink for bulk-encoding formats, e.g. Parquet/ORC.
     */
    @PublicEvolving
    public static class BulkFormatBuilder<IN, BucketID> extends BucketsBuilder<IN, BucketID> {

        private static final long serialVersionUID = 1L;

        private final long bucketCheckInterval;

        private final org.apache.flink.core.fs.Path basePath;

        private final BulkWriter.Factory<IN> writerFactory;

        private final BucketAssigner<IN, BucketID> bucketAssigner;

        private final BucketFactory<IN, BucketID> bucketFactory;

        BulkFormatBuilder( org.apache.flink.core.fs.Path basePath, BulkWriter.Factory<IN> writerFactory, BucketAssigner<IN, BucketID> assigner) {
            this(basePath, writerFactory, assigner, 60L * 1000L, new DefaultBucketFactoryImpl<IN, BucketID>());
        }

        private BulkFormatBuilder(
                Path basePath,
                BulkWriter.Factory<IN> writerFactory,
                BucketAssigner<IN, BucketID> assigner,
                long bucketCheckInterval,
                BucketFactory<IN, BucketID> bucketFactory) {
            this.basePath = Preconditions.checkNotNull(basePath);
            this.writerFactory = writerFactory;
            this.bucketAssigner = Preconditions.checkNotNull(assigner);
            this.bucketCheckInterval = bucketCheckInterval;
            this.bucketFactory = Preconditions.checkNotNull(bucketFactory);
        }

        public BulkFormatBuilder<IN, BucketID> withBucketCheckInterval( long interval) {
            return new BulkFormatBuilder<>(basePath, writerFactory, bucketAssigner, interval, bucketFactory);
        }

        public <ID> BulkFormatBuilder<IN, ID> withBucketAssigner( BucketAssigner<IN, ID> assigner) {
            return new BulkFormatBuilder<>(basePath, writerFactory, Preconditions.checkNotNull(assigner), bucketCheckInterval, new DefaultBucketFactoryImpl<>());
        }

        @VisibleForTesting
        BulkFormatBuilder<IN, BucketID> withBucketFactory( final BucketFactory<IN, BucketID> factory) {
            return new BulkFormatBuilder<>(basePath, writerFactory, bucketAssigner, bucketCheckInterval, Preconditions.checkNotNull(factory));
        }

        /** Creates the actual sink. */
        public SojHdfsSinkWithKeytab<IN> build() {
            return new SojHdfsSinkWithKeytab(this, bucketCheckInterval);
        }

        @Override
        Buckets<IN, BucketID> createBuckets(int subtaskIndex) throws IOException {
            return new Buckets<>(
                    basePath,
                    bucketAssigner,
                    bucketFactory,
                    new BulkPartWriter.Factory<>(writerFactory),
                    OnCheckpointRollingPolicy.build(),
                    subtaskIndex);
        }
    }

    // --------------------------- Sink Methods -----------------------------

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        final int subtaskIndex = getRuntimeContext().getIndexOfThisSubtask();
        this.buckets = bucketsBuilder.createBuckets(subtaskIndex);

        final OperatorStateStore stateStore = context.getOperatorStateStore();
        bucketStates = stateStore.getListState(BUCKET_STATE_DESC);
        maxPartCountersState = stateStore.getUnionListState(MAX_PART_COUNTER_STATE_DESC);

        if (context.isRestored()) {
            buckets.initializeState(bucketStates, maxPartCountersState);
        }
    }

    @Override
    public void notifyCheckpointComplete(long checkpointId) throws Exception {
        buckets.commitUpToCheckpoint(checkpointId);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        Preconditions.checkState(bucketStates != null && maxPartCountersState != null, "sink has not been initialized");

        buckets.snapshotState(
                context.getCheckpointId(),
                bucketStates,
                maxPartCountersState);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        this.processingTimeService = ((StreamingRuntimeContext) getRuntimeContext()).getProcessingTimeService();
        long currentProcessingTime = processingTimeService.getCurrentProcessingTime();
        processingTimeService.registerTimer(currentProcessingTime + bucketCheckInterval, this);
        ugi = KeytabHdfsFactory.getUGI(parameterTool);
        boolean isProd = parameterTool.getBoolean(PROD_CONFIG);
        conf = KeytabHdfsFactory.getConf(isProd);
    }

    @Override
    public void onProcessingTime(long timestamp) throws Exception {
        final long currentTime = processingTimeService.getCurrentProcessingTime();
        buckets.onProcessingTime(currentTime);
        processingTimeService.registerTimer(currentTime + bucketCheckInterval, this);
    }

    @Override
    public void invoke(IN value, SinkFunction.Context context) throws Exception {
        ugi.checkTGTAndReloginFromKeytab();
        ugi.doAs((PrivilegedExceptionAction<?>) () -> {
        buckets.onElement(value, context);
        return null;
    });
    }

    @Override
    public void close() throws Exception {
        ugi.checkTGTAndReloginFromKeytab();
        ugi.doAs((PrivilegedExceptionAction<?>) () -> {
            if (buckets != null) {
                buckets.close();
            }
            return null;
        });
    }


    public static void main(String[] args ){
        System.out.println(ReflectData.get().getSchema(AgentAttribute.class));
        System.out.println(SojHdfsSinkWithKeytab.class.getResource("").getPath());
        System.out.println(SojHdfsSinkWithKeytab.class.getClassLoader().getResource("com/ebay/sojourner/ubd/rt/connectors/filesystem/KeytabHdfsFactory.class").getPath());
        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/").getPath());
//        Files.copy()
        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/"));

        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);

    }
}


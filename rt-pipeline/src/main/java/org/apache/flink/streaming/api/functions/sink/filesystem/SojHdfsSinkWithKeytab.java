package org.apache.flink.streaming.api.functions.sink.filesystem;


import com.ebay.sojourner.ubd.rt.connectors.filesystem.KeytabHdfsFactory;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.functions.util.FunctionUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.CheckpointListener;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeCallback;
import org.apache.hadoop.security.UserGroupInformation;

import java.security.PrivilegedExceptionAction;

import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.PROD_CONFIG;


/**
 * @param <IN>
 */

@PublicEvolving
public class SojHdfsSinkWithKeytab<IN>
        extends RichSinkFunction<IN>
        implements CheckpointedFunction, CheckpointListener, ProcessingTimeCallback {

    private static final long serialVersionUID = 1L;
    private org.apache.hadoop.conf.Configuration conf;
    private transient UserGroupInformation ugi;
    private StreamingFileSink streamingFileSink;

    public SojHdfsSinkWithKeytab( StreamingFileSink streamingFileSink ) {
        super();

        this.streamingFileSink = streamingFileSink;
    }

    // --------------------------- Sink Methods -----------------------------

    @Override
    public void initializeState( FunctionInitializationContext context ) throws Exception {
        streamingFileSink.initializeState(context);
    }

    @Override
    public void notifyCheckpointComplete( long checkpointId ) throws Exception {
        streamingFileSink.notifyCheckpointComplete(checkpointId);
    }

    @Override
    public void snapshotState( FunctionSnapshotContext context ) throws Exception {

        streamingFileSink.snapshotState(context);

    }

    @Override
    public void setRuntimeContext( RuntimeContext t ) {
        super.setRuntimeContext(t);
        FunctionUtils.setFunctionRuntimeContext(streamingFileSink, t);
    }

    @Override
    public void open( Configuration parameters ) throws Exception {
        streamingFileSink.open(parameters);
        ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        ugi = KeytabHdfsFactory.getUGI(parameterTool);
        boolean isProd = parameterTool.getBoolean(PROD_CONFIG);
        conf = KeytabHdfsFactory.getConf(isProd);
    }

    @Override
    public void onProcessingTime( long timestamp ) throws Exception {
        streamingFileSink.onProcessingTime(timestamp);
    }

    @Override
    public void invoke( IN value, SinkFunction.Context context ) throws Exception {
        ugi.checkTGTAndReloginFromKeytab();
        ugi.doAs((PrivilegedExceptionAction<?>) () -> {
            streamingFileSink.invoke(value, context);
            return null;
        });
    }

    @Override
    public void close() throws Exception {
        ugi.checkTGTAndReloginFromKeytab();
        ugi.doAs((PrivilegedExceptionAction<?>) () -> {
            streamingFileSink.close();
            return null;
        });
    }


    public static void main( String[] args ) throws IllegalAccessException, NoSuchFieldException {

//        Field modifiersField = TypeExtractor.class.getDeclaredField("registeredTypeInfoFactories");
//        modifiersField.setAccessible(true);
//        Map<Type, Class<? extends TypeInfoFactory>> registeredTypeInfoFactories = (Map<Type, Class<? extends TypeInfoFactory>>) modifiersField.get(null);
//        registeredTypeInfoFactories.put(String.class, SOjStringFactory.class);
//        modifiersField.set(null,registeredTypeInfoFactories);
//
//        for(Map.Entry entry:((Map<Type, Class<? extends TypeInfoFactory>>)modifiersField.get(null)).entrySet())
//        {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
        Class classList = StreamingFileSink.class.getDeclaringClass();
        System.out.println(classList.getSimpleName());
//        for(Class classname:classList)
//        {
//            if(classname.getSimpleName().equals("BulkFormatBuilder")) {
//                System.out.println(classname.getSimpleName());
//                Constructor constructor=classname.getConstructor(Path.class,BulkWriter.Factory.class,BucketAssigner.class);
//                constructor.newInstance();
//            }
//        }

//        Constructor  constructor=StreamingFileSink.class.getDeclaredConstructor(StreamingFileSink.class.getBucketsBuilder.class,);
//        constructor.newInstance();
//        System.out.println(ReflectData.get().getSchema(AgentAttribute.class));
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("").getPath());
//        System.out.println(SojHdfsSinkWithKeytab.class.getClassLoader().getResource("com/ebay/sojourner/ubd/rt/connectors/filesystem/KeytabHdfsFactory.class").getPath());
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/").getPath());
////        Files.copy()
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/"));
//
//        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);

    }
}


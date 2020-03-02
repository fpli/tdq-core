package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.HDFS_DATA_PATH;
import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.HDFS_ROW_COUNT;
import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.HDFS_WRITE_BUFFER;
import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.PROD_CONFIG;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.util.DateUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.CheckpointListener;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeService;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * Created 2019-08-29 09:18
 *
 * @author : Unikal Liu
 * @version : 1.0.0
 */
@Slf4j
public class SojHdfsSink<T> extends RichSinkFunction<T>
    implements CheckpointedFunction, CheckpointListener {

  private static final long serialVersionUID = 8789819075057897888L;
  private static final ThreadLocal<Path> threadLocalActiveFilePath = new ThreadLocal<Path>();
  private org.apache.hadoop.conf.Configuration conf;
  private String targetFolder;
  private int fileRowCount;
  // private long interval;
  private int writeBuffer;
  private DataFileWriter<T> activeStream = null;
  private DataFileWriter<T> dataFileWriter = null;
  private Schema schema = null;
  private AtomicLong count = new AtomicLong();
  private FileSystem fileSystem;
  private AtomicLong fileCount = new AtomicLong(0);
  private AtomicLong chkId = new AtomicLong(0);
  private boolean isRestored = false;
  private Map<Integer, String> taskFileMap = new HashMap<>();
  // private List<Map<Integer, String>> listTaskFileName = new ArrayList<>();

  private transient ProcessingTimeService processingTimeService;
  private transient ListState<Tuple2<Integer, String>> checkPointTaskFileList;
  private transient UserGroupInformation ugi;
  private Class<T> type = null;

  public SojHdfsSink(Type type) {
    type = type;
  }

  public static void main(String[] args) {
    System.out.println(ReflectData.get().getSchema(AgentAttribute.class));
    System.out.println(SojHdfsSink.class.getResource("").getPath());
    System.out.println(
        SojHdfsSink.class
            .getClassLoader()
            .getResource("com/ebay/sojourner/ubd/rt/connectors/filesystem/KeytabHdfsFactory.class")
            .getPath());
    System.out.println(SojHdfsSink.class.getResource("/").getPath());
    //        Files.copy()
    System.out.println(SojHdfsSink.class.getResource("/"));

    ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    ParameterTool parameterTool =
        (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();

    boolean isProd = parameterTool.getBoolean(PROD_CONFIG);

    targetFolder = parameterTool.getRequired(HDFS_DATA_PATH) + "/avro";
    fileRowCount = parameterTool.getInt(HDFS_ROW_COUNT, 500000);
    writeBuffer = parameterTool.getInt(HDFS_WRITE_BUFFER, 4 * 1024 * 1024);
    // interval = parameterTool.getLong(STREAM_CHECKPOINT_INTERVAL, 60L * 1000L);

    ugi = KeytabHdfsFactory.getUGI(parameterTool);
    conf = KeytabHdfsFactory.getConf(isProd);

    schema = ReflectData.get().getSchema(type);
    ReflectDatumWriter<T> rdw = new ReflectDatumWriter<T>(type);
    dataFileWriter = new DataFileWriter<>(rdw);
  }

  @Override
  public void close() throws Exception {
    super.close();

    generateFile();

    if (dataFileWriter != null) {
      dataFileWriter.close();
    }
  }

  @Override
  public void invoke(T t, Context context) throws Exception {
    try {
      if (t instanceof UbiEvent) {
        DataFileWriter<T> invokeWriter = switchWriter(((UbiEvent) t).getEventTimestamp());
        if (invokeWriter != null) invokeWriter.append(t);
      } else {
        DataFileWriter<T> invokeWriter = switchWriter(((UbiSession) t).getEndTimestamp());
        if (invokeWriter != null) invokeWriter.append(t);
      }

    } catch (Exception e) {
      throw new RuntimeException("error to invoke message", e);
    }
  }

  private DataFileWriter<T> switchWriter(long eventTime) throws IOException, InterruptedException {
    ugi.checkTGTAndReloginFromKeytab();
    ugi.doAs(
        (PrivilegedExceptionAction<?>)
            () -> {
              fileSystem = FileSystem.get(conf);

              int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();

              String dateAndHour = DateUtil.stampToDate(eventTime, "'dt='yyyyMMdd'/h='HH");

              boolean isInInterval = true;
              String taskFileName;

              if (taskFileMap.size() > 0) {

                // taskFileMap = listTaskFileName.get(0);
                taskFileName = taskFileMap.get(indexOfThisSubtask);

                // log.info("-------xixliu:switchWriter:" + taskFileMap.toString());
                // log.info("-------xixliu:taskFileName:" + taskFileName);

                String[] splits = taskFileName.split("-");

                String taskFileHH = splits[3].substring(0, 10);
                String eventHH = DateUtil.stampToDate(eventTime, "yyyyMMddHH");
                long taskFileChkId = Long.parseLong(splits[2]);

                if (!taskFileHH.equals(eventHH) || chkId.get() != taskFileChkId) {
                  isInInterval = false;
                }
              }

              String eventTaskFileName =
                  indexOfThisSubtask
                      + "-"
                      + chkId
                      + "-"
                      + DateUtil.stampToDate(eventTime, "yyyyMMddHHmm");

              if (activeStream == null || fileCount.get() % fileRowCount == 0 || !isInInterval) {

                generateFile();

                Path sourcFolder = new Path(targetFolder + "/" + dateAndHour);

                String fileName =
                    String.format(
                        "/.inprogress.part-%s-%s.avro", eventTaskFileName, (UUID.randomUUID()));

                Path activeFile = new Path(sourcFolder.toString() + fileName);

                threadLocalActiveFilePath.set(activeFile);

                if (!fileSystem.exists(sourcFolder)) fileSystem.mkdirs(sourcFolder);

                activeStream =
                    dataFileWriter.create(
                        schema,
                        fileSystem.create(threadLocalActiveFilePath.get(), false, writeBuffer));

                log.info("create new file: " + threadLocalActiveFilePath.get());

                taskFileMap.put(indexOfThisSubtask, dateAndHour + fileName);
                // listTaskFileName.clear();
                // listTaskFileName.add(taskFileMap);

              }

              return null;
            });
    fileCount.set(count.incrementAndGet());
    return activeStream;
  }

  private void generateFile() throws IOException, InterruptedException {

    ugi.checkTGTAndReloginFromKeytab();
    ugi.doAs(
        (PrivilegedExceptionAction<?>)
            () -> {
              if (activeStream != null) {
                activeStream.fSync();
                activeStream.close();
                count.getAndSet(0);
              }
              try {
                fileSystem = FileSystem.get(conf);
                if (threadLocalActiveFilePath.get() != null
                    && fileSystem.exists(threadLocalActiveFilePath.get())) {

                  String s = threadLocalActiveFilePath.get().toString().replace(".inprogress.", "");
                  Path newFilePath =
                      new Path(
                          s.substring(0, s.length() - 42)
                              + "-"
                              + fileCount
                              + "-"
                              + DateUtil.stampToDate(
                                  System.currentTimeMillis(), "yyyyMMddHHmmssSSS")
                              + ".avro");

                  if (fileSystem.exists(newFilePath)) {
                    newFilePath = new Path(s + "-" + fileCount + ".avro");
                  }
                  fileSystem.rename(threadLocalActiveFilePath.get(), newFilePath);
                  log.info(
                      "persist file: " + threadLocalActiveFilePath.get() + " to " + newFilePath);
                }
              } catch (Exception ex) {
                throw new RuntimeException(
                    "error to close previous file handler: " + threadLocalActiveFilePath.get());
              }
              return null;
            });
  }

  @Override
  public void snapshotState(FunctionSnapshotContext context) throws Exception {

    checkPointTaskFileList.clear();

    for (Map.Entry<Integer, String> element : taskFileMap.entrySet()) {
      checkPointTaskFileList.add(Tuple2.of(element.getKey(), element.getValue()));
    }
    chkId.set(context.getCheckpointId());
  }

  @Override
  public void initializeState(FunctionInitializationContext context) throws Exception {

    ListStateDescriptor<Tuple2<Integer, String>> listStateDescriptor =
        new ListStateDescriptor<Tuple2<Integer, String>>(
            "checkPointTaskFileMapList",
            TypeInformation.of(new TypeHint<Tuple2<Integer, String>>() {}));
    checkPointTaskFileList = context.getOperatorStateStore().getUnionListState(listStateDescriptor);

    // checkPointTaskFileUnionList=context.getOperatorStateStore().
    // getUnionListState(listStateDescriptor);

    if (context.isRestored()) {

      //            Iterator<Map<Integer, String>> it = checkPointCountList.get().iterator();
      //            while (it.hasNext()) {
      //                Map<Integer, String> element = it.next();
      //                listTaskFileName.add(element);
      //            }

      List<Long> taskFileTime = new ArrayList<>();
      List<Long> taskFileChkId = new ArrayList<>();

      for (Tuple2<Integer, String> element : checkPointTaskFileList.get()) {
        taskFileMap.put(element.f0, element.f1);

        taskFileChkId.add(Long.parseLong(element.f1.split("-")[2]));
        taskFileTime.add(Long.parseLong(element.f1.split("-")[3]));
      }

      log.info("-------Restored:taskFileMap:" + taskFileMap.toString());

      chkId.set(Collections.min(taskFileChkId));

      log.info("-------Restore:checkpointId:" + chkId.get());

      String failedHHmm = Collections.min(taskFileTime).toString();

      String dateStr =
          DateUtil.stampToDate(
              DateUtil.addHourAndGetTime(DateUtil.dateToStamp(failedHHmm, "yyyyMMddHHmm"), -1),
              "yyyy-MM-dd HH:00:00.000");

      long time = DateUtil.dateToStamp(dateStr, "yyyy-MM-dd HH:mm:ss.SSS");

      String folder2 = DateUtil.stampToDate(time, "'dt='yyyyMMdd'/h='HH");
      String folder1 =
          DateUtil.stampToDate(
              DateUtil.dateToStamp(failedHHmm, "yyyyMMddHHmm"), "'dt='yyyyMMdd'/h='HH");

      throw new RuntimeException(
          String.format(
              "Can't restored ! Failed at %s,suggest to backfill data from %s.\n"
                  + "You can edit the Main arguments to set  --kafka.consumer.from.time %s .\n"
                  + "Before start the job please remove the folders after %s on hadoop cluster.\n"
                  + "eg: hadoop fs -rmr /sys/edw/working/esa/wrk03_marketing/eip/"
                  + "eip_dcp_dtl_w/avro/%s \n"
                  + "hadoop fs -rmr /sys/edw/working/esa/wrk03_marketing/eip/eip_dcp_dtl_w/"
                  + "avro/%s  etc\n",
              failedHHmm, dateStr, time, folder2, folder2, folder1));
    }
  }

  //    @Override
  //    public void onProcessingTime(long timestamp) throws Exception {
  //
  //        final long currentTime = processingTimeService.getCurrentProcessingTime();
  //
  //
  //        processingTimeService.registerTimer(currentTime + interval, this);
  //
  //        log.info("-------xixliu:onProcessingTime" + timestamp);
  //        log.info("-------xixliu:onProcessingTime currentTime" + currentTime);
  //
  //        log.info("-------xixliu:onProcessingTime currentProcessingTime:" +
  // processingTimeService.getCurrentProcessingTime());
  //
  //    }

  @Override
  public void notifyCheckpointComplete(long checkpointId) throws Exception {
    // log.info("-------xixliu:notifyCheckpointComplete:" + checkpointId);
  }
}

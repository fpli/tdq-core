package com.ebay.tdq.sinks;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author juntzhang
 */
@Slf4j
public class LocalFileSink extends RichSinkFunction<String> {
  private static final Object lock = new Object();
  private static File file = null;
  private static PrintWriter out;

  public LocalFileSink(String name, String header) {
    synchronized (lock) {
      if (file == null) {
        createFile(name, header);
      }
    }
  }

  public static void createFile(String name, String header) {
    file = new File("./tdq/target/" + name + "-" +
        getTimeStr(System.currentTimeMillis()) + ".csv");
    if (file.exists()) {
      log.info("delete file[" + file.toPath() + "]:" + file.delete());
    }
    try {
      log.info("create new file[" + file.toPath() + "]: " + file.createNewFile());
      out = new PrintWriter(file);
    } catch (IOException e) {
      log.warn(e.getMessage());
    }
    write(header);
  }

  public static String getTimeStr(Long t) {
    return FastDateFormat.getInstance("yyyy_MM_dd_HH_mm_ss").format(t);
  }

  public static void write(String str) {
    synchronized (lock) {
      out.write(str);
      out.println();
      out.flush();
    }
  }

  @Override
  public void invoke(String str, Context context) {
    write(str);
  }

  public void close() {
    out.close();
  }
}

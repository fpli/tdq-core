package com.ebay.sojourner.ubd.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

@Slf4j
public class HdfsLoader {

  private static volatile FileSystem fileSystem = null;
  private static volatile HdfsLoader hdfsLoader = null;

  private HdfsLoader() {
    initFs();
  }

  public String getLkpFileContent(String parentPath, String filename) {

    initFs();
    String content = null;
    Path filePath = new Path(parentPath + filename);
    try {
      InputStream in = loadInStream(filePath, filename);
      StringBuffer resultBuilder = new StringBuffer();
      byte[] bytes = new byte[4096];
      int readBytes = 0;

      while ((readBytes = in.read(bytes)) != -1) {
        resultBuilder.append(new String(Arrays.copyOfRange(bytes, 0, readBytes),
            StandardCharsets.UTF_8));
        bytes = new byte[4096];
      }
      content = resultBuilder.toString().trim();
    } catch (IOException e) {
      e.printStackTrace();
      log.error("open HDFS file {} issue:{}", filePath.getName(), e.getMessage());
    }
    return content;
  }

  public static HdfsLoader getInstance() {
    if (hdfsLoader == null) {
      synchronized (HdfsLoader.class) {
        if (hdfsLoader == null) {
          hdfsLoader = new HdfsLoader();
        }
      }
    }
    return hdfsLoader;
  }

  private void initFs() {
    if (fileSystem == null) {
      Configuration configuration = new Configuration();
      try {
        fileSystem = FileSystem.newInstance(configuration);
      } catch (IOException e) {
        e.printStackTrace();
        log.error("init Hadoop FileSystem issue:" + e.getMessage());
      }
    }
  }

  @SuppressWarnings("resource")
  public InputStream loadInStream(Path path, String resource)
      throws FileNotFoundException {
    InputStream instream = null;
    initFs();
    try {
      if (fileSystem.exists(path)) {
        //                instream = new FileInputStream(path);
        instream = fileSystem.open(path);
      } else {
        log.info("Load resource directly as provided path is empty, resource: " + resource);
        instream = loadResource(resource);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      log.info(
          "Load file failed from path: " + path + ", and try to load from resource: " + resource);
      instream = loadResource(resource);
    } catch (IOException e) {
      e.printStackTrace();
      instream = loadResource(resource);
    }

    return instream;
  }

  private InputStream loadResource(String resource) throws FileNotFoundException {
    InputStream instream = null;
    if (StringUtils.isNotBlank(resource)) {
      instream = HdfsLoader.class.getResourceAsStream(resource);
      if (instream == null) {
        throw new FileNotFoundException("Can't locate resource based on classPath: " + resource);
      }
    } else {
      throw new RuntimeException("Try to load empty resource: " + resource);
    }

    return instream;
  }

  public boolean isUpdate(String path, String fileName, Map<String, Long> lkpfileDate) {
    initFs();
    Path path1 = new Path(path, fileName);
    try {
      if (fileSystem.exists(path1)) {
        FileStatus[] fileStatus = fileSystem.listStatus(path1, new FileNameFilter(fileName));
        long lastModifiedTime=fileStatus[0].getModificationTime();
        long preLastModifiedTime=lkpfileDate.get(fileName);
        if(lastModifiedTime>preLastModifiedTime){
          lkpfileDate.put(fileName,lastModifiedTime);
        }
        return lastModifiedTime>preLastModifiedTime;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private class FileNameFilter implements PathFilter {

    private String fileName;

    private FileNameFilter(String fileName) {
      this.fileName = fileName;
    }

    @Override
    public boolean accept(Path path) {
      if(fileName.contains(path.getName()))
      {
        return true;
      }
      return false;
    }
  }

}

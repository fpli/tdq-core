package com.ebay.sojourner.business.util;

import java.io.FileReader;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvUtils {

  public static final String[] FILE_HEADER = {"name", "age"};
  private static CSVFormat csvFormat;
  private static Reader reader;
  private static String path;

  public static Iterable<CSVRecord> readCsvFile(String filename, String[] fileheader)
      throws Exception {

    csvFormat = CSVFormat.DEFAULT.withHeader(fileheader).withSkipHeaderRecord();

    path = CsvUtils.class.getClassLoader().getResource(filename).getPath();

    reader = new FileReader(path);

    return csvFormat.parse(reader);
  }
}

package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.Person;
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


  public static void main(String[] args) throws Exception {

    Iterable<CSVRecord> records = readCsvFile("person.csv", FILE_HEADER);

    for (CSVRecord record : records) {
      Person person = new Person();
      person.setAge(Integer.parseInt(record.get("age")));
      person.setName(record.get("name"));
      System.out.println(person.toString());
    }
  }
}

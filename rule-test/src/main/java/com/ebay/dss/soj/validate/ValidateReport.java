package com.ebay.dss.soj.validate;

import com.ebay.dss.soj.jdbc.HiveJDBClient;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ValidateReport {
  private static Map<String, String> summary = new Yaml()
      .loadAs(ValidateReport.class.getResourceAsStream("/ubd-summary.yaml"), HashMap.class);

  public static void main(String[] args) throws SQLException {
    HiveJDBClient client = new HiveJDBClient();
    ResultSet resultSet = client.exeSQL(summary.get("UBI_SESSION_SAMPLE"));
    System.out.println(toJson(resultSet));
  }

  public static String toJson(ResultSet rs) throws SQLException {
    List<Map<String, Object>> rsList = new ArrayList<>();
    ResultSetMetaData rsmd = rs.getMetaData();
    while (rs.next()) {
      Map<String, Object> rsColMap = new HashMap<>();
      int numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        String columnName = rsmd.getColumnLabel(i);
        rsColMap.put(columnName, rs.getObject(columnName));
      }
      rsList.add(rsColMap);
    }
    return new Gson().toJson(rsList);
  }
}

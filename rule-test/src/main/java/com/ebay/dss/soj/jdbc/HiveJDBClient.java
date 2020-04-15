package com.ebay.dss.soj.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJDBClient {

  private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
  private static final String APOLLO_HIVESERVER2_JDBC_URL = "jdbc:hive2://"
      + "apollo-rno-zk-1.vip.hadoop.ebay.com:2181,"
      + "apollo-rno-zk-2.vip.hadoop.ebay.com:2181,"
      + "apollo-rno-zk-3.vip.hadoop.ebay.com:2181,"
      + "apollo-rno-zk-4.vip.hadoop.ebay.com:2181,"
      + "apollo-rno-zk-5.vip.hadoop.ebay.com:2181/;"
      + "serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";

  public HiveJDBClient() {
    if (connection == null) {
      initJDBConnection();
    }
  }

  private Connection connection;

  private Connection initJDBConnection() {
    try {
      Class.forName(DRIVER_NAME);
      connection = DriverManager.getConnection(APOLLO_HIVESERVER2_JDBC_URL);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }

  public ResultSet exeSQL(String sql) {
    ResultSet resultSet = null;
    try {
      Statement statement = connection.createStatement();
      resultSet = statement.executeQuery(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }
}

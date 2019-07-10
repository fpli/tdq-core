package com.ebay.sojourner.ubd.batch.pipeline

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.ebay.sojourner.ubd.common.model.{EventKey, SojEvent, UbiEvent}
import org.apache.spark.{SparkConf, SparkContext}
import com.alibaba.fastjson.JSON
import org.apache.avro.generic.GenericRecord
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.avro.mapred.{AvroInputFormat, AvroWrapper}
import org.apache.hadoop.io.NullWritable

object fSojournerUBDBatchPipeline {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("Sojourner Unified Bot Detection Batch Pipeline").getOrCreate()

    System.out.println(this.getClass.getResource("/").getPath)
    val df = spark.read.format("com.databricks.spark.avro").load(this.getClass.getResource("/").getPath + "event-r-03111.avro")
    System.out.println(df.printSchema())
    System.out.println(df.count())
    df.select("key.guid", "key.sessionSkey", "key.seqNum").createOrReplaceTempView("table1");
    val df2 = spark.read.format("com.databricks.spark.avro").load(this.getClass.getResource("/").getPath + "event-r-03111.avro")
    df2.select("value.guid", "value.sessionSkey", "value.sessionStartDt").createOrReplaceTempView("table2");
    val finalDf = spark.sql("select tb1.guid,tb1.sessionSkey,tb1.seqNum,tb2.sessionStartDt from table1 tb1 join table2 tb2 on tb1.guid=tb2.guid and tb1.sessionSkey=tb2.sessionSkey")
    val targetFilePath = new File(this.getClass.getResource("/").getPath + "test")
    if (targetFilePath.exists()) {
      targetFilePath.delete();
    }
    finalDf.write.parquet("file://" + this.getClass.getResource("/").getPath + "test")
    spark.stop()

  }
}
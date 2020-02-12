package com.ebay.sojourner.ubd.batch.pipeline

import java.io.File

import org.apache.spark.sql.SparkSession

object SojournerUBDBatchPipelineForEvent {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("Sojourner Unified Bot Detection Batch Pipeline").getOrCreate()
    val filePath = this.getClass.getResource("/").getPath
    //    val filePath="/Users/xiaoding/"
    val parquetFileDF = spark.read.parquet(filePath + "part-11-4")
    parquetFileDF.createOrReplaceTempView("parquetFile")
    parquetFileDF.printSchema()
    val namesDF = spark.sql("SELECT guid, clientData.TStamp, FROM parquetFile LIMIT 100")
//    namesDF.map(attributes => "Name: " + attributes(0)).show()

//    df.select("key.guid", "key.sessionSkey", "key.seqNum").createOrReplaceTempView("table1");
//    val df2 = spark.read.format("com.databricks.spark.avro").load(filePath + "event-r-03111.avro")
//    df2.select("value.guid", "value.sessionSkey", "value.sessionStartDt").
//      createOrReplaceTempView("table2");
//    val finalDf = spark.sql("select tb1.guid,tb1.sessionSkey,tb1.seqNum,tb2.sessionStartDt " +
//      "from table1 tb1 join table2 tb2 on tb1.guid=tb2.guid and tb1.sessionSkey=tb2.sessionSkey")
    val targetFilePath = new File(filePath + "test")
    if (targetFilePath.exists()) {
      targetFilePath.delete();
    }
    namesDF.printSchema()
    namesDF.show()
    namesDF.write.parquet("file://" + this.getClass.getResource("/").getPath + "test")
    spark.stop()

  }
}

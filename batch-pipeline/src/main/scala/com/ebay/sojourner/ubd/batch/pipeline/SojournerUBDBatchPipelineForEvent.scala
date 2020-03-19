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


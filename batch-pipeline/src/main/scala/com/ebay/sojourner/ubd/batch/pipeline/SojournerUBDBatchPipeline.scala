package com.ebay.sojourner.ubd.batch.pipeline

import org.apache.spark.sql.SparkSession

object SojournerUBDBatchPipeline {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("Sojourner Unified Bot Detection Batch Pipeline").getOrCreate()
    val file = "README.md"
    println(s"Line count of $file: " + spark.read.textFile(file).count())
  }
}

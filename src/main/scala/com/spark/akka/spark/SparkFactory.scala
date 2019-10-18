package com.spark.akka.spark

import com.spark.akka.util.AppConfig
import org.apache.spark.sql.SparkSession

/**
  * Creates one SparkSession which is shared and reused among multiple HttpRequests
  */
object SparkFactory {
  val spark: SparkSession = SparkSession.builder
    .master(AppConfig.sparkMaster)
    .appName(AppConfig.sparkAppName)
    .getOrCreate

  val sc = spark.sparkContext
  val sparkConf = sc.getConf
}

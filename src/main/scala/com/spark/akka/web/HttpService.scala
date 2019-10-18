package com.spark.akka.web

import com.spark.akka.spark.SparkFactory
import org.apache.spark.launcher.SparkLauncher

/**
 * Service class computing the value for route bindings "/activeStreams" and "/count" respectively.
 */
object HttpService {

  val sc = SparkFactory.sc

  // To server http://host:port/count route binding
  // Random spark job counting a seq of integers split into 25 partitions
  def count(): String = sc.parallelize(0 to 500000, 25).count.toString

  // To server http://host:port/activeStreams route binding
  // Returns how many streams are active in sparkSession currently
  def activeStreamsInSparkContext(): Int = SparkFactory.spark.streams.active.length

  def jobHandler(): Unit = {
    new SparkLauncher()
      //.setSparkHome("http://localhost:6066/v1/submissions/create")
      //.setJavaHome(JAVA_HOME)
      .setAppResource("/Users/Ambuj/Desktop/testLivyJar.jar")
      .setMainClass("com.ap.spark.LivyTestApp")
      .setMaster("local[*]")
      .startApplication()

  }
}

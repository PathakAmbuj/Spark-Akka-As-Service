package com.spark.akka.util

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Loads default config params from application.conf file.
 * It also supports cmd-line args to override the default values.
 */
object AppConfig {

  val conf: Config = ConfigFactory.load
  val sparkMasterDef: String = conf.getString("spark.master")
  val sparkAppNameDef: String = conf.getString("spark.appname")
  val akkaHttpPortDef: Int = conf.getInt("akka.http.port")

  var akkaHttpPort: Int = akkaHttpPortDef
  var sparkMaster: String = sparkMasterDef
  var sparkAppName: String = sparkAppNameDef

  def main(args: Array[String]): Unit = {
    parse("-m localhost1 --akkaHttpPort 8080".split(" ").toList)
    print(sparkMaster, sparkAppName, akkaHttpPort)
  }

  val usage =
    s"""
This application comes as Spark2.1-REST-Service-Provider using an embedded,
Reactive-Streams-based, fully asynchronous HTTP server (i.e., using akka-http).
So, this application needs config params like AkkaWebPort to bind to, SparkMaster
and SparkAppName

Usage: spark-submit spark-as-service-using-embedded-server.jar [options]
  Options:
  -h, --help
  -m, --master <master_url>                    spark://host:port, mesos://host:port, yarn, or local. Default: $sparkMasterDef
  -n, --name <name>                            A name of your application. Default: $sparkAppNameDef
  -p, --akkaHttpPort <portnumber>              Port where akka-http is binded. Default: $akkaHttpPortDef

Configured 4 routes:
1. homepage - http://host:port - says "hello world"
2. version - http://host:port/version - tells "spark version"
3. activeStreams - http://host:port/activeStreams - tells how many spark streams are active currently
4. count - http://host:port/count - random spark job to count a seq of integers
  """

  @scala.annotation.tailrec
  def parse(list: List[String]): this.type = {

    list match {
      case Nil => this
      case ("--master" | "-m") :: value :: tail => {
        sparkMaster = value
        parse(tail)
      }
      case ("--name" | "-n") :: value :: tail => {
        sparkAppName = value
        parse(tail)
      }
      case ("--akkaHttpPort" | "-p") :: value :: tail => {
        akkaHttpPort = value.toInt
        parse(tail)
      }
      case ("--help" | "-h") :: tail => {
        printUsage(0)
      }
      case _ => {
        printUsage(1)
      }
    }
  }

  def printUsage(exitNumber: Int) = {
    println(usage)
    sys.exit(status = exitNumber)
  }
}

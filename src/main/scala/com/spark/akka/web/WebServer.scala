package com.spark.akka.web

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import com.spark.akka.spark.SparkFactory

/**
 * Http Server definition
 * Configured 4 routes:
 * 1. homepage - http://host:port - says "hello world"
 * 2. version - http://host:port/version - tells "spark version"
 * 3. activeStreams - http://host:port/activeStreams - tells how many spark streams are active currently
 * 4. count - http://host:port/count - random spark job to count a seq of integers
 * 5. submitJob - http://host:port/submitJob - submit job on remote or local spark cluster
 */
object WebServer extends HttpApp {

  override def routes: Route = pathEndOrSingleSlash {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Hello World!! This is Akka responding..</h1>"))
    }
  } ~
    path("version") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Spark version: ${SparkFactory.sc.version}</h1>"))
      }
    } ~
    path("activeStreams") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Current active streams in SparkContext: ${HttpService.activeStreamsInSparkContext()}"))
      }
    } ~
    path("count") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Count 0 to 500000 using Spark with 25 partitions: ${HttpService.count()}"))
      }
    } ~
    path("customer" / IntNumber) { id =>
      complete {
        s"CustId: ${id}"
      }
    } ~
    path("submitJob") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"${HttpService.jobHandler()}<h1>...Job Submitted Successfully...</h1>"))
      }
    } ~
    path("submitForm") {
      get {
        val route =
          formFields('txtUserName, 'txtPassword.as[String])
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,s"""the data is ${route.toString}"""))
      }
    } ~
  path("loadForm") {
    get {
      import java.nio.file.{Files, Paths}
      val content: String = new String(Files.readAllBytes(Paths.get("/Users/Ambuj/IdeaProjects/spark-as-service-using-embedded-server-master/src/main/resources/login.html")))
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
        content
      ))
    }

  }
}
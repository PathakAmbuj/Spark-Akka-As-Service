import akka.http.scaladsl.settings.ServerSettings
import com.spark.akka.util.AppConfig
import com.spark.akka.web.WebServer
import com.typesafe.config.ConfigFactory

object MainApp extends App {

  // init config params from cmd-line args
  AppConfig.parse(this.args.toList)

  // Starting the server
  WebServer.startServer("localhost", AppConfig.akkaHttpPort, ServerSettings(ConfigFactory.load))

  println(s"Server online at http://localhost:", AppConfig.akkaHttpPort, "/")
}

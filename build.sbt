organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `hello` = (project in file("."))
  .aggregate(`hello-api`, `hello-impl`, `hello-stream-api`, `hello-stream-impl`)

lazy val `hello-api` = (project in file("hello-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-impl` = (project in file("hello-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-api`)

lazy val `hello-stream-api` = (project in file("hello-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-stream-impl` = (project in file("hello-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-stream-api`, `hello-api`)

import java.io.Closeable
import scala.sys.process.Process

val startElasticSearch = taskKey[Closeable]("Starts elastic search")

startElasticSearch in ThisBuild := {
  val esVersion = "5.4.0"
  val log = streams.value.log
  val elasticsearch = target.value / s"elasticsearch-$esVersion"

  if (!elasticsearch.exists()) {
    log.info(s"Downloading Elastic Search $esVersion...")
    IO.unzipURL(url(s"https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-$esVersion.zip"), target.value)
    IO.append(elasticsearch / "config" / "log4j2.properties", "\nrootLogger.level = warn\n")
  }

  val binFile = if (sys.props("os.name") == "Windows") {
    elasticsearch / "bin" / "elasticsearch.bat"
  } else {
    elasticsearch / "bin" / "elasticsearch"
  }

  val process = Process(binFile.getAbsolutePath, elasticsearch).run(log)
  log.info("Elastic search started on port 9200")

  new Closeable {
    override def close(): Unit = process.destroy()
  }
}

lagomInfrastructureServices in ThisBuild += (startElasticSearch in ThisBuild).taskValue


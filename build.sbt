import com.github.play2war.plugin._

name := """dont-lose-hope"""

version := "1.0-SNAPSHOT"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.amazonaws" % "aws-java-sdk" % "1.9.13",
  "org.json" % "json" % "20141113")
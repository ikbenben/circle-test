name := """circle-test"""
organization := "com.ikbenben"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += cache
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ikbenben.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ikbenben.binders._"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "FinalProjectScraper",
    libraryDependencies += "org.jsoup" % "jsoup" % "1.17.2",
    libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.1.1"
  )

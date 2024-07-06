ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "FinalProjectScraper",
    libraryDependencies ++= Seq(
      "org.jsoup" % "jsoup" % "1.17.2",
      "net.ruippeixotog" %% "scala-scraper" % "3.1.1",
      "org.seleniumhq.selenium" % "selenium-java" % "4.1.2",
      "org.seleniumhq.selenium" % "selenium-chrome-driver" % "4.1.2",
      "org.scala-js" %% "scalajs-dom" % "1.1.0",
      "com.lihaoyi" %% "upickle" % "1.3.11"

    ),
    // Add Scala.js specific dependencies
    jsDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.1.0"
    )
  )

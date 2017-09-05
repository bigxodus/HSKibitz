name := "EvaluatingServer"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
    "com.twitter" %% "twitter-server" % "1.30.0",
    "com.github.finagle" %% "finch-core" % "0.16.0-M1",
    "com.github.finagle" %% "finch-json4s" % "0.16.0-M1",
    "com.github.finagle" %% "finch-circe" % "0.16.0-M1",
    "io.circe" %% "circe-core" % "0.8.0",
    "io.circe" %% "circe-generic" % "0.8.0",
    "io.circe" %% "circe-parser" % "0.8.0"
)
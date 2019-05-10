lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion = "2.5.22"
lazy val catsVersion = "1.6.0"
lazy val reactiveMongoVer = "0.16.3"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "org.gwgs",
      scalaVersion    := "2.12.8"
    )),
    name := "akka-http-template",
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"            % catsVersion,
      "org.typelevel"     %% "cats-effect"          % "1.2.0",
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "org.reactivemongo" %% "reactivemongo"        % reactiveMongoVer,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test
    ),
    //SI-2712, for improved type inference (required by cats)
    scalacOptions += "-Ypartial-unification"
  )

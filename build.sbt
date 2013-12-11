name := "play2-auth-reactivemongo"

version := "1.0-SNAPSHOT"

val akkaVersion = "2.2.3"

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7")

resolvers ++= Seq(
  "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository",
  "Local ivy" at Path.userHome.asFile.toURI.toURL + ".ivy2/cache",
  "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "oschina mvn" at "http://maven.oschina.net/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "sbt-plugin-releases" at "http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"
)

libraryDependencies ++= Seq(
  cache,
  filters,
  "com.typesafe.akka"  %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"  %% "akka-slf4j"       % akkaVersion,
  "com.typesafe.akka"  %% "akka-testkit"     % akkaVersion,
  "com.typesafe.akka"  %% "akka-contrib"     % akkaVersion,
  "com.typesafe.akka"  %% "akka-cluster"     % akkaVersion,
  "com.typesafe.akka"  %% "akka-remote"      % akkaVersion,
  "com.typesafe.akka"  %% "akka-agent"       % akkaVersion,
  "com.typesafe.akka"  %% "akka-dataflow"    % akkaVersion,
  "com.typesafe.akka"  %% "akka-transactor"  % akkaVersion,
  //"com.typesafe.akka"  %% "akka-persistence-experimental"  % akkaVersion,
  "com.typesafe.akka"  %% "akka-file-mailbox"  % akkaVersion,
  "com.typesafe.akka"  %% "akka-mailboxes-common"  % akkaVersion,
  "ch.qos.logback"      % "logback-classic"  % "1.0.13",
  "com.typesafe.akka"  %% "akka-testkit"     % akkaVersion % "test",
  "com.jsuereth"        %% "scala-arm"      % "1.3",
  "joda-time"           %   "joda-time"     % "2.3",
  "org.joda"            %   "joda-convert"  % "1.5",
  "com.google.protobuf" % "protobuf-java" % "2.5.0",
  "jp.t2v" %% "play2-auth"      % "0.11.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.0-SNAPSHOT",
  "org.specs2"  %% "specs2"           % "2.3.3"      % "test",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.mockito"       %   "mockito-core"      % "1.9.5" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.11.1" withSources() withJavadoc(),
  "junit" % "junit" % "4.11" % "test",
  //"jp.t2v" %% "play2-auth-test" % "0.11.0" % "test" , //exclude("org.junit", "junit")
  "com.novocode" % "junit-interface" % "0.7" % "test->default"
)     

play.Project.playScalaSettings

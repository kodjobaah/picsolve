import com.typesafe.sbt.SbtNativePackager.packageArchetype
import com.typesafe.sbt.packager.archetypes.ServerLoader.{SystemV, Upstart}

packageArchetype.java_server


organization  := "com.todolist"

name := "ToDoList"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += DefaultMavenRepository

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
  "io.spray"            %%  "spray-can"     % sprayV withSources() withJavadoc(),
  "io.spray"            %%  "spray-routing" % sprayV withSources() withJavadoc(),
  "io.spray"            %%  "spray-json" %  "1.3.2" withSources() withJavadoc(),
    "io.spray"            %%  "spray-testkit" % sprayV  % "test" withSources() withJavadoc(),
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV withSources() withJavadoc(),
    "com.typesafe.slick" % "slick-hikaricp_2.11" % "3.1.0" withSources() withJavadoc(),
    "com.typesafe.slick" %% "slick" % "3.1.0" withSources() withJavadoc(),
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.4.189",
    "mysql" % "mysql-connector-java" % "5.1.30",
  "com.zaxxer" % "HikariCP" % "2.4.1",
  "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
  "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test"
  )

}


Revolver.settings: Seq[sbt.Setting[_]]

Revolver.enableDebugging(port=5151, suspend = true)

mainClass in Compile := Some("com.todolist.Boot")

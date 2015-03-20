name := "akka-persistence-jdbc-play"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  anorm,
  cache,
  ws  
)

libraryDependencies ++= Seq(
	"com.github.dnvriend" %% "akka-persistence-jdbc" % "1.1.1",
	"org.postgresql"       % "postgresql"            % "9.4-1201-jdbc41"
)

resolvers += "dnvriend at bintray" at "http://dl.bintray.com/dnvriend/maven"

name in Universal := name.value
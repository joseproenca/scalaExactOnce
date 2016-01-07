name := "ExactlyOnce"
 
version := "1.0"
 
scalaVersion := "2.11.7"
 
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3-SNAPSHOT"
libraryDependencies += "junit" % "junit" % "4.12"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0-M15"

name := "SparkScala17"

version := "0.1"

scalaVersion := "2.11.12"

// https://mvnrepository.com/artifact/com.holdenkarau/spark-testing-base
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "1.5.0_0.3.0" % Test
libraryDependencies += "junit" % "junit" % "4.10"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % Test

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0"
libraryDependencies += "com.databricks" %% "spark-csv" % "1.3.0"
// https://mvnrepository.com/artifact/org.apache.spark/spark-hive-thriftserver
libraryDependencies += "org.apache.spark" %% "spark-hive" % "1.6.0"
//libraryDependencies += "org.apache.spark" % "spark-hive-thriftserver_2.10" % "1.6.2"
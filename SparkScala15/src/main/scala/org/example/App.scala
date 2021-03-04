package org.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.avg
import org.apache.spark.sql.functions.sum
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Hello world!
 *
 */
object App   {
  println( "Hello World!" )

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
  val conf = new SparkConf().setMaster("local").setAppName("OlympicAthletes").set("spark.executor.memory","1g")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    // Athlete,age,country,game,Date,Sport,Gold,Silver,Bronze,Total._

    val df:DataFrame = sqlContext.read.format("com.databricks.spark.csv").option("header","true").load("input-spark/OlympicAthletes.csv")
    df.registerTempTable("Records")
    // doing some aggregations
    df.select("Country","Total").groupBy("Country").agg(sum("Total"),avg("Total")).foreach(println)

    // How many athletes by country are older than 40
    println()
    println("How many athletes by country are older than 40")
    sqlContext.sql("SELECT Country,count(1) from Records where Age >40 group by Country").foreach(println)

    sqlContext.udf.register("strLen",(s:String)=> s.length())
    // use the registered udf to print country with its number or letters
    println()
    println("use the registered udf to print country with its number or letters")
    sqlContext.sql("SELECT Country, strLen(Country) from Records").foreach(println)
    System.in.read()
  }
}

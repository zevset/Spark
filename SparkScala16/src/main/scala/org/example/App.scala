package org.example

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Hello world!
 *
 */
object App   extends App {
  println( "Hello World!" )
val pathToFile = "input-spark/reduced-tweets.json"

  def loadData(): DataFrame ={
    val conf = new SparkConf()
      .setAppName("DataFrame")
      .setMaster("local[*]")

    val sc = new SparkContext(conf)

    val sqlcontext = new SQLContext(sc)

    sqlcontext.read.json(pathToFile)
  }

  def showDataFrame() = {
    val dataframe = loadData
    dataframe.show
  }

  def printSchema() ={
    val dataframe =loadData
    dataframe.printSchema
  }


  def filterByLocation(): DataFrame = {
    val dataframe = loadData
    dataframe.filter(dataframe.col("place").equalTo("Paris")).toDF()
  }

  def mostPopularTwitterer():(Long,String) = {
    val dataframe = loadData

    dataframe.groupBy(dataframe.col("user"))
      .count()
      .rdd
      .map(x=> (x.get(1).asInstanceOf[Long], x.get(0).asInstanceOf[String]))
      .sortBy(_._1,false,1)
      .first()
  }


  //showDataFrame()
  //printSchema()
  //filterByLocation().show()
  println(mostPopularTwitterer())
}


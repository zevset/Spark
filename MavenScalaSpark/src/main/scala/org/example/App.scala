package org.example

import org.apache.spark._

object App {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("TestSpark")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List( 1, 2, 3, 4, 5))
    rdd.collect().foreach(println)
  }

}
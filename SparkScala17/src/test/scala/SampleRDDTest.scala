import com.holdenkarau.spark.testing.{RDDComparisons, SharedSparkContext}
import org.scalatest.FunSuite

class SampleRDDTest extends FunSuite with SharedSparkContext{
  //val sparkConf = new SparkConf().setAppName("mitest2").setMaster("local[*]")
  //override val sc = new SparkContext(sparkConf)

  test("really simple transformation"){

    val input = List("hi","hi holden","bye")
    val expected = List(List("hi"),List("hi","holden"), List("bye"))
    assert(expected === sc.parallelize(input).map(_.split(" ").toList).collect().toList )
  }

  test("really simple transformation with rdd - rdd comparision"){
    //val sc = new SparkContext(sparkConf)
    val input = List("hi","hi holden","bye")
    val expected = List(List("hi"),List("hi","holden"),List("bye"))
    assert( None === RDDComparisons
      .compare(sc.parallelize(expected), sc.parallelize(input).map(_.split(" ").toList)))
  }
}

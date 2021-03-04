import com.holdenkarau.spark.testing.{DataFrameSuiteBase, SharedSparkContext}
//import org.apache.spark.sql.hive.HiveContext
import org.scalatest.FunSuite

class SampleDataFrameTest extends FunSuite with SharedSparkContext with DataFrameSuiteBase
{

  val diffByteArray = Array[Byte](192.toByte)
  val inputList = List(Magic("panda",9001.0),
                      Magic("coffee",9002.0))
  val inputList2 = List(Magic("panda",9001.0 + 1E-6),
                        Magic("coffee",9002.0))

  test("dataframe should be equal to its self"){
    //val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //import sqlContext.implicits._

    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    val input = sc.parallelize(inputList).toDF
    equalDataFrames(input,input)
  }

  test("unequal dataframes should  not be equal"){
    //val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //import sqlContext.implicits._
    //val hiveCtx = new HiveContext(sc)

    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    val input = sc.parallelize(inputList).toDF
    val input2 = sc.parallelize(inputList2).toDF
    intercept[org.scalatest.exceptions.TestFailedException]{
      equalDataFrames(input,input2)
    }
  }

  test("unequal dataframe should not be equal when length differs") {
    //val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //import sqlContext.implicits._

    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    val input = sc.parallelize(inputList).toDF
    val input2 = sc.parallelize(inputList.headOption.toSeq).toDF
    intercept[org.scalatest.exceptions.TestFailedException] {
      equalDataFrames(input, input2)
    }
  }
}

case class Magic(name: String, power: Double)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
//this is used to emplicitu convert and RDD to a DataFrame
import sqlContext.implicits._

case class User(id: Int, gender: String, age: Int, occupation: String, zip: String)
case class Rating(userId: Int, movieId: Int,rating: Int, tm: Long)

val users = sc.textFile("input-spark/users.dat").map(_.split("::")).map(row => User( row(0).toInt,row(1),row(2).toInt, row(3),row(4))).toDF
val rating = sc.textFile("input-spark/ratings.dat").map(_.split("::")).map(row => Rating(row(0).toInt,row(1).toInt,row(2).toInt,row(3).toLong)).toDF

users.show
rating.show
users.registerTempTable("USERS")
rating.registerTempTable("RATINGS")

val resultSet = sqlContext.sql("SELECT age, AVG(rating) FROM USERS JOIN RATINGS ON (USERS.id = RATINGS.userId) GROUP BY age")
resultSet.collect

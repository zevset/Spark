import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source


object MovieLensALS {

  def main(args: Array[String]): Unit = {
    println("Q1")
    println(args(0))
    println(args(1))
    val conf = new SparkConf().setAppName("MovieLensALS").set("spark.executor.memory","1g")
    val sc = new SparkContext(conf)

    println("Q2")
    println(args(0))
    println(args(1))
    //load personal ratings
    val myRatings = loadRatings(args(0))
    val myRatingsRDD = sc.parallelize(myRatings)

    println("Q3")
    //load ratings and movie titles
    val movieLensHomeDir = args(1)
    println("Q4")
    // ratings is an RDD of (last digit of timestamps, (userId,movieId,rating))
    val ratings = sc.textFile(movieLensHomeDir + "ratings.dat").map(parseRating)
    println("Q5")
    // movies is an RDD of (movieId,movieTitle)
    val movies = sc.textFile(movieLensHomeDir + "movies.dat").map(parseMovie)
    println("Q6")
    // used later in recomendation
    val moviesMap = movies.collect.toMap
    println("Q7")
    // your code here
    val numRatings = ratings.count
    val numUsers = ratings.values.map(r => r.user).distinct().count()
    val numMovies = ratings.values.map(r => r.product).distinct().count()
    println(s"Got ${numRatings} ratings from ${numUsers} users on ${numMovies} movies.")

    // split rating into train (%60), validation(%20), and test(%20) based on the
    // last digit of the timestamp, add my Ratings to train, and cache them
    // training, validation, test are all RDDs of (userId,movieId,rating)
    // val numPartition = 4 // <-- not used because executed in local
    val training = ratings.filter(pair => pair._1 < 6).values.union(myRatingsRDD)
      /*repartition(numPartitions.*/.cache()

    val validation = ratings.filter(pair => pair._1 >= 6 && pair._1 < 8).values
      /*repartition(numPartitions.*/.cache()

    val test = ratings.filter(pair => pair._1 >= 8).values
      /*repartition(numPartitions.*/.cache()

    val numTraining = training.count
    val numValidation = validation.count
    val numTest = test.count

    println(s"Training: ${numTraining}, Validation: ${numValidation}, numTest: ${numTest}")

    // train models and evaluate then on the validation set
    val ranks = List(8,12)
    val lambdas = List(0.1,10.0)
    val numIters = List(10,20)
    var bestModel: Option[MatrixFactorizationModel] = None
    var bestValidationRmse = Double.MaxValue
    var bestRank = 0
    var bestLambda = -1.0
    var bestNumIter = -1

    for(rank <- ranks; lambda <- lambdas; numIter <- numIters){
      val model = ALS.train(training, rank,numIter,lambda)
      val validationRmse = computeRmse(model, validation, numValidation)
      println("RMSE (validation) = " + validationRmse + " for the model trained with rannk = "
      + rank + ", lambda = " + lambda + ",  and numIter = " + numIter + ".")
      if(validationRmse < bestValidationRmse){
        bestModel = Some(model)
        bestValidationRmse = validationRmse
        bestRank = rank
        bestLambda = lambda
        bestNumIter = numIter
      }
    }

    val testRmse = computeRmse(bestModel.get, test,numTest)
    println("The best model was trained with rank = "+ bestRank + "  and lambda = "+ bestLambda
    +  ", and numIter = " + bestNumIter + ", and its RMSE on the test set is " + testRmse + ".")

    // make personalized recommendations
    val myRatedMovieIds = myRatings.map(_.product)
    val moviesNotSeen = movies.keys.filter(!myRatedMovieIds.contains(_)).collect()
    val candidates = sc.parallelize(moviesNotSeen)
    val recommendations = bestModel.get
      .predict(candidates.map((0, _)))
      .collect()
      .sortBy(- _.rating)
      .take(50)

    var i = 1
    println("Movies recommended for you:")
      recommendations.foreach { r =>
      println("%2d".format(i) + ": " + moviesMap(r.product))
      i += 1
    }

    // shut down the SparkContext
    sc.stop()
  }


  // Parses a rating record in MovieLens format userId::movieId::rating::timestamp
  def parseRating(line: String) = {
    val fields = line.split("::")

    // to make later the partition easy for training, validation and test
    (fields(3).toLong %10, Rating(fields(0).toInt,fields(1).toInt,fields(2).toDouble))
  }

  // Parses a movie record in MovieLens format movieId::movieTitle
  def parseMovie(line: String) = {
    val fields = line.split("::")
    (fields(0).toInt,fields(1))
  }

  // Compuse RMSE (Root Mean Squared Error)
  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], n: Long): Double = {
    val predictions: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))
    val predictionsAndRatings = predictions.map(x => ((x.user, x.product), x.rating))
      .join(data.map(x => ((x.user, x.product), x.rating)))
      .values
    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ + _) / n)
  }

  // load ratings from file
  def loadRatings(ratingsFile: String) = {
    try {
      println("E1")
      val fileContents = Source.fromFile(ratingsFile).getLines.toList
      println("E2")
      val ratings = fileContents.map { line =>
        val fields = line.split("::")
        Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
      }.filter(_.rating > 0.0)
      println("E3")
      if (ratings.isEmpty) {
        sys.error("No ratings provided.")
      } else {
        ratings
      }
    }catch{
      case ex: Exception => println("erick: "+ex)
        sys.exit(-1)
    }
  }
}

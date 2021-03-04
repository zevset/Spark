val data = (1 to 100).toList
val rdd = sc.parallelize(data)
rdd.count
rdd.cache
rdd.take(10)
rdd.take(10).foreach(println(_))
rdd.foreach(println(_))
rdd.map(_ * 2).foreach(println(_))
rdd.map(i => (i %10, 1)).reduceByKey(_ + _).foreach(println(_))

val data = sc.textFile("input-spark/quixote.txt")
data.cache
data.take(10).foreach(println(_))
val counts ) data.flatMap(line => line.split(" ")).map(word=> (word,1)).reduceByKey(_+_)
counts.saveAsTextFile("quixote-wordcount.spark")

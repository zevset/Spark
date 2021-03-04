val quixote = sc.textFile("input-spark/quixote.txt")
val wordLengths = quixote.flatMap(line => line.split(" ")).filter(word => word.length >4 && !List("Quixote","Sancho").contains(word)).map(_.length).cache
val accum = sc.longAccumulator(0,"AccumLength")
wordLengths.foreach(length => accum += length)
accum.value.toDouble / wordLengths.count

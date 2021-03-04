val logfile = "input-spark/weblogs"
val targetfile = "input-spark/targetmodels.txt"

import scala.io.Source
val targetlist = Source.fromFile(targetfile).getLines.toList
//broadcast the target list to all workers
val targetlistbc = sc.broadcast(targetlist)
//filter out request from diveces not in the target list
val targetregs = sc.textFile(logfile).filter(line => targetlistbc.value.count(line.contains(_))>0)
targetregs.take(5).foreach(println)

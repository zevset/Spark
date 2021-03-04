//import org.apache.spark.SparkContext
//import org.apache.spark.SparkConf
//import org.apache.spark.util

val jpgcount = sc.longAccumulator(0)
val htmlcount = sc.accumulator(0)
val csscount = sc.accumulator(0)
val filename = "input-spark/weblogs"
val logs = sc.textFile(filename)
logs.foreach(line =>{
if (line.contains(".html")){ htmlcount += 1}
else if (line.contains(".jpg")) {jpgcount += 1}
else if (line.contains(".css")){ csscount += 1}
})
println("Request Totals:")
println(".css request: " + csscount.value)
println(".html request: " + htmlcount.value)
println(".jpg request: " + jpgcount.value)

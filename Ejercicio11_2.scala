val jpgcount = sc.longAccumulator("data for jpg")
val htmlcount = sc.longAccumulator("data fom html")
val csscount = sc.longAccumulator("data for css")
val filename = "input-spark/weblogs"
val logs = sc.textFile(filename)
logs.foreach(line =>{
if (line.contains(".html")){ htmlcount.add(1)}
else if (line.contains(".jpg")) {jpgcount.add(1)}
else if (line.contains(".css")){ csscount.add(1)}
})
println("Request Totals:")
println(".css request: " + csscount.value)
println(".html request: " + htmlcount.value)
println(".jpg request: " + jpgcount.value)

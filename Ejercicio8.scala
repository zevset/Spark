import scala.xml._

//get the activation XML File
var filename = "input-spark/activations/activations"
var activations = sc.textFile(filename)

def getactivations(fileiterator: Iterator[String]): Iterator[Node] = {
	val nodes = XML.loadString(fileiterator.mkString) \\ "activation"
	nodes.toIterator 
}

var activationTrees = activations.mapPartitions(getactivations)

def getmodel(activation: Node): String = {
	(activation \ "model").text
}

var models = activationTrees.map(getmodel)
println(models.toDebugString)

val modelcounts = models.map(model => (model,1)).reduceByKey((v1,v2) => v1+v2)
println(modelcounts.toDebugString)


for(pair <- modelcounts.map(_.swap).top(10))
println("model %s (%s)".format(pair._2, pair._1))

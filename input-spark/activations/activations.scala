// load XML files containing device activation records.
// Find the most common device models activated

import scala.xml._

// Given a partition containing multi-line XML, parse the contents.
// Return an iterator of activation XML nodes contained in the partition

def getactivations(fileiterator: Iterator[String]): Iterator[Node] = {
val nodes = XML.loadString(fileiterator.mkString) \\ "activation"
nodes.toIterator
}

// Get the model name from a device activation record
def getmodel(activation: Node): String = {
(activation \ "model").text
}


// activation XML files
var filename="input-spark/activations/activations"

// parse each partition as a file into an activation XML record
var activations = sc.textFile(filename)
var activationTrees = activations.mapPartitions(getactivations)

// Map each activation record to a device model name
var models = activationTrees.map(getmodel)

// Show the partitioning
println(models.toDebugString)

// count activations by model
var modelcounts = models.map(model => (model,1)).reduceByKey((v1,v2) => v1+v2)

// Show the partitioning
println(modelcounts.toDebugString)

// display the top 10 models
for (pair <- modelcounts.map(_.swap).top(10)) println("Model %s (%s)".format(pair._2,pair._1))

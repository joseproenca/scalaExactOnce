Exactly-Once Quantity Transfer
========

This project is a small Scala implementation of the algorithm described in the paper ["Exactly-once quantity transfer"](http://) by Ali Shoker et al.

It uses Akka actors to represent different agents that communicate with each other to reach a concensus about a global quantity, whereas each agent can be notified of new or consumed resources from the global quantity.

To compile and run the tests you only need [sbt](http://www.scala-sbt.org), and you just need to type ```sbt test```. This will run the only test in this project in [TestAgents.scala](https://github.com/joseproenca/scalaExactOnce/blob/master/src/test/scala/exactOnce/TestAgents.scala), and print the result of 5 rounds of evolution.
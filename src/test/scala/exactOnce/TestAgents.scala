/**
  * Created by jose on 07/01/16.
  */
package exactOnce

import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._
import SplitableInsts.SplitableFloat // implicit
import scala.concurrent.ExecutionContext.Implicits.global // implicit

class TestAgents extends JUnitSuite {

  val system = ActorSystem("exactOnce")

  /**
    * Run 2 agents 5 times, with initial values 42 and 50.
    * Add 1 to the 1st and take 3 from the second after 1 round, and check if they converge to 45.
    * Wait 100 milisecs between the execution of each agent.
    */
  @Test def TestPrintFloats(): Unit = {

    val r1 = system.actorOf(Props(new Agent[Float](1,42)), name = "1-42")
    val r2 = system.actorOf(Props(new Agent[Float](2,50)), name = "2-50")

    println("## initial ##")
    r1 ! 'Show
    r2 ! 'Show
    Thread.sleep(100)

    for (i <- 1 until 5) {
      println(s"## round $i ##")
      r1 ! (r2,2); Thread.sleep(100)
      r2 ! (r1,1); Thread.sleep(100)
      //
      if (i==1) {r1! Plus[Float](1); r2 ! Minus[Float](3)}
    }

    var success = true
    implicit val timeout = Timeout(200,scala.concurrent.duration.MILLISECONDS)

    (r1 ? 'GetVal) onComplete { case v => success &= v.get == 45.toFloat }
    (r2 ? 'GetVal) onComplete { case v => success &= v.get == 45.toFloat }

    Thread.sleep(100)
    assert(success)
  }
  
}

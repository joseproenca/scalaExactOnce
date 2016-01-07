package exactOnce

import akka.actor._

/** An agent is an actor with state given by 'Node',
  * and reacts to messages (Nodes) sent by other actors.
  */
class Agent[Data](id:Int, v_init:Data)(implicit split: Splitable[Data])
extends Actor {

  private var me = new Node(id,v_init,0,0,Map(),Map())
  
  def receive = {
    // get the Node information from another agent
    case other:Node[Data] =>
      me = fillSlots(me,other)
      me = createSlot(me,other)
      me = GCtokens(me,other)
      me = createToken(me,other)
      println(s" -> $me")

    // Triggers the sending of this Node to another given agent.
    case a:(ActorRef,Int) => a._1 ! me.projection(a._2)
    
    // Updates its internal value by adding or removing a value
    case Plus(x:Data) =>
      me = me.setVal(split.plus(me.value,x))
      println(s"added $x -- $me")
    case Minus(x:Data) =>
      me = me.setVal(split.split(me.value,x)._1)
      println(s"took $x -- $me")

    // used for debugging/testing
    case 'Show => println(s"--- $me ---")
    case 'GetVal => sender ! me.value
    case x:Any => println("unknown message: "+x)
  }
  
  
  // Auxiliar functions
  private def fillSlots(i:Node[Data],j:Node[Data]) =
    if ((j.tokens contains i.id) && (i.slots contains j.id) &&
         j.tokens(i.id).sck == i.slots(j.id).sck &&
         j.tokens(i.id).dck == i.slots(j.id).dck )
//      (i+j.tokens(i.id).d) takeSlot j.id
      i.setVal(split.plus(i.value,j.tokens(i.id).d)) takeSlot j.id
    else if ((i.slots contains j.id) && (j.sck > i.slots(j.id).sck))
      i takeSlot j.id
    else
      i
      
  private def createSlot(i:Node[Data],j:Node[Data]) = {
    val h = split.needs(i.value,j.value)
    if (!i.slots.contains(j.id) && (h != 0))
      i.addSlot(j.id -> Token(j.sck,i.dck,h)).incDck
    else
      i
  }
    
  private def GCtokens(i:Node[Data],j:Node[Data]) =
    if (i.tokens.contains(j.id) &&
         (( j.slots.contains(i.id) && i.tokens(j.id).dck < j.slots(i.id).dck) ||
          (!j.slots.contains(i.id) && i.tokens(j.id).dck < j.dck))) {
      i takeToken j.id
    }
    else
      i
      
  private def createToken(i:Node[Data],j:Node[Data]) = 
    if (j.slots.contains(i.id) && j.slots(i.id).sck==i.sck) {
      val (v,q) = split.split(i.value, j.slots(i.id).d)
      i.addToken(j.id -> Token(j.slots(i.id).sck,j.slots(i.id).dck,q)).
        setVal(v).
        incSck
    }
    else
      i     
}


case class Plus[D](v:D)
case class Minus[D](v:D)


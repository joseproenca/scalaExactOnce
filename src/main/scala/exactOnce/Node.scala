package exactOnce

/** A 'Node' represents the internal state of an agent,
  * and the messages exchanged between agents.
  *
  * @param id     unique ID of the agent that owns this node
  * @param value  current value of the node
  * @param sck    source clock (counter)
  * @param dck    destinatino clock (counter)
  * @param slots  map from source      agents to associated counters and data
  * @param tokens map from destination agents to associated counters and data
  * @tparam Data  type of (splitable) data being maintained.
  */
case class Node[Data](
    id:Int, value:Data, sck:Int, dck:Int,
    slots:Map[Int,Token[Data]],
    tokens:Map[Int,Token[Data]]) {
  
  def fetch = value
  def setVal(d:Data)          = new Node(id,d,sck,dck,slots,tokens)
  def takeSlot(other:Int)     = new Node(id,value,sck,dck,slots-other,tokens)
  def takeToken(other:Int)    = new Node(id,value,sck,dck,slots,tokens-other)
  def addSlot(s:(Int,Token[Data]))  = new Node(id,value,sck,dck,slots+s,tokens)
  def addToken(s:(Int,Token[Data])) = new Node(id,value,sck,dck,slots,tokens+s)
  def incSck                  = new Node(id,value,sck+1,dck,slots,tokens)
  def incDck                  = new Node(id,value,sck,dck+1,slots,tokens)
  def projection(other:Int)   = new Node(id,value,sck,dck
                                        ,slots.filterKeys(_==other)
                                        ,tokens.filterKeys(_==other))
          
  override def toString =
    s"<$id,$value,$sck,$dck,$slots,$tokens>"
}

/** A token has a pair of clocks (or counters) for the source and destination, and a data value 'd'. */
case class Token[Data](sck:Int,dck:Int,d:Data) {
  def ck = (sck,dck)
}


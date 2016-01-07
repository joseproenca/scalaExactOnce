package exactOnce

trait Splitable[Data] {
  def plus (x:Data,y:Data): Data
  def needs(x:Data,y:Data): Data
  def split(x:Data,h:Data): (Data,Data)
}


object SplitableInsts {
  /** defines static functions for splitable elements */
  implicit object SplitableFloat extends Splitable[Float] {
    def plus(x:Float,y:Float) = x+y
    def needs(x:Float,y:Float)  = (y - x + math.abs(y - x))/4
    def split(x:Float,h:Float) = (
      (x - h + math.abs(x-h))/2,
      (x + h - math.abs(x-h))/2
    )
  }
}

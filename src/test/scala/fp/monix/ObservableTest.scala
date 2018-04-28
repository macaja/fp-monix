package fp.monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.{Consumer, Observable}
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.io.Source

class ObservableTest extends AsyncWordSpec with Matchers{

  "Monix Observable" should{
    /*"emit events with interval" in{
      val tick = {
        Observable.interval(1.second)
          // common filtering and mapping
          .filter(_ % 2 == 0)
          .map(_ * 2)
          // any respectable Scala type has flatMap, w00t!
          .flatMap(x => Observable.fromIterable(Seq(x,x+1)))
          // only take the first 5 elements, then stop
          .take(20)
          // to print the generated events to console
          .dump("Out")
      }.subscribe()
      println(s"observable => $tick")
      1 should be(1)
    }*/
  }
  "x" in{
    val filename = "/home/mauricio/projects/mine/fp-monix/test.txt"
    val file: Iterator[String] = Source.fromFile(filename).getLines
    def x = Observable.fromIterator(file).map(l => {
      val result = s"$l with observable"
      /*println(l)
      println(result)*/
      result
    }).consumeWith(consumer)

    def consumer: Consumer[String, String] = Consumer.foldLeftTask("")((x,s) => Task.now(x + s))
    println(s"${x.runAsync}")
    1 should be(1)
  }

}

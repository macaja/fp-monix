package fp.monix

import org.scalatest.{AsyncWordSpec, Matchers}
import monix.reactive.Observable

import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits.global

class ObservableTest extends AsyncWordSpec with Matchers{

  "Monix Observable" should{
    "" in{
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
    }
  }

}

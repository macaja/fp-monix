package fp.monix

import monix.execution.Scheduler

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

object Main extends App with Services {

  implicit val file = "test.txt"
  implicit val delay: FiniteDuration = 30.seconds
  implicit val sc: Scheduler = Scheduler.io()

  println("Initializing job !!!")
  processFile.runAsync

}

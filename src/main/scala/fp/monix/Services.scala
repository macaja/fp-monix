package fp.monix

import fp.monix.domain.Info
import monix.eval.Task
import monix.reactive.{Consumer, Observable}

import scala.concurrent.duration._

trait Services {
  implicit def delay: FiniteDuration
  implicit def file: String

  val empty: Iterator[String] = Iterator.empty

  def processFile = (for{
    _ <- Observable.interval(delay)
    b <- Observable.now(FileClient.download(file).fold(empty)(_.getLines))
    c <- Observable.fromIterator(b)
  } yield c).consumeWith(consumerDownloadedFile) onErrorRestart(2)

  def consumerDownloadedFile: Consumer[String, Info] = Consumer.foldLeftTask[Info,String](Info(""))((i, s) => {
    val info = i.copy(data = s)
    println(s"Task info => ${Task.now(info)}")
    Task.now(info)
  })

}

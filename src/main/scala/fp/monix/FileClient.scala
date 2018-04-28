package fp.monix

import java.text.SimpleDateFormat
import java.util.Date

import scala.io.{BufferedSource, Source}
import scala.util.Try

object FileClient {
  def download(filename: String): Option[BufferedSource] =
    Try(Source.fromFile(s"/home/mauricio/projects/mine/fp-monix/$filename")).fold(_ => {println(s"No file found at ${currentTime}");None},b=> {
      println(s"file founded")
      Some(b)
    })

  def currentTime = {
    val now = System.currentTimeMillis()
    val sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm")
    val date = new Date(now)
    sdf.format(date)
  }
}

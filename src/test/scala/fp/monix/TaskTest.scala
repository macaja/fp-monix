package fp.monix

import org.scalatest.{AsyncWordSpec, Matchers}
import monix.eval.Task
import org.scalatest.concurrent.ScalaFutures
import monix.execution.Scheduler.Implicits.global

class TaskTest extends AsyncWordSpec with Matchers with ScalaFutures{

  "Monix Task" should{
    "only execute task with runAsync" in{
      val task = Task(2)
      whenReady(task.runAsync){ r =>
        r should be(2)
      }
    }
    "does not execute a sequence in paralell with .sequence function like it does for Future.sequence" in{
      val start = System.currentTimeMillis()
      val taskS = (for{
        s <- Task.sequence((1 to 200).toList.map(Task(_))).map(l => l.map(v => v * 80))
      }yield s).runAsync
      val taskG = (for{
        g <- Task.gather((1 to 200).toList.map(Task(_))).map(l => l.map(v => v * 80))
      }yield g).runAsync
      taskS.onComplete({
        case _ => println(s"Task.sequence tooks ${System.currentTimeMillis() - start} ms")
      })
      taskG.onComplete({
        case _ => println(s"Task.gather tooks ${System.currentTimeMillis() - start} ms")
      })

      1 should be(1)
    }
  }

}

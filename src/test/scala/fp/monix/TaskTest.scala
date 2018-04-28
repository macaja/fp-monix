package fp.monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.Future
import scala.concurrent.duration._

class TaskTest extends AsyncWordSpec with Matchers with ScalaFutures{

  "Monix Task" should{
    "only execute task with runAsync" in{
      val task = Task(2)
      println(s"task un-triggered $task")
      val taskr = task.runAsync
      println(s"task triggered $taskr")
      whenReady(taskr){ r =>
        r should be(2)
      }
    }
    "does not execute a sequence in parallel with .sequence function like it does for Future.sequence" in{
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

    "don’t remember their results by default" in{
      val total = Task {
        println("Computing 2 + 2")
        2 + 2
      }
      println(s"total 1 ${total.runAsync}")
      println(s"total 2 ${total.runAsync}")
      1 should be(1)
    }

    "memoize result as Future does" in{
      val total = Task {
        println("Computing 5 + 5")
        5 + 5
      }.memoize
      println(s"total 1 memoize ${total.runAsync}")
      println(s"total 2 memoize ${total.runAsync}")
      1 should be(1)
    }
    "evalOnce" in{
      val task = Task.evalOnce{
        println("Computing evalOnce")
        1 + 1
      } //memoize on the fisrt evaluation
      println(s"task 1 evalOnce ${task.runAsync}")
      println(s"task 2 evalOnce ${task.runAsync}")
      1 should be(1)
    }

    "delay" in{ //alias for eval
      val delayTask = Task.delay{
        println("Computing delay")
        1 + 1
      }
      println(s"total 1 memoize ${delayTask.runAsync}")
      println(s"total 2 memoize ${delayTask.runAsync}")
      1 should be(1)
    }

    "delay execution" in{
      val task = Task{
        println("Computing delay execution")
        1 + 1
      }.delayExecution(10.seconds)
      println(s"delay execution 1 ${task.runAsync}")
      println(s"delay execution 2 ${task.runAsync}")
      1 should be(1)
    }

    "delay result" in{
      val task = Task{
        println("Computing delay result")
        1 + 1
      }.delayResult(10.seconds)
      println(s"delay result 1 ${task.runAsync}")
      println(s"delay result 2 ${task.runAsync}")
      1 should be(1)
    }

    "equivalents of Future.successful" in{
      val pure = Task.pure(1)  //is executed strait away on the current thread
      val now = Task.now(2)  //is executed strait away on the current thread
      val eval = Task.eval(3) // lazy, doesn't trigger until runAsync
      println(s"pure => $pure")
      println(s"now => $now")
      println(s"eval => $eval")
      1 should be(1)
    }

    "from Future" in{
      val future = Future(1)
      val task = Task.fromFuture(future) //execute the Future right now
      val taskDefer = Task.deferFuture(future) //don't execute the future until runAsync
      println(s"task => ${task}")
      println(s"task deferFuture => ${taskDefer}")
      1 should be(1)
    }

    "error handling" in{
      val task = Task.eval(1)
      task onErrorRestart(3)
      task onErrorRestartIf(_.equals(NullPointerException))
      task onCancelRaiseError(new Exception(""))
      task onErrorHandle(_ => 4)
      task onErrorHandleWith{ case _ => Task.now(2)}

      1 should be(1)
    }
  }

}

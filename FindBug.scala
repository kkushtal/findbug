import java.time.Instant

import scala.util.{Failure, Success, Try}

object FindBug extends App {

  case class Result(value: Try[String])

  case class Batch(results: Seq[Result])

  val batch = Batch((1 to 5000).map {
    case i if i % 1000 == 0 => Failure(new RuntimeException(s"failed at $i"))
    case i => Success(s"result $i")
  }.map(Result))


  def processResult(errLogRecord: String)(result: Result): Unit = result match {
    case Result(Failure(_)) =>
      println(s"result $result failed: $errLogRecord")
    case Result(Success(_)) =>
  }

  //here goes!
  val startTime = Instant.now()

  val curried = processResult(s"in batch ${batch.toString}") _
  batch.results.map(curried)

  println(s"processed in ${Instant.now().toEpochMilli - startTime.toEpochMilli} millis")
}



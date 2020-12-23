import java.time.Instant
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object FindBug extends App {

  case class Result(value: Try[String])

  case class Batch(results: Seq[Result])

  val batch = Batch((1 to 5000).map {
    case i if i % 1000 == 0 => Failure(new RuntimeException(s"failed at $i"))
    case i => Success(s"result $i")
  }.map(Result))

  def processResult(result: Result, errLogRecord: String): Unit = result match {
    case Result(Failure(_)) =>
      println(s"result $result failed: $errLogRecord")
    case Result(Success(_)) =>
  }

  @tailrec def processResultRec(results: Seq[Result], errLogRecord: String): Unit = results match {
    case results if results.isEmpty => ()
    case results =>
      processResult(results.head, errLogRecord)
      processResultRec(results.tail, errLogRecord)
  }

  //here goes!
  val startTime = Instant.now()

  processResultRec(batch.results, s"in batch ${batch.toString}")

  println(s"processed in ${Instant.now().toEpochMilli - startTime.toEpochMilli} millis")
}



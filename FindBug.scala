import java.time.Instant

import scala.util.{Failure, Success, Try}

object FindBug extends App {

  case class Result(value: Try[String])

  case class Batch(results: Seq[Result])

  val batch = Batch((1 to 5000).map {
    case i if i % 1000 == 0 => Failure(new RuntimeException(s"failed at $i"))
    case i => Success(s"result $i")
  }.map(Result))

  //here goes!
  val startTime = Instant.now()

  val batchString = batch.toString
  val result = batch.results.collect {
    case result@Result(Failure(_)) => s"result $result failed: $batchString"
  }.mkString(", ")
  println(result)

  println(s"processed in ${Instant.now().toEpochMilli - startTime.toEpochMilli} millis")
}



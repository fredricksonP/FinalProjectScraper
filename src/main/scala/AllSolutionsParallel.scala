import scala.sys.process.*
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration.Duration

object AllSolutionsParallel extends App {
  // Path to the Python script
  implicit val ec: ExecutionContext = ExecutionContext.global

  //Function to start python scripts as futures so they can be run in parallel
  def pythonScriptFuture(scriptPath: String): Future[Int] = Future {
    //Environment path
    val pythonVenvPath = "PythonScraper/venv/bin/python" 
    val command = s"$pythonVenvPath $scriptPath"
    val exitCode = command.!
    exitCode
  }

  //Old sequential soloution below
  
//  val pythonVenvPath = "PythonScraper/venv" 
//  val pythonVenvPath = "PythonScraper/venv/bin/python" 
//  val scriptPath = "PythonScraper/realtyAutoScraper.py" 
//  val command = s"$pythonVenvPath $scriptPath"
//    val exitCode = command.!
  // Check the exit code
  //    if (exitCode == 0) {
  //      println("Python script executed successfully.")
  //    } else {
  //      println(s"Python script execution failed with exit code $exitCode.")
  //    }

    //Specify file paths
    val startTime = System.nanoTime()
    val bankgkokRealty = pythonScriptFuture("PythonScraper/realtyAutoScraper.py")
    val phuketRealty = pythonScriptFuture("PythonScraper/phuketScraper.py")

    val futureBooks: Future[Unit] = Future {
      parallelScaperWithData.runLogic()
    }

    val result = Await.result(phuketRealty, Duration.Inf)
    val _ = Await.result(bankgkokRealty, Duration.Inf)
    val _ = Await.result(futureBooks, Duration.Inf)
    val endTime = System.nanoTime()
    val durationInSeconds = (endTime - startTime) / 1e9
    println(s"Time taken for 3 functions: $durationInSeconds seconds")
}

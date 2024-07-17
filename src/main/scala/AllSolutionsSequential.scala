import scala.sys.process.*

object AllSolutionsSequential extends App{
  val startTime = System.nanoTime()
  //Function to start python scripts as futures so they can be run in parallel
//  def pythonScriptExecute(scriptPath: String): Future[Int] = Future {
//    val pythonVenvPath = "PythonScraper/venv/bin/python" 
//    //    val scriptPath = "PythonScraper/realtyAutoScraper.py" 
//    val command = s"$pythonVenvPath $scriptPath"
//    val exitCode = command.!
//    exitCode
//  }

    //Run the Bangkok Realty Scraper
    val pythonVenvPath = "PythonScraper/venv/bin/python" 
    val scriptPath = "PythonScraper/realtyAutoScraper.py" 
    
    val command = s"$pythonVenvPath $scriptPath"
    val exitCode = command.!
//   Check the exit code
    if (exitCode == 0) {
      println("Python script executed successfully.")
    } else {
      println(s"Python script execution failed with exit code $exitCode.")
    }

  //Run the Phuket Realty Scraper
    val commandTwo = s"$pythonVenvPath $scriptPath"
    val exitCodeTwo = command.!
    //   Check the exit code
    if (exitCode == 0) {
      println("Python script executed successfully.")
    } else {
      println(s"Python script execution failed with exit code $exitCode.")
    }

  booksToScrapeSequential.runLogic()
  
  val endTime = System.nanoTime()
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken for 3 functions run sequentially: $durationInSeconds seconds")
}

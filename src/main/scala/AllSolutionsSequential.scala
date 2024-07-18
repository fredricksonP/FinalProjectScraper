import scala.sys.process.*

/*
  The purpose of this file is to run a number of my web scraping solutions in a sequential setting
  to obtain the large amount of target data for calculations I want to make. This is the sequential
  version. This version starts a Bangkok webscraper that obtains listings data from the Bangkok area.
  Afterwards, a different part of the realty website is scraped to obtain listings data from phuket.
  Finally, 1000 books are scraped from books.toscrape.com sequentally. The ultimate result is code
  that takes a long time to run. Around 300 plus seconds. Check out the AllSolutionsParallel version
  if you don't want to wait that long.
 */
object AllSolutionsSequential extends App{


  //Start system timer to produce quantifiable results
  val startTime = System.nanoTime()

    //Run the Bangkok and Phuket Realty Scrapers
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

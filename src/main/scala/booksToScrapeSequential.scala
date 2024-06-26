import SequentialRealatorScraper.{browser, htmlListingElement}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import scala.io.Source

object booksToScrapeSequential extends App{
  //open the csv file of all links to books to scrape
  // Specify the path to your file
  val filePath = "src/main/scala/bookslinks.csv"

  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Get the start time
  val startTime = System.nanoTime()
  // Read each line
  for (line <- bufferedSource.getLines()) {
    // Print the line (or do any processing)
    println(line)
    val doc = browser.get(line)
    val bookTitle = doc  >> text("h1")
    println(bookTitle)
  }
  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val duration = endTime - startTime
  println(s"Time taken: $duration nanoseconds")
  
  bufferedSource.close() // Close the file reading
}

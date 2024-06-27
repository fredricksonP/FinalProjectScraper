import SequentialRealatorScraper.{browser, htmlListingElement}
import com.google.common.util.concurrent.AtomicDouble
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.math._

object booksToScrapeSequential extends App{
  //open the csv file of all links to books to scrape
  // Specify the path to your file
  var highestBookPrice = AtomicDouble(0.0) //The atomic double used for highest price

  implicit val ec: ExecutionContext = ExecutionContext.global
  val filePath = "src/main/scala/bookslinks.csv"

  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  def fetchPage(url: String): Future[Document] = Future {
    try {
      val newBrowser = JsoupBrowser()
      println(s"Fetching page: $url")
      val doc = newBrowser.get(url)
      val bookTitle = doc  >> text("h1")
//      val bookPrice = doc  >> text("price_color")
      val bookPrice = doc >> element(".price_color")
      val bookPriceParsed = bookPrice.text
      val bookPriceDouble = bookPriceParsed.drop(1).toDouble

//      if (bookPriceDouble > highestBookPrice.get()) {
//        print("yo")
//      }
//      highestBookPrice = max(book)
      println("Title: " + bookTitle)
      println("Price: " + bookPriceParsed)

      println(s"Fetched page successfully: $url")
//      doc
    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }



  // Get the start time
  val startTime = System.nanoTime()
  val fetchedPages = bufferedSource.getLines().map( x => fetchPage(x))
  val allPages = Future.sequence(fetchedPages)
  // Read each line

  // Get the end time
  Await.result(allPages, Duration.Inf)
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")
  
  bufferedSource.close() // Close the file reading
}

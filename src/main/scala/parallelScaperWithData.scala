import scala.concurrent.{Await, ExecutionContext, Future}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import scala.io.Source
import scala.concurrent.duration.Duration
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*


import scala.concurrent.{Await, ExecutionContext, Future}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import scala.io.Source
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import scala.io.Source
import scala.concurrent.duration.Duration

object parallelScaperWithData extends App {
  implicit val ec: ExecutionContext = ExecutionContext.global
  case class Book(title: String, price: Double, url: String)

  def fetchPage(url: String): Future[Book] = Future {
    try {
      val newBrowser = JsoupBrowser()
      println(s"Fetching page: $url") //Update console to show which page is being scraped
      val doc = newBrowser.get(url) //Establish jsoup connection

      val bookTitle = doc  >> text("h1")
      val bookPrice = doc >> element(".price_color")
      val bookPriceParsed = bookPrice.text
      val bookPriceDouble = bookPriceParsed.drop(1).toDouble

      val table = doc >> element("tbody") // Select the tbody element
      val rows = (table >> elements("tr") >> elements("td")).toVector // Select all rows in the tbody
      val rowIndex = 2 // For example, to get the third row (index starts at 0)

      Book(bookTitle, bookPriceDouble, url)

    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }

  val filePath = "src/main/scala/bookslinks.csv"
  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Get the start time
  val startTime = System.nanoTime()


  val urls = bufferedSource.getLines().toList
  val fetchFutures = urls.map(fetchPage)

  val allPagesFuture = Future.sequence(fetchFutures)

  val maxPriceFuture = allPagesFuture.map { books =>
    books.maxBy(_.price)
  }

  val maxPriceBook = Await.result(maxPriceFuture, Duration.Inf)

  println(s"The book with the highest price is: ${maxPriceBook.title} at $$${maxPriceBook.price}, URL: ${maxPriceBook.url}")

  // Wait for all scraping to complete
//  Await.result(scrapeFutures, Duration.Inf)

  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")

  bufferedSource.close()  // Close the file reading
}

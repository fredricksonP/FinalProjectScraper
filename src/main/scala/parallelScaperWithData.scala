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

  def fetchPage(url: String): Future[Document] = Future {
    try {
      val newBrowser = JsoupBrowser()
      println(s"Fetching page: $url")
      val doc = newBrowser.get(url)
      doc
    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }

  def scrapePage(doc: Document): Unit = {
    try {
      val bookTitle = doc >> text("h1")
      val bookPrice = doc >> element(".price_color")
      val bookPriceParsed = bookPrice.text
      val bookPriceDouble = bookPriceParsed.drop(1).toDouble

      // Update the highest price safely
      // highestPrice.set(math.max(highestPrice.get(), bookPriceDouble))
      println(s"Title: $bookTitle")
      println(s"Price: $$${bookPriceParsed}")

      val table = doc >> element("tbody") // Select the tbody element
      val rows = (table >> elements("tr") >> elements("td")).toVector // Select all rows in the tbody

      println("Row captured: " + rows.mkString(", "))

    } catch {
      case e: Exception =>
        println(s"Failed to scrape data from document. Error: ${e.getMessage}")
    }
  }

  val filePath = "src/main/scala/bookslinks.csv"
  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Get the start time
  val startTime = System.nanoTime()

  // Fetch pages in parallel
  val fetchedPages = bufferedSource.getLines().map(x => fetchPage(x)).toList
  val allPages = Future.sequence(fetchedPages)

  // Scrape pages in parallel
  val scrapeFutures: Future[Unit] = allPages.flatMap { documents =>
    // Map each Document to a Future[Unit] by applying scrapePage to each Document
    Future.sequence(documents.map(doc => Future(scrapePage(doc)))).map(_ => ())
  }

  // Wait for all scraping to complete
  Await.result(scrapeFutures, Duration.Inf)

  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")

  bufferedSource.close()  // Close the file reading
}

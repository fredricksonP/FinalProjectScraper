import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import org.jsoup.Jsoup

import scala.io.Source
import net.ruippeixotog.scalascraper.browser.JsoupBrowser


object booksSequentialOpenConn extends App{
  case class Product(upc: String, prod_type: String, image: String, price: String)

  var allPrices = Vector()
  //TODO: Calc average price
  //TODO: Keep track of availability
  //TODO: Make methods to get low stock, medium stock and high stock

  //File path containing the 1000 links to books
  val filePath = "src/main/scala/bookslinks.csv"

  //Make a new jsoup session
  val browser = JsoupBrowser()

  // Get a session to maintain cookies and keep the session open for all requests
//  val session = browser.newSession()

  def fetchPage(url: String) = {
    try {
      val newBrowser = JsoupBrowser()
      println(s"Fetching page: $url")
//      val doc = newBrowser.get(url)
      val doc = browser.get(url)

      val bookTitle = doc >> text("h1")
      val bookPrice = doc >> element(".price_color")
      val bookPriceParsed = bookPrice.text
      val bookPriceDouble = bookPriceParsed.drop(1)

      val table = doc >> element("tbody") // Select the tbody element
      val rows = (table >> elements("tr") >> elements("td")).toVector // Select all rows in the tbody
      val rowIndex = 2 // For example, to get the third row (index starts at 0)


      //      highestPrice.cmp(rows(2).toString.drop(1).toDouble)
      println(s"Adding price: ${bookPriceParsed}")
      //      pricesQueue.add(bookPriceParsed)
      println(s"Added price: ${bookPriceParsed}")


      println("Row captured: " + rows(0))
      println("Row captured: " + rows(1))
      println("Row captured: " + rows(2))
      println("Row captured: " + rows(3))
      println("Row captured: " + rows(4))
      println("Row captured: " + rows(5))
      println("Row captured: " + rows(6))


      println("Title: " + bookTitle)
      println("Price: " + bookPriceParsed)

    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }

  // Get the start time
  val startTime = System.nanoTime()

  val source = Source.fromFile(filePath)

  // Use a for loop to read each line from the file and call the scrape function
  for (line <- source.getLines()) {
    fetchPage(line)
  }

  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")


}

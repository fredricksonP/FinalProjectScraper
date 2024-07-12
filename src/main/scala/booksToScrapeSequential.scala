import MoreSequentialScraping.{browser, htmlListingElement}

import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source

//imports for making atomic data structures
import com.google.common.util.concurrent.AtomicDouble
import scalaz.Leibniz.subst
import java.util.concurrent.ConcurrentLinkedQueue

/*
  After giving up on scraping with a web driver, I came up with a new idea.
  What if there was a way to scrape multiple static pages instead of trying
  to scrape a lot of data from one page. With this idea, I found a scraping friendly site
  designed for learning and testing web scraping. It's called bookstoscrape.com. This is 
  an E-commerce web website that's like an online bookstore. I figured out that I could scrape all
  of the different book pages and obtain various book data. To scrape all sites, I exported
  all of the pages links to a csv file. Then, I read from the csv file and create connections to the 
  various pages for scraping book data such as book title, price, and stock. This version is sequential, 
  but the ultimate goal is to scrape all pages in parallel. 
 */

object booksToScrapeSequential extends App{
  case class Product(upc: String, prod_type: String, image: String, price: String)
  //open the csv file of all links to books to scrape
  // Specify the path to your file

//TODO: make some sort of thread safe list to keep track of all prices

  var allPrices = Vector()
//TODO: Calc average price
//TODO: Keep track of availability
//TODO: Make methods to get low stock, medium stock and high stock

  val filePath = "src/main/scala/bookslinks.csv"

  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Use ConcurrentLinkedQueue for thread-safe, growable list
  val pricesQueue = new ConcurrentLinkedQueue[String]()

  def fetchPage(url: String) = {
    try {
      val newBrowser = JsoupBrowser()
      println(s"Fetching page: $url")
      val doc = newBrowser.get(url)
      val bookTitle = doc  >> text("h1")
      val bookPrice = doc >> element(".price_color")
      val bookPriceParsed = bookPrice.text
      val bookPriceDouble = bookPriceParsed.drop(1)

      val table = doc >> element("tbody") // Select the tbody element
      val rows = (table >> elements("tr") >> elements("td")).toVector // Select all rows in the tbody
      val rowIndex = 2 // For example, to get the third row (index starts at 0)


//      highestPrice.cmp(rows(2).toString.drop(1).toDouble)
      println(s"Adding price: ${bookPriceParsed}")
//      pricesQueue.add(bookPriceParsed)
      //##### right here an update can't occur until the future is complete.
      allPrices :+ bookPriceParsed
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
  val lines = bufferedSource.getLines();
  val source = Source.fromFile(filePath)

  // Use a for loop to read each line and print it
  for (line <- source.getLines()) {
    fetchPage(line)
  }

//  val fetchedPages = bufferedSource.getLines().map( x => fetchPage(x))
//  val allPages = Future.sequence(fetchedPages)
  // Read each line

  // Get the end time
//  Await.result(allPages, Duration.Inf)
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")

  bufferedSource.close() // Close the file reading
}

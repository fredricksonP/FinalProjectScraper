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

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source

//imports for making atomic data structures
import com.google.common.util.concurrent.AtomicDouble
import scalaz.Leibniz.subst
import java.util.concurrent.ConcurrentLinkedQueue


object booksToScrapeSequential extends App{
  case class Product(upc: String, prod_type: String, image: String, price: String)
  //open the csv file of all links to books to scrape
  // Specify the path to your file
//  var highestBookPrice = AtomicDouble(0.0) //The atomic double used for highest price
//  class AtomicallyLockedVec {
//    private var setOfVisited: Vector[Double] = Vector()
//
//    def addElement(newElement: Double): Unit = this.synchronized {
//      setOfVisited = setOfVisited :+ newElement // Update the set with the new link
//    }
//
//    def get: Vector[Double] = this.synchronized {
//      setOfVisited
//    }

//TODO: Make some sort of atomically locked double to keep track of hifhest price
//Did some research on how to use an atomic double in scala and found this: https://guava.dev/releases/22.0/api/docs/com/google/common/util/concurrent/AtomicDouble.html

//  val mostExpensivePrice = new AtomicDouble(0.0)
//
//  def updateHighestPrice(newPrice: Double): Unit = {
//    mostExpensivePrice.updateAndGet(current => math.max(current, newPrice))
//  }

  class AtomicLockedDouble {
    private var maxPrice: Double = 0.0

    def get: Double = this.synchronized {
      maxPrice
    }

    def cmp(elementToCheck: Double): Unit = this.synchronized {
//      println("element to check: " + elementToCheck + " This: " + maxPrice)
      maxPrice = math.max(elementToCheck, this.maxPrice)
    }

  }

//TODO: make some sort of thread safe list to keep track of all prices

  var allPrices = Vector()
//TODO: Calc average price
//TODO: Keep track of availability
//TODO: Make methods to get low stock, medium stock and high stock

  implicit val ec: ExecutionContext = ExecutionContext.global
  val filePath = "src/main/scala/bookslinks.csv"

  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  var highestPrice = new AtomicLockedDouble

  // Use ConcurrentLinkedQueue for thread-safe, growable list
  val pricesQueue = new ConcurrentLinkedQueue[String]()

  def fetchPage(url: String): Future[Document] = Future {
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
      println(s"Adding price: $$${bookPriceParsed}")
//      pricesQueue.add(bookPriceParsed)
      //##### right here an update can't occur until the future is complete.
      allPrices :+ bookPriceParsed
      println(s"Added price: $$${bookPriceParsed}")


      println("Row captured: " + rows(0))
      println("Row captured: " + rows(1))
      println("Row captured: " + rows(2))
      println("Row captured: " + rows(3))
      println("Row captured: " + rows(4))
      println("Row captured: " + rows(5))
      println("Row captured: " + rows(6))



      //Keep track of all the prices in a concurrent linked queue
//      concurrentQueue.add(bookPriceDouble)

//      pricesVec.addElement(bookPriceParsed)

      println("Title: " + bookTitle)
      println("Price: " + bookPriceParsed)
//
//      println(s"Fetched page successfully: $url")
      doc
    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }

  def scrapePage(page: Future[Document]): Unit = {
    //Scrape data here
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

  import scala.jdk.CollectionConverters._

//  val iterator = concurrentQueue.iterator().asScala
//  iterator.foreach(item => println(s"Item: $item"))

  bufferedSource.close() // Close the file reading
}

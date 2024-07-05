import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

/* 
  This was the third file that I created. After learning how to do some basic sequential 
  web scraping, I wanted to see if I could use techniques like Future, Success, and Await. 
  This is parallelism on a small scale, but the point isn't to see speedup here, but rather 
  to prove that you can combine jsoup and web scraping with parallelism. I will hopefully build
  on these tactics to create a much larger scraper that actually showcases parallel speedup. 
 */

object attemptingParallelism extends App {
  val browser = JsoupBrowser()
  implicit val ec: ExecutionContext = ExecutionContext.global

  //function to turn page connection into a future
  def fetchPage(url: String): Future[Document] = Future {
    try {
      val newBrowser = JsoupBrowser()
      //Give some output to show that the page is being accessed. 
      println(s"Fetching page: $url")  
      val doc = newBrowser.get(url)
      println(s"Fetched page successfully: $url")
      doc
    } catch {
      case e: Exception =>
        println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
        throw e
    }
  }

  //
  def logDocument(doc: Document): Unit = {
    println("Document fetched successfully.")
  }

  def logError(url: String, exception: Throwable): Unit = {
    println(s"Failed to fetch page: $url. Error: ${exception.getMessage}")
  }

  //Connecting to two pages at one in parallel
  val menuFuture = fetchPage("https://frbbq.com/menu/")
  val beerListFuture = fetchPage("https://frbbq.com/beer-list/")

  //Scraping website 1
  val htmlListingElementFuture = menuFuture.map(menu => menu >> element("h1"))
  val nameFuture = htmlListingElementFuture.map(htmlListingElement => htmlListingElement >> text("h1"))
  nameFuture.onComplete {
    //On a successful complete print out the website title 
    case Success(doc) =>
      println(nameFuture)
    case Failure(_) => ???
  }

  //scraping website 2
  val beerListElementsFuture = beerListFuture.map(beerList => beerList >> elements("h5"))

  beerListElementsFuture.map(beerListElements => beerListElements.foreach { priceElement =>
    //Printing out all of the tables 
    val price = priceElement.text
    println(s"Drink info: $price")
  })

  //Wait for the futures result so that the data can be printed 
  Await.result(beerListElementsFuture, 30.seconds)
  Await.result(nameFuture, 30.seconds)

  //Establish another connection to wikipedia
  menuFuture.onComplete {
    case Success(doc) =>
      logDocument(doc)
    case Failure(exception) =>
      logError("https://en.wikipedia.org/wiki/Main_Page (menu)", exception)
  }

  
  beerListFuture.onComplete {
    case Success(doc) =>
      logDocument(doc)
    case Failure(exception) =>
      logError("https://en.wikipedia.org/wiki/Main_Page (beer list)", exception)
  }
}

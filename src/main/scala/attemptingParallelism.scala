import SequentialRealatorScraper.{beerList, beerListElements}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

object attemptingParallelism extends App {
  val browser = JsoupBrowser()
  implicit val ec: ExecutionContext = ExecutionContext.global

  //function to turn page connectoin into a future
  def fetchPage(url: String): Future[Document] = Future {
    try {
      val newBrowser = JsoupBrowser()
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

  def logDocument(doc: Document): Unit = {
    println("Document fetched successfully.")
  }

  def logError(url: String, exception: Throwable): Unit = {
    println(s"Failed to fetch page: $url. Error: ${exception.getMessage}")
  }

  val menuFuture = fetchPage("https://frbbq.com/menu/")
  val beerListFuture = fetchPage("https://frbbq.com/beer-list/")

  //Scraping website 1
  val htmlListingElementFuture = menuFuture.map(menu => menu >> element("h1"))
  val nameFuture = htmlListingElementFuture.map(htmlListingElement => htmlListingElement >> text("h1"))
  nameFuture.onComplete {
    case Success(doc) =>
      println(nameFuture)
    case Failure(_) => ???
  }

  //scraping website 2
  val beerListElementsFuture = beerListFuture.map(beerList => beerList >> elements("h5"))

  beerListElementsFuture.map(beerListElements => beerListElements.foreach { priceElement =>
    val price = priceElement.text
    println(s"Price: $price")
  })

  Await.result(beerListElementsFuture, 30.seconds)
  Await.result(nameFuture, 30.seconds)

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
  // Combining futures to ensure both pages are fetched before proceeding
//  val combinedFuture = for {
//    menuDoc <- menuFuture
//    beerListDoc <- beerListFuture
//  } yield (menuDoc, beerListDoc)

//  combinedFuture.onComplete {
//    case Success((menuDoc, beerListDoc)) =>
//      println("Both documents fetched successfully.")
//    case Failure(exception) =>
//      println(s"Failed to fetch one or both documents: ${exception.getMessage}")
//  }

//  // Increase the timeout duration to 60 seconds for testing
//  try {
//    Await.result(combinedFuture, 60.seconds)
//    println("Processing completed within timeout.")
//  } catch {
//    case e: Exception =>
//      println(s"Processing timed out. Error: ${e.getMessage}")
//  }
//}

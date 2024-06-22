import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object AttemptingParallelism extends App {
  val browser = JsoupBrowser()
  implicit val ec: ExecutionContext = ExecutionContext.global

  def fetchPage(url: String): Future[Document] = Future {
    println(s"Fetching page: $url")
    browser.get(url)
  }

  def logDocument(doc: Document): Unit = {
    println("Document fetched successfully.")
  }

  val menuFuture = fetchPage("https://frbbq.com/menu/")
  val beerListFuture = fetchPage("https://frbbq.com/beer-list/")

  menuFuture.onComplete {
    case Success(doc) =>
      logDocument(doc)
    case Failure(exception) =>
      println(s"Failed to fetch menu page: ${exception.getMessage}")
  }

  beerListFuture.onComplete {
    case Success(doc) =>
      logDocument(doc)
    case Failure(exception) =>
      println(s"Failed to fetch beer list page: ${exception.getMessage}")
  }

  // Combining futures to ensure both pages are fetched before proceeding
  val combinedFuture = for {
    menuDoc <- menuFuture
    beerListDoc <- beerListFuture
  } yield (menuDoc, beerListDoc)

  combinedFuture.onComplete {
    case Success((menuDoc, beerListDoc)) =>
      println("Both documents fetched successfully.")
    case Failure(exception) =>
      println(s"Failed to fetch one or both documents: ${exception.getMessage}")
  }

  // Increase the timeout duration to 60 seconds for testing
  try {
    Await.result(combinedFuture, 60.seconds)
    println("Processing completed within timeout.")
  } catch {
    case e =>
      println("Processing timed out.")
  }
}

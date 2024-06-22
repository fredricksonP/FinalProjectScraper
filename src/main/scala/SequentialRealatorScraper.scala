import SequentialScraper.doc
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
//import scala.collection.JavaConversions._

object SequentialRealatorScraper extends App{
  val browser = JsoupBrowser()
//  implicit val ec: ExecutionContext = ExecutionContext.global
  //Looking at ways to parallelize my code:
//  def fetchPage(url: String): Future[Document] = Future {
//    browser.get(url)
//  }

//  val doc = browser.get("https://www.lazada.co.th/shop-mobiles/")
  val docFuture = browser.get("https://frbbq.com/menu/")

  // Use proper CSS selector to find the element
  val htmlListingElement =  doc >> element("h1")

  val name = htmlListingElement >> text("h1")
  println(name)

  val beerList = browser.get("https://frbbq.com/beer-list/")
//  val beerListElement = doc >> select("div.fusion-column-wrapper")
  val beerListElements = beerList >> elements("span.fusion-toggle-heading")
//  println(beerListElements)
//  val name= beerElement >> text("span.fusion-toggle-heading")

//  beerListElements.foreach {
//    beerElement =>
//      val name = beerElement >> text("span.fusion-toggle-heading")
////      val price = beerElement >> text("fusion-responsive-typography-calculated")
//      println(name + " " )
//  }

//  val priceElements = beerList >> elements("h5.fusion-responsive-typography-calculated")

  val priceElements = beerList >> elements("h5")
  priceElements.foreach { priceElement =>
    val price = priceElement.text
    println(s"Price: $price")
  }


//
//  import java.io.IOException;
//  try {
//    // Fetch the HTML content of the page
//    // Select the <h5> element with the specific class
//    val elements = beerList.select("h5.fusion-responsive-typography-calculated")
//    // Find the element and extract the price
//
//    elements.foreach { element =>
//      System.out.println("Price: " + element)
//    }
//  } catch {
//    case _ =>
//      println("error")
////      e.printStackTrace
//  }
//  val drinkName = beerListElement >> elements("span.fusion-toggle-heading")
//  println("\n\n\n" + drinkName)
  // Iterate over each element and extract the drink name
//  beerListElement.foreach { beerElement =>
//    // Extract text from elements with class 'fusion-toggle-heading' within each beer element
//    val drinkName = (beerElement >> elementList("div.fusion-toggle-heading")).map(_.text)
//    drinkName.foreach(println)
//  }
//  println(drinkName)
//  val title = htmlListingElement >> text(".RfADt a")
//  val price = htmlListingElement >> text(".aBrP0 .ooOxS")
//
//  println(s"Title: $title")
//  println(s"Price: $price")
//  println("-------------------------")

  // Print the HTML of the selected element
//  println(name)

  // Now you can extract specific data from htmlListingElement using DSL methods
  // For example:
  // val price = htmlListingElement >> text(".card-price")
  // println(price)

}
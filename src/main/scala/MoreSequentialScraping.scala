import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

/*
  This was the second file I created in my scraping journey.
  The goal here was to see if I could scrape target information from
  a web page on my own. Last time I was following a tutorial on a designated
  scraping web page. Now I wanted to scrape on my own. I ended up scraping
  wikipedia and the website of my first job. When scraping the website of my first job
  (a barbeque restraunt) I scraped some of the drink items off of their website.
 */

import scala.concurrent.{Await, ExecutionContext, Future}

object MoreSequentialScraping extends App{
  val browser = JsoupBrowser()
//  implicit val ec: ExecutionContext = ExecutionContext.global
  //Looking at ways to parallelize my code:
//  def fetchPage(url: String): Future[Document] = Future {
//    browser.get(url)
//  }

//  val doc = browser.get("https://www.lazada.co.th/shop-mobiles/")
//  val docFuture = browser.get("https://frbbq.com/menu/")
  val doc = browser.get("https://en.wikipedia.org/wiki/Main_Page")

  // Grabbing the header from wikipedia
  val htmlListingElement =  doc >> element("h1")
  println(htmlListingElement) //Printing the whole element returned

  val name = htmlListingElement >> text("h1")
  println(name) //Now just parsing the h1 text from the tittle

  val beerList = browser.get("https://frbbq.com/beer-list/")

  //Learning how to extract specific elements here aside from your everyday html elements
  // val beerListElement = doc >> select("div.fusion-column-wrapper")
  //  val name= beerElement >> text("span.fusion-toggle-heading")
  val beerListElements = beerList >> elements("span.fusion-toggle-heading")


  //Trying to get all of the elements
//  beerListElements.foreach {
//    beerElement =>
//      val name = beerElement >> text("span.fusion-toggle-heading")
//      val price = beerElement >> text("fusion-responsive-typography-calculated")
//      println(name + " " )
//  }
//  val priceElements = beerList >> elements("h5.fusion-responsive-typography-calculated")

  //Finally got all of the elements printing out.
  var counter = 1
  val priceElements = beerList >> elements("h5")
  
  //Go over all of the elements to scrape and print target data
  priceElements.foreach { priceElement =>
    val tableElem = priceElement.text //Extract info from the table
    
    //Format the output based on the element extracted
    if counter == 1 then {println(s"Drink: $tableElem")}
    else if counter % 4 == 0 then {
      println(s"Drink: $tableElem")
    } else if(counter % 2 == 0)
      println(s"Size: $tableElem")
    else{
      println(s"Price: $tableElem")
    }
    counter += 1
  }
}
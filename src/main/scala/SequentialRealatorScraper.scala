import SequentialScraper.doc
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*

object SequentialRealatorScraper extends App{
  val browser = JsoupBrowser()
  val doc = browser.get("https://www.lazada.co.th/shop-mobiles/")

  // Use proper CSS selector to find the element
  val htmlListingElement = doc >> element("Bm3ON")

//  val name = htmlListingElement >> text("span")

  val title = htmlListingElement >> text(".RfADt a")
  val price = htmlListingElement >> text(".aBrP0 .ooOxS")

  println(s"Title: $title")
  println(s"Price: $price")
  println("-------------------------")

  // Print the HTML of the selected element
//  println(name)

  // Now you can extract specific data from htmlListingElement using DSL methods
  // For example:
  // val price = htmlListingElement >> text(".card-price")
  // println(price)

}
import SequentialScraper.{doc, htmlProductElements}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import net.ruippeixotog.scalascraper.model.Document

object LargeScraper extends App{
  //First connect to a realty website
  //TODO: Find a website for analysis
//  https://www.realtor.com/realestateandhomes-search/Colorado-Springs_CO
  val browser = JsoupBrowser()
//  val doc = browser.get("https://www.thailand-property.com/properties-for-sale/nakhon-pathom/phutthamonthon/salaya?exact_bed=false")
  val doc = browser.get("https://www.fivestars-thailand.com/en/sale/bangkok")
  //TODO: Test a new web connection with jsoup
  val htmlHouseElements = doc >> elementList("div.property-block")
  println(htmlHouseElements)
  println("There are " + htmlHouseElements.length + " in in the htmlHouseElements")
  //Next, pull elements
  //TODO: pull elements and print the HTML to make sure proper html is parsed
  //Process elements
  //TODO: Process html to only elements I want for my stats
  //print some elements in a for loop first
  //TODO: Start by printing a subset of the data
  //Then retrieve a large number (all) of the elements with the for loop
  //TODO: Maintain a connection an scrape a large amount of data from a site

}

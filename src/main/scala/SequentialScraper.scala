import org.jsoup.*
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

/*
  This was the first file I made for this final project. The goal of this
  file was to learn how to web scrape using jsoup and scala. In FunPar Assignment 3,
  we learned how to establish connections to pages with jsoup and how to obtain some basic
  information from a web page. However, I needed to learn how to scrape target data from a webpage.
  To learn how to do this, I followed a helpful web scraping tutorial which taught me how to scrape
  product information from a fake E-commerce page that's designed for scraping. This was an iterative
  process where first I learned how to scrape one product from the page, and then I learned how to scrape
  all of the products from the E-commerce page. This wast the scraping tutorial I followed.
  https://www.zenrows.com/blog/scala-web-scraping#convert-data-to-csv
 */


object SequentialScraper extends App {
  //Defining a structure to hold the scraped data
  case class Product(name: String, url: String, image: String, price: String)

  //Connect to the E-commerce page
  val browser = JsoupBrowser()
  val doc = browser.get("https://www.scrapingcourse.com/ecommerce/")
  val html = doc.toHtml

  //**** Learned how to extract a single product ****
  // get the first HTML product on the page
//  val htmlProductElement = doc >> element("li.product")
//
//  // extract the desired data from it
//  val name = htmlProductElement >> text("h2")
//  val url = htmlProductElement >> element("a") >> attr("href")
//  val image = htmlProductElement >> element("img") >> attr("src")
//  val price = htmlProductElement >> text("span")
//  val firstProduct = (name, url, image, price)
  //  println(name + " " + url + " " + image + " " + price)
  //  println(firstProduct)
  //**** Learned how to extract a single product ****

  //Extracting all of the product on a page
  val htmlProductElements = doc >> elementList("li.product")

  val products: List[Product] = htmlProductElements.map(htmlProductElement => {
    // extract the desired data from it
    val name = htmlProductElement >> text("h2")
    val url = htmlProductElement >> element("a") >> attr("href")
    val image = htmlProductElement >> element("img") >> attr("src")
    val price = htmlProductElement >> text("span")

    // return a new Product instance
    Product(name, url, image, price)
  })

  //recursive fxn to print all products
  def print_products(prods: List[Product]): Unit = prods match {
    case Nil => println()
    case h :: t => {
      println(h.name)
      println(h.url)
      println(h.image)
      println(h.price)
      println("___________")
      print_products(t)
    }
  }
  print_products(products)


}

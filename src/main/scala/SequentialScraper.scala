import org.jsoup.*
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._



object SequentialScraper extends App {
  //Defining a structure to hold the scraped data
  case class Product(name: String, url: String, image: String, price: String)

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

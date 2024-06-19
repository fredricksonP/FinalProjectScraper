import org.jsoup.*
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._



object SequentialScraper {

  case class Product(name: String, url: String, image: String, price: String)

  val browser = JsoupBrowser()
  val doc = browser.get("https://www.scrapingcourse.com/ecommerce/")
  val html = doc.toHtml

  // get the first HTML product on the page
  val htmlProductElement = doc >> element("li.product")

  // extract the desired data from it
  val name = htmlProductElement >> text("h2")
  val url = htmlProductElement >> element("a") >> attr("href")
  val image = htmlProductElement >> element("img") >> attr("src")
  val price = htmlProductElement >> text("span")


  println(name + " " + url + " " + image + " " + price)
}

@main
def main(): Unit = {
  println("hello world")
  val scraper = SequentialScraper
}

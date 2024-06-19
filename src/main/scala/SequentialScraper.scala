import org.jsoup.*
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._



object SequentialScraper {
  val browser = JsoupBrowser()
  val doc = browser.get("https://www.scrapingcourse.com/ecommerce/")
  val html = doc.toHtml

  println(html)
}

@main
def main(): Unit = {
  println("hello world")
  val scraper = SequentialScraper
  println(scraper)
}

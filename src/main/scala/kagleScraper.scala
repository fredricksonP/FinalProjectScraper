import SequentialRealatorScraper.browser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser

object kagleScraper extends App{

  //establsih jsoup browser
  val browser = JsoupBrowser()
  //Connect to kaggle site
  val doc = browser.get("https://en.wikipedia.org/wiki/Main_Page")
}

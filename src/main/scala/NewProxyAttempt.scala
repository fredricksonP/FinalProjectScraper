import scala.io.Source
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

object NewProxyAttempt {
  System.setProperty("http.proxyHost", "192.168.5.1");
  System.setProperty("http.proxyPort", "1080");
  Document doc = Jsoup.connect("www.google.com").get();
}

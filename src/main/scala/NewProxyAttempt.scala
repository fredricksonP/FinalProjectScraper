import org.jsoup.Jsoup
import org.jsoup.nodes.{Document => JsoupDocument}
import java.net.{InetSocketAddress, Proxy}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.{Document => ScraperDocument}


object NewProxyAttempt extends App {

  //Setting the Proxy I want to use.
  val proxyHost = "85.209.153.174"
  val proxyPort = 999

  // Create a proxy instance
  val proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))

  val url = "http://books.toscrape.com/catalogue/category/books_1/index.html"

//  val url = "http://books.toscrape.com/catalogue/a-light-in-the-attic_1000/index.html"

  //Try catch block to catch if the connection or the proxy fails.
  try {
    val response = Jsoup.connect(url)
      .proxy(proxy)
      .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
      .execute()
//    val response = JsoupBrowser()

    val statusCode = response.statusCode()
    val body = response.body()
    val parsed = response.parse()
    println(s"Status Code: $statusCode")
    println(s"Response Body: $parsed")

    if (statusCode == 403 || body.contains("blocked") || body.contains("captcha")) {
      println("The site might be blocking requests from proxies.")
    } else {
      println("The site is accessible through the proxy.")
    }
  } catch {
    case e: Exception => println(s"Error: ${e.getMessage}")
  }
  
  
}

  // Connect to the URL using Jsoup and fetch the document with the proxy
//  val jsoupDoc: JsoupDocument = Jsoup.connect(url)
//    .proxy(proxy)
//    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
//    .get()
//
//  // Convert Jsoup Document to scala-scraper Document
//  val browser = JsoupBrowser()
//  val scraperDoc: ScraperDocument = browser.parseString(jsoupDoc.html())
//
//  // Print the document title to verify the connection
//  println(scraperDoc)

  // Connect to the URL using Jsoup and fetch the document
//  val doc: Document = Jsoup.connect("http://www.google.com").get()
//  val doc: Nothing = Jsoup.connect("http:// example. com").userAgent("Mozilla").data("name", "jsoup").get

//  val url = "http://example.com";
//  val doc = Jsoup.connect(url)
//    .proxy("http.proxyHost", "192.168.5.1")
//    .get();

  //  val connection = Jsoup.connect(url).userAgent("Bot")



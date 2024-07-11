//import NewProxyAttempt.doc
//
//import scala.io.Source
//import net.ruippeixotog.scalascraper.browser.JsoupBrowser
//import net.ruippeixotog.scalascraper.model.Document
//import org.jsoup.Jsoup
//import java.net.{InetSocketAddress, Proxy}

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.{InetSocketAddress, Proxy}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document


object NewProxyAttempt {
  System.setProperty("http.proxyHost", "192.168.5.1")
  System.setProperty("http.proxyPort", "1080")

  val proxyHost = "192.168.5.1"
  val proxyPort = 1080

  // Create a proxy instance
  val proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))

  val url = "http://example.com"

  // Connect to the URL using Jsoup and fetch the document with the proxy
  val jsoupDoc: Document = Jsoup.connect(url)
    .proxy(proxy)
    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
    .get()

  // Convert Jsoup Document to scala-scraper Document
  val browser = JsoupBrowser()
  val scraperDoc: Document = browser.parseInput(jsoupDoc.html(), url)

  // Print the document title to verify the connection
  println(scraperDoc.title)

  // Connect to the URL using Jsoup and fetch the document
//  val doc: Document = Jsoup.connect("http://www.google.com").get()
//  val doc: Nothing = Jsoup.connect("http:// example. com").userAgent("Mozilla").data("name", "jsoup").get

//  val url = "http://example.com";
//  val doc = Jsoup.connect(url)
//    .proxy("http.proxyHost", "192.168.5.1")
//    .get();
  
  //  val connection = Jsoup.connect(url).userAgent("Bot")


}

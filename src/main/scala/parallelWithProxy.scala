import scala.io.Source
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

object parallelWithProxy extends App{
  //Following this Proxy tutorial for scala
  //https://proxiesapi.com/articles/scraping-without-headaches-using-scala-and-scalaj-http-with-proxy-servers
  val browser = JsoupBrowser()
  System.setProperty("http.proxyHost", "proxy.example.com")
  System.setProperty("http.proxyPort", "8080")

  val doc: Document = browser.get("http://example.com")
  val title: String = doc.title

  System.clearProperty("http.proxyHost")
  System.clearProperty("http.proxyPort")


  val filePath = "src/main/scala/bookslinks.csv"
  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Get the start time
  val startTime = System.nanoTime()


  val urls = bufferedSource.getLines().toList
//  val fetchFutures = urls.map(fetchPage)

//  val allPagesFuture = Future.sequence(fetchFutures)


  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")

  bufferedSource.close() // Close the file reading
}

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import java.io.{File, PrintWriter}
import org.jsoup.Jsoup
import org.jsoup.Connection
import scala.util.{Failure, Success}

object ProxyChecker extends App {
  val validProxies = new java.util.concurrent.CopyOnWriteArrayList[String]()
  val apiUrl = "https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&proxy_format=ipport&format=text"
  val workingProxiesFile = "working_proxies.txt"
  val potentialProxiesFile = "potential_proxies.txt"

  def checkProxies(proxy: String): Unit = {
    println(s"Checking $proxy...")
    try {
      val response = Jsoup.connect("http://books.toscrape.com/catalogue/category/books_1/index.html")
        .proxy(proxy.split(":")(0), proxy.split(":")(1).toInt)
        .timeout(3000)
        .execute()

      if (response.statusCode == 200) {
        validProxies.add(proxy)
        val pw = new PrintWriter(new java.io.FileOutputStream(new File(workingProxiesFile), true))
        pw.println(proxy)
        pw.close()
        println(s"SUCCESS: $proxy")
      } else {
        println(s"$proxy Failed with status ${response.statusCode}")
      }
    } catch {
      case e: Exception => println(s"$proxy Failed with exception ${e.getMessage}")
    }
  }

  def fetchProxies(apiUrl: String): Option[String] = {
    try {
      val response = Jsoup.connect(apiUrl).ignoreContentType(true).execute()
      if (response.statusCode == 200) Some(response.body) else None
    } catch {
      case e: Exception => println(s"Failed to fetch proxies: ${e.getMessage}"); None
    }
  }

  fetchProxies(apiUrl).foreach { fileText =>
    val pw = new PrintWriter(potentialProxiesFile)
    pw.write(fileText)
    pw.close()
  }

  val proxies = Source.fromFile("potential_proxies.txt").getLines().toList
  val futures = proxies.map(proxy => Future { checkProxies(proxy) })

  val aggregated = Future.sequence(futures)
  aggregated.onComplete {
    case Success(_) => println("All proxies checked.")
    case Failure(e) => println(s"An error occurred: ${e.getMessage}")
  }

  Await.result(aggregated, Duration.Inf)
}

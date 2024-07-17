import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import java.io.{File, PrintWriter}
import org.jsoup.Jsoup
import scala.util.{Failure, Success}

object ProxyChecker extends App {
  val validProxies = new java.util.concurrent.CopyOnWriteArrayList[String]()
  val apiUrl = "https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&proxy_format=ipport&format=text"
  val workingProxiesFile = "working_proxies.txt"
  val potentialProxiesFile = "potential_proxies.txt"

  def checkProxies(proxy: String): Unit = {
    println(s"Checking $proxy")
    val proxyHost = proxy.split(":")(0)
    val proxyPort = proxy.split(":")(1).toInt
    val timeout = 3.seconds  // Manual timeout for the entire proxy check process

    // Create a Future to handle the proxy check
    val proxyCheckFuture = Future {
      val response = Jsoup.connect("http://books.toscrape.com/catalogue/category/books_1/index.html")
        .proxy(proxyHost, proxyPort)
        .timeout(3000)  // Timeout for the connection itself
        .execute()

      response.statusCode() match {
        case 200 =>
          println(s"SUCCESS: $proxy")
          validProxies.add(proxy)
          val pw = new PrintWriter(new java.io.FileOutputStream(new File(workingProxiesFile), true))
          pw.println(proxy)
          pw.close()
          println(s"SUCCESS: $proxy")
        case _ => println(s"$proxy Failed with status ${response.statusCode()}")
      }
    }

    // Apply a timeout to the Future
    try {
      Await.ready(proxyCheckFuture, timeout).value.get match {
        case Success(_) => // Proxy check succeeded
        case Failure(e) => println(s"$proxy Failed with exception ${e.getMessage}")
      }
    } catch {
      case e: TimeoutException => println(s"$proxy Timeout after $timeout")
    }
  }

  def fetchProxies(apiUrl: String): Option[String] = {
    try {
      val response = Jsoup.connect(apiUrl).ignoreContentType(true).execute()
      if (response.statusCode() == 200) Some(response.body()) else None
    } catch {
      case e: Exception => println(s"Failed to fetch proxies: ${e.getMessage}"); None
    }
  }

  // Get the latest proxies from proxy scrape and write them to a file
  fetchProxies(apiUrl).foreach { fileText =>
    val pw = new PrintWriter(potentialProxiesFile)
    pw.write(fileText)
    pw.close()
  }

  val proxies = Source.fromFile(potentialProxiesFile).getLines().toList
  val futures = proxies.map(proxy => Future { checkProxies(proxy) })

  val aggregated = Future.sequence(futures)
  aggregated.onComplete {
    case Success(_) => println("All proxies checked.")
    case Failure(e) => println(s"An error occurred: ${e.getMessage}")
  }

  Await.result(aggregated, Duration.Inf)
}

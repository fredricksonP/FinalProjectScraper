import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*

import scala.io.Source

/*
  After giving up on scraping with a web driver, I came up with a new idea.
  What if there was a way to scrape multiple static pages instead of trying
  to scrape a lot of data from one page. With this idea, I found a scraping friendly site
  designed for learning and testing web scraping. It's called books.toscrape.com. This is
  an E-commerce web website that's like an online bookstore. I figured out that I could scrape all
  of the different book pages and obtain various book data. To scrape all sites, I exported
  all of the pages links to a csv file. Then, I read from the csv file and create connections to the 
  various pages for scraping book data such as book title, price, and stock. This version is sequential, 
  but the ultimate goal is to scrape all pages in parallel. 
 */

object booksToScrapeSequential{
  case class Product(upc: String, prod_type: String, image: String, price: String)

  var allPrices: Vector[Double] = Vector()
  var allTitles: Vector[String] = Vector()

  //File path containing the 1000 links to books
  val filePath = "src/main/scala/bookslinks.csv"

  def runLogic(): Unit = {


    def fetchPage(url: String): Unit = {
      try {
        val newBrowser = JsoupBrowser()
        println(s"Fetching page: $url")
        val doc = newBrowser.get(url)
        val bookTitle = doc >> text("h1")
        val bookPrice = doc >> element(".price_color")
        val bookPriceParsed = bookPrice.text
        val bookPriceDouble = bookPriceParsed.drop(1)

        val table = doc >> element("tbody") // Select the tbody element
        val rows = (table >> elements("tr") >> elements("td")).toVector // Select all rows in the tbody
        val rowIndex = 2 // For example, to get the third row (index starts at 0)


        //      highestPrice.cmp(rows(2).toString.drop(1).toDouble)
        println(s"Adding price: ${bookPriceParsed}")

        //      pricesQueue.add(bookPriceParsed)
        val priceDouble = bookPriceParsed.drop(1).toDouble
        //Add data to respective data structures
        allPrices :+ priceDouble
        allTitles :+ bookTitle


        //Used for seeing what rows contain what data
        //      println("Row captured: " + rows(0))
        //      println("Row captured: " + rows(1))
        //      println("Row captured: " + rows(2))
        //      println("Row captured: " + rows(3))
        //      println("Row captured: " + rows(4))
        //      println("Row captured: " + rows(5))
        //      println("Row captured: " + rows(6))


        println("Title: " + bookTitle)
        println("Price: " + bookPriceParsed)

      } catch {
        case e: Exception =>
          println(s"Failed to fetch page: $url. Error: ${e.getMessage}")
          throw e
      }
    }

    // Get the start time
    val startTime = System.nanoTime()

    val source = Source.fromFile(filePath)

    // Use a for loop to read each line from the file and call the scrape function
    for (line <- source.getLines()) {
      fetchPage(line)
    }
    //  Display results
    println(s"Titles Vec: $allTitles")
    println(s"BookPrice Vec: $allPrices")


    // Get the end time
    val endTime = System.nanoTime()

    // Calculate the duration
    val durationInSeconds = (endTime - startTime) / 1e9
    println(s"Time taken: $durationInSeconds seconds")
  }

  def main(args: Array[String]): Unit = {
    runLogic()
  }
}

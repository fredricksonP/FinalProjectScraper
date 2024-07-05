import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Document
import scala.jdk.CollectionConverters._

/* 
  This was my first attempt at obtaining large amounts of data. Now that I'm able to scrape pages
  and I've proven that parallel techniques can be used with scraping, I was on the hunt got lots 
  of data to scrape and process. However, I ran into problems. Finding static web pages with 
  tons of data to scrape isn't really a thing. Nearly all (if not all) websites that have large amounts
  of data, are dynamic webpages meaning that more data is loaded when it's demanded through a 
  botton click or a scroll. What I found is that this goes beyond the capabilities of JSoup. So 
  I got to work researching and found that you need a web driver in order to write code that clicks 
  buttons for you. With more research, I came across selenium with chrome web driver. I spent 
  considerable amounts of time downloading and trying to hook all of these softwares up. However, as 
  you can see, my selenium web driver gets blocked each time this file is run. Ultimately, after talking 
  with professor Kanat, it was decided that this wasn't a great rroute to be going, and that the web driver may be 
  beyond the scope of this project so I stopped pursuing it.  
 */

object LargeScraper extends App{
  // Set the path to the ChromeDriver executable
  System.setProperty("webdriver.chrome.driver", "src/main/scala/chromedriver-mac-arm64/chromedriver")

  // Initialize the ChromeDriver
  val options = new ChromeOptions()
  options.addArguments("--headless")
  options.addArguments("--allowed-ips")
  val driver: WebDriver = new ChromeDriver()

  //Trying a number of different websites with the webdriver to see if I can 
  //Get any to connect. 
  //Go to this website:
//  https://www.kaggle.com/datasets/nelgiriyewithana/most-streamed-spotify-songs-2024

  try {
    // Navigate to the desired URL
//    driver.get("https://www.fivestars-thailand.com/en/sale/bangkok")
    driver.get("https://the-internet.herokuapp.com/")
//    driver.get("https://data.lacity.org/Public-Safety/Crime-Data-from-2020-to-Present/2nrs-mtv8/data_preview")

    // Click the button to view more listings
    val button = driver.findElement(By.id("show-more"))
    button.click()
  }
  finally
  {
    // Close the browser
    driver.quit()
  }
    //First connect to a realty website
  //TODO: Find a website for analysis
//  https://www.realtor.com/realestateandhomes-search/Colorado-Springs_CO
  val browser = JsoupBrowser()
//  val doc = browser.get("https://www.thailand-property.com/properties-for-sale/nakhon-pathom/phutthamonthon/salaya?exact_bed=false")
  val doc = browser.get("https://www.fivestars-thailand.com/en/sale/bangkok")
  //TODO: Test a new web connection with jsoup
  val htmlHouseElements = doc >> elementList("div.property-block")
  println(htmlHouseElements)
  println("There are " + htmlHouseElements.length + " in in the htmlHouseElements")
  //Next, pull elements
  //TODO: pull elements and print the HTML to make sure proper html is parsed
  //Process elements
  //TODO: Process html to only elements I want for my stats
  //print some elements in a for loop first
  //TODO: Start by printing a subset of the data
  //Then retrieve a large number (all) of the elements with the for loop
  //TODO: Maintain a connection an scrape a large amount of data from a site

}

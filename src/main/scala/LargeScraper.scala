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

object LargeScraper extends App{
  // Set the path to the ChromeDriver executable
  System.setProperty("webdriver.chrome.driver", "src/main/scala/chromedriver-mac-arm64/chromedriver")

  // Initialize the ChromeDriver
  val options = new ChromeOptions()
  options.addArguments("--headless")
  val driver: WebDriver = new ChromeDriver()

  try {
    // Navigate to the desired URL
//    driver.get("https://www.fivestars-thailand.com/en/sale/bangkok")
    driver.get("https://the-internet.herokuapp.com/")
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

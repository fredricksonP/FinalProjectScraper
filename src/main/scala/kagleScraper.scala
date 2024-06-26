////import net.ruippeixotog.scalascraper.browser.JsoupBrowser
////import net.ruippeixotog.scalascraper.dsl.DSL.*
////import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
////import net.ruippeixotog.scalascraper.dsl.DSL.*
////import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
////import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
////import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
////import net.ruippeixotog.scalascraper.model.Document
////
////object kagleScraper extends App{
////
////  //establsih jsoup browser
////  val browser = JsoupBrowser()
////  //Connect to kaggle site
////  val doc = browser.get("https://www.kaggle.com/datasets/nelgiriyewithana/most-streamed-spotify-songs-2024")
////  val artistCol = doc >> elements("div.sc-dnxBXz-sc-diOBcD-jzMyzS-glFPwH")
////  println(artistCol)
////  //<div class="sc-iPYwOE sc-ejHknZ fnShgv eexbBZ"><div class="sc-eUJXWJ bmzQkT"><span class="sc-cyZbeP sc-cXrtoi bLfAFT bnzWJw">Taylor Swift</span><span class="sc-cyZbeP sc-iUFSyr bLfAFT kGmLjN">1%</span></div><div class="sc-eUJXWJ bmzQkT"><span class="sc-cyZbeP sc-cXrtoi bLfAFT bnzWJw">Drake</span><span class="sc-cyZbeP sc-iUFSyr bLfAFT kGmLjN">1%</span></div><div class="sc-eUJXWJ bmzQkT"><span class="sc-cyZbeP sc-cXrtoi bLfAFT hLNsXz">Other (4474)</span><span class="sc-cyZbeP sc-iUFSyr bLfAFT hWdaNO">97%</span></div></div>
////
////
////}
//
//
//import org.apache.spark.sql.SparkSession
//
//object KaggleDatasetProcessor extends App {
//  // Initialize Spark session
//  val spark = SparkSession.builder
//    .appName("Kaggle Dataset Processor")
//    .config("spark.master", "local")
//    .getOrCreate()
//
//  // Path to the downloaded CSV file
//  val datasetPath = "path/to/most-streamed-spotify-songs-2024.csv"
//
//  // Read the CSV file
//  val df = spark.read.option("header", "true").csv(datasetPath)
//
//  // Show the first few rows of the dataset
//  df.show()
//
//  // Extracting artists column
//  val artists = df.select("artist")
//
//  // Show artists
//  artists.show()
//
//  // Perform any additional processing as needed
//}

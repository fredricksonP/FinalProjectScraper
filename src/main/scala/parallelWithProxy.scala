import scala.io.Source


object parallelWithProxy extends App{
  

  val filePath = "src/main/scala/bookslinks.csv"
  // Open the file
  val bufferedSource = Source.fromFile(filePath)

  // Get the start time
  val startTime = System.nanoTime()


  val urls = bufferedSource.getLines().toList
  val fetchFutures = urls.map(fetchPage)

  val allPagesFuture = Future.sequence(fetchFutures)

  val maxPriceFuture = allPagesFuture.map { books =>
    books.maxBy(_.price)
  }

  // Calculate max price and total price on complete
  allPagesFuture.onComplete {
    case Success(books) =>
      val maxPriceBook = books.maxBy(_.price)
      val totalPrice = books.map(_.price).sum
      val avgPrice = totalPrice / books.length
      val totalStock = books.map(_.stock).sum
      val avgStock = totalStock / books.length

      println(s"The book with the highest price is: ${maxPriceBook.title} at $$${maxPriceBook.price}, URL: ${maxPriceBook.url}")
      println(s"The average price of all books is: ${avgPrice}")
      println(s"The average stock for books is: ${avgStock}")

    case Failure(ex) =>
      println(s"Failed to fetch and process pages. Error: ${ex.getMessage}")
  }

  val maxPriceBook = Await.result(maxPriceFuture, Duration.Inf)

  // Wait for all scraping to complete
  //  Await.result(scrapeFutures, Duration.Inf)

  // Get the end time
  val endTime = System.nanoTime()

  // Calculate the duration
  val durationInSeconds = (endTime - startTime) / 1e9
  println(s"Time taken: $durationInSeconds seconds")

  bufferedSource.close() // Close the file reading
}

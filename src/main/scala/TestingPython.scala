import scala.sys.process._

object TestingPython extends App {
    // Path to the Python script
//    val pythonScriptPath = "PythonScraper/realtyAutoScraper.py"

//  val pythonVenvPath = "PythonScraper/venv" // Update with the absolute path to your virtual environment's Python interpreter
  val pythonVenvPath = "PythonScraper/venv/bin/python" // Update with the correct path
  val scriptPath = "PythonScraper/realtyAutoScraper.py" // Update with the absolute path to your Python script
  val command = s"$pythonVenvPath $scriptPath"

    // Command to run the Python script
//    val command = s"python3 $pythonScriptPath"

    // Execute the command
    val exitCode = command.!

    // Check the exit code
    if (exitCode == 0) {
      println("Python script executed successfully.")
    } else {
      println(s"Python script execution failed with exit code $exitCode.")
    }
}

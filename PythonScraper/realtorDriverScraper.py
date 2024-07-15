from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
import time


#Specify our expiremental options to connect to sites
my_options = Options()
my_options.add_experimental_option("detach", True)

#Connect to target site with the chrome driver
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=my_options )
driver.get("https://www.thailand-property.com/")
driver.maximize_window()

# input = driver.find_element("xpath", "//div[contains(@class, 'form-control')]")
# Find the realty search bar
input_element = driver.find_element("xpath", "//input[@placeholder='Search by location, station, condo name or keyword']")
input_element.click()
#enter search bar input
input_element.send_keys("example text")

time.sleep(5)


print(input)
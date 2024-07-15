# This is a headless browser scraper that scrapes https://www.fivestars-thailand.com/en/sale/bangkok
#for property prices. 
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
#connect to the page with all of the available properties in the Bangkok location
driver.get("https://www.fivestars-thailand.com/en/sale/bangkok")
driver.maximize_window()



for i in range(1, 10):
    # Locate the button using its id
    view_more_button = driver.find_element("id", "show-more")
    # Click the button
    view_more_button.click()
    time.sleep(5)

print(input)

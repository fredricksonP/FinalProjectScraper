from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
import time

# This was a first attempt at scraping https://www.thailand-property.com/. 
# I was foolishly trying to use the web driver to do every part of browsing the 
# website that human would do. I visited the site, and was trying to enter the search
# bar and enter my search criteria and then hit enter. However, I discovered that 
# I could just send my search criteria throuhg the url instead and cut out a lot 
# of uneccesary driver actions. Also, I found another realty website in the process 
# which was more optimal for my scraping purposes. That's what ended up leading to the
# creation of realtyAutoScraper and phuketScraper files. 

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
input_element.send_keys("Bankok")

time.sleep(5)

# Discovered here that the website has annoying ads and popups which make clicking 
# the correct buttons and finding the right elements extraordinarily difficult. 
# Other realty websites don't have this functionality. 

print(input)
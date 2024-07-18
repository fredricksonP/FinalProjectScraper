# Following this tutorial for automated browser scraping: https://www.youtube.com/watch?v=SPM1tm2ZdK4
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
import time

# This file was way of learning how to use browser automation in python. I had nearly no
# clue how to use a web driver. My only "experience" was my failure to try and 
# incorperate selenium web driver into scala. However, this attempt proved to be
# successful and the tutorial I followed was quite helpful. However, the tutorial 
# chose to scrape websites using xpath instead of beautiful soup. This gave me an
# opportunity to learn yet another scraping technique, but I was not as fond of using
# xpath over beautiful soup for scraping.

#Specify our expiremental options to connect to sites
my_options = Options()
my_options.add_experimental_option("detach", True)

#Connect to target site with the chrome driver
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=my_options )
driver.get("https://www.neuralnine.com/")
driver.maximize_window()


links = driver.find_elements("xpath", "//a[@href]")

#loop to find the book button and click it 
for link in links:
    if "Books" in link.get_attribute("innerHTML"):
        link.click()
        break
     

book_links = driver.find_elements("xpath", 
                                "//div[contains(@class, 'elementor-column-wrap')][.//h2[text()[contains(., '7 IN 1')]]][count(.//a)=2]//a")


#Testing to see what book links is returning   
# for l in book_links:
#     print(l.get_attribute("href"))

#click on the first element found in booklinks to navigate to amazon
book_links[0].click()
 
#Code to switch tabs to scrape the link that was found
driver.switch_to.window(driver.window_handles[1]) #switches to the first new tab
 
#letting the page load
time.sleep(3)

purchase_buttons = driver.find_elements("xpath", "//a[.//span[text()[contains(., 'Paperback')]]]//span[text()[contains(., '$')]]")

# Get all of the inner html from the purchase buttons on amazon
for button in purchase_buttons:
    print(button.get_attribute("innerHTML"))

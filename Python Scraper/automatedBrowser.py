# Following this tutorial for automated browser scraping: https://www.youtube.com/watch?v=SPM1tm2ZdK4
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

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
                                "//div[contains(@class, 'elementor-column-wrap)][//h2[text()[contains(., '7 IN 1')]]][count(.//a)=2]//a")


for link in book_links:
    print(link.get_attribute("href"))
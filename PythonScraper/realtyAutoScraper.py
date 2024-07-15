# This is a headless browser scraper that scrapes https://www.fivestars-thailand.com/en/sale/bangkok
#for property prices. 
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
import time
from bs4 import BeautifulSoup
import re #for cleaning the price string


#Specify our expiremental options to connect to sites
my_options = Options()
my_options.add_experimental_option("detach", True)

#Connect to target site with the chrome driver
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=my_options )
#connect to the page with all of the available properties in the Bangkok location
driver.get("https://www.fivestars-thailand.com/en/sale/bangkok")
driver.maximize_window()

CONVERSION_RATE = 0.02766


for i in range(1, 2):
    # Locate the button using its id
    print("loaded more content: " + str(i))
    view_more_button = driver.find_element("id", "show-more")
    # Click the button
    view_more_button.click()
    time.sleep(3) #wait for content to load

#now, after loading a lot of the content onto the page, I will parse it all
# properties = driver.find_elements("xpath", 
#                                 "//div[contains(@class, 'property-block')]")

page_source = driver.page_source
soup = BeautifulSoup(page_source, 'html.parser')

properties = soup.find_all('div', class_='property-block')


for p in properties:
    title_elem = p.find('a', class_='property-block-name title')
    title = title_elem.get_text(strip=True) if title_elem else 'N/A'

    price_elem = p.find('div', class_='property-block-bottom__').find_all('p')[2] if p.find('div', class_='property-block-bottom__') else None
    price = price_elem.get_text(strip=True) if price_elem else 'N/A'

    #TODO: Convert Thai baht to usd here
    cleaned = re.sub(r'\D', '', price)

    # Convert to integer
    priceInt = int(cleaned)
    # print(priceInt)
    usd = int(cleaned) * CONVERSION_RATE

    rooms_elem = p.find('p', class_='property-block-bottom__beds')
    rooms = rooms_elem.get_text(strip=True) if rooms_elem else 'N/A'
    
    print(f"Title: {title}")
    print(f"Price THB: {price}")
    print(f"Price USD: ${usd}USD")
    print(f"Rooms: {rooms}")
    print('---')


driver.quit()

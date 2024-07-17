# This is a headless browser scraper that scrapes https://www.fivestars-thailand.com/en/sale/bangkok
#for property prices. This code uses selenium to dynamically load content on the website
# Once the content is loaded, beautiful soup is used to scrape the target content
# This code introduces purposeful optimized timeouts to give dynamic content time
# to load. Specifically, this code scrapes realty prices for the bankok area. 
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
import time
from bs4 import BeautifulSoup
import re #for cleaning the price string
from dataclasses import dataclass #for making a python struct

#Specify the expiremental options to connect to sites
my_options = Options()
my_options.add_experimental_option("detach", True)

#Connect to target site with the chrome driver
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=my_options )
#connect to the page with all of the available properties in the Bangkok location
driver.get("https://www.fivestars-thailand.com/en/sale/bangkok")
driver.maximize_window()

CONVERSION_RATE = 0.02766

# This was the best way I found to make a struct in python. 
# Found here: https://stackoverflow.com/questions/35988/c-like-structures-in-python
@dataclass
class PropertyListing:
    title: str = "N/A"
    thai_price: float = -1.0
    usd_price:float = thai_price * CONVERSION_RATE
    rooms: int = -1

#Testing property struct
# p = PropertyListing("Test Prop", 300000, 10000, 3)
# print(p) 
all_properties: list[PropertyListing] = []


#Loop to continually load more listings from the page
for i in range(1, 10):
    # Locate the button using its id
    # print("loaded more Bankok content: " + str(i))
    view_more_button = driver.find_element("id", "show-more")
    # Click the button
    view_more_button.click()
    time.sleep(3) #wait for content to load

#Now that a bunch of content it loaded, connect to the page with beautiful soup 
page_source = driver.page_source
soup = BeautifulSoup(page_source, 'html.parser')
#get all of the property block elements
properties = soup.find_all('div', class_='property-block')

#Going through each property div block and extracting target data
for p in properties:
    title_elem = p.find('a', class_='property-block-name title')
    title = title_elem.get_text(strip=True) if title_elem else 'N/A'

    price_elem = p.find('div', class_='property-block-bottom__').find_all('p')[2] if p.find('div', class_='property-block-bottom__') else None
    price = price_elem.get_text(strip=True) if price_elem else 'N/A'

    #Convert Thai baht scraped from the list to usd
    cleaned = re.sub(r'\D', '', price) #use regrex to drop all non-numeric chars
    # Convert to integer
    priceInt = int(cleaned)
    usd = priceInt * CONVERSION_RATE

    rooms_elem = p.find('p', class_='property-block-bottom__beds')
    rooms = rooms_elem.get_text(strip=True) if rooms_elem else 'N/A'
    
    # print(f"Title: {title}")
    # print(f"Price THB: {price}")
    # print(f"Price USD: ${usd}USD")
    # print(f"Rooms: {rooms}")
    # print('---')
    newProp = PropertyListing(title, price, usd, rooms)
    all_properties.append(newProp)


driver.quit()
# print("\n\n\nProperties Found: ")
# print(all_properties)

#Calc target stats 
max_rooms = max(all_properties, key=lambda x: x.rooms)
max_price = max(all_properties, key=lambda x: x.usd_price)
min_price = min(all_properties, key=lambda x: x.usd_price)
avg_price = sum(property.usd_price for property in all_properties) / len(all_properties)

#output stats to the consol
print(f"Aggregate statistics scraped from {len(all_properties)} listings in Bankok: ")
print(f"Max Room Size: {max_rooms.rooms}")
print(f"Max price: {max_price.usd_price}")
print(f"Min price: {min_price.usd_price}")
print(f"Avg price: {avg_price}")
print("--------------------------------------------")
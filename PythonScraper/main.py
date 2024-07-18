import requests
from bs4 import BeautifulSoup as BS
import concurrent.futures
import time
from random import choice #allows me to select a random element from a list

# This file was all about practicing using free proxies, and how to rotate proxies. 
# The file code connects to a free repository of proxies on github and scrapes
# the proxies from the page. Then a proxy is chosen at random and a connection
# to Amazon is attempted. This file is a great example of how I would rotate proxies
# in the future if I were using more reliable paid for proxies. However, as I've come
# to find out, free proxies are largely useless becuase they either don't work 
# (can't connect to any site), or if they do work, they can't connect to a reputable 
# website with security measures in place to stop free proxies. 

# This tutorial helped me get started with scraping in python
# https://nanonets.com/blog/web-scraping-with-python-tutorial/

# This function connects to a github page that contains a list of proxies
# and scrapes the list of proxies from github. Then the function tries to 
# connect to amazon with the various proxies (choes at random) from the list. 
def get_proxies():
    # URL of the raw text file containing the proxy list
    url = "https://raw.githubusercontent.com/clarketm/proxy-list/master/proxy-list-raw.txt"
    response = requests.get(url) #connect to github

    # if req is successful
    if response.status_code == 200:
        proxy_list_text = response.text
        # Split text into lines
        proxy_list_lines = proxy_list_text.splitlines()
        # print proxies
        # for proxy in proxy_list_lines:
        #     print(proxy)

        # print(proxy_list_lines)
        return proxy_list_lines
    # print error message if connectoin fails
    else:   
        print(f"Failed to retrieve the proxy list. Status code: {response.status_code}")


proxies = get_proxies()

def get_rand_prox(proxies):
    return{"https" : choice(proxies)}
def working_proxy():
    working = []
    for i in range(40):
        proxy = get_rand_prox(proxies)
        print(f"using {proxy}...")
        try: 
            req = requests.get("https://www.amazon.com", proxies=proxy, timeout=3)
            print(req.status_code)
            if req.status_code == 200:
                working.append(proxy)
        except: 
            print("failed")
            pass
    return working

working = working_proxy()
print(working)    

# Step 1: Send a GET request to the web page
# url = 'https://en.wikipedia.org/wiki/Main_Page'
# response = requests.get(url)
#Define a list to hold all the url's from the csv file
listOfUrls = []
bookData = []

#stuctrure I made to hold book details. 
class BookStruct:
    def __init__(book, title, price, field3):
        book.title = title
        book.price = price
        book.field3 = field3

#Enter the filepath for the csv file full of potential proxies here
file = "../src/main/scala/bookslinks.csv"
with open(file, 'r') as file:
    lines = file.readlines()
    for line in lines:
        listOfUrls.append(line.strip())
        # print(line.strip())
print("\n\n" + listOfUrls[0])

#Function to scrape book data from each page. 
def get_book_data(url):
    doc = requests.get(url)
    content = BS(doc.content, 'html.parser')
    bookTitle = content.find('h1').text
    print("\n" + bookTitle)
    return


start_time = time.time()

with concurrent.futures.ThreadPoolExecutor() as ex:
    ex.map(get_book_data, listOfUrls)

end_time = time.time()

elapsed_time = end_time - start_time

print(f"Elapsed time: {elapsed_time} seconds")

# # Step 2: Check if the request was successful
# if response.status_code == 200:
#     # Step 3: Parse the HTML content
#     soup = BeautifulSoup(response.content, 'html.parser')
    
#     # Step 4: Extract data
#     # For example, extract all the links on the page
#     links = soup.find_all('a')
    
#     for link in links:
#         print(link.get('href'))
# else:
#     print(f"Failed to retrieve the page. Status code: {response.status_code}")

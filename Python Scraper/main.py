import requests
from bs4 import BeautifulSoup as BS
import concurrent.futures

# Step 1: Send a GET request to the web page
# url = 'https://en.wikipedia.org/wiki/Main_Page'
# response = requests.get(url)
#Define a list to hold all the url's from the csv file
listOfUrls = []
bookData = []

class BookStruct:
    def __init__(book, title, price, field3):
        book.title = title
        book.price = price
        book.field3 = field3

#Enter the filepath for the csv file here
file = "../src/main/scala/bookslinks.csv"
with open(file, 'r') as file:
    lines = file.readlines()
    for line in lines:
        listOfUrls.append(line.strip())
        # print(line.strip())
print("\n\n" + listOfUrls[0])

def get_book_data(url):
    doc = requests.get(url)
    content = BS(doc.content, 'html.parser')
    bookTitle = content.find('h1').text
    print("\n" + bookTitle)
    return

with concurrent.futures.ThreadPoolExecutor() as ex:
    ex.map(get_book_data, listOfUrls)

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

import threading
import queue
import concurrent.futures
import requests

q = queue.Queue()
valid_proxies = []

#Function to check if a free proxy is valid or not
def check_proxies(proxy):
    try: 
        print("checking " + proxy + "...")
        # res = requests.get("https://www.amazon.com/", proxies={"http": proxy, "https": proxy}, timeout=3)
        res = requests.get("http://books.toscrape.com/catalogue/category/books_1/index.html", proxies={"http": proxy, "https": proxy}, timeout=3)
        # res = requests.get("https://thailand.kinokuniya.com/t/books/english-books?gad_source=1&gclid=Cj0KCQjwv7O0BhDwARIsAC0sjWMW7-zp4QMToaw7SKMTurT4KP8xKjXJzyLx6ICDRGpJPkRBBd3vEBwaAoSWEALw_wcB", proxies={"http": proxy, "https": proxy}, timeout=3)
    except: 
        print(proxy + " Failed")
        # continue
    if res.status_code == 200:
        with threading.Lock():  # avoid race conditions by locking file writes
            with open("working_proxies.txt", "a") as out_f:
                out_f.write(proxy + "\n")
                print(f"SUCCESS: {proxy}")

#Ask proxy scrape for the newest list of free proxies with their GET API call
#Proxy scrape updates this list of proxies every 5 minutes so the API call gets the newest list of proxies
api_url = "https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&proxy_format=ipport&format=text"
proxies_response = requests.get(api_url)

#If the response is valid, then I want to save the proxies to a file
if proxies_response.status_code == 200:
    file_text = proxies_response.text 
    with open("potential_proxies.txt", "w") as in_file:
        in_file.write(file_text)


#Open the list of free proxies
with open("http_proxies.txt", "r") as f: 
    print(f)
    proxies = []
    proxies = f.read().split("\n")
    with concurrent.futures.ThreadPoolExecutor() as ex:
        ex.map(check_proxies, proxies)
    # for p in proxies:
    #     q.put(p)

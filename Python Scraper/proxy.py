import threading
import queue

import requests

q = queue.Queue()
valid_proxies = []


with open("http_proxies.txt", "r") as f: 
    print(f)
    proxies = f.read().split("\n")
    for p in proxies:
        q.put(p)

# with open("working_proxies.txt", "w") as out_f:


def check_proxies():
    global q
    while not q.empty():
        proxy = q.get()
        try: 
            print("checking " + proxy + "...")
            # res = requests.get("https://www.amazon.com/", proxies={"http": proxy, "https": proxy}, timeout=3)
            res = requests.get("http://books.toscrape.com/catalogue/category/books_1/index.html", proxies={"http": proxy, "https": proxy}, timeout=3)
            # res = requests.get("https://thailand.kinokuniya.com/t/books/english-books?gad_source=1&gclid=Cj0KCQjwv7O0BhDwARIsAC0sjWMW7-zp4QMToaw7SKMTurT4KP8xKjXJzyLx6ICDRGpJPkRBBd3vEBwaAoSWEALw_wcB", proxies={"http": proxy, "https": proxy}, timeout=3)
        except: 
            continue
        if res.status_code == 200:
            with threading.Lock():  # avoid race conditions by locking file writes
                with open("working_proxies.txt", "a") as out_f:
                    out_f.write(proxy + "\n")
                    print(f"SUCCESS: {proxy}")



for t in range(10):
    threading.Thread(target=check_proxies).start()
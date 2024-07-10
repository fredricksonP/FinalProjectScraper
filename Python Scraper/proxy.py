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


def check_proxies():
    global q
    while not q.empty():
        proxy = q.get()
        try: 
            print("checking " + proxy + "...")
            res = requests.get("https://www.amazon.com/", proxies={"http": proxy, "https": proxy}, timeout=3)
        except: 
            continue
        if res.status_code == 200:
            print("SUCCESS:" + proxy)

for t in range(10):
    threading.Thread(target=check_proxies).start()
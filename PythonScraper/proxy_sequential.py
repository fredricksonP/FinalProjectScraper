import requests
import time
# This file calls an API for proxyscrape.com which returns a list of free
# proxies (stored in a txt file) that's updated every five minutes from the website. However, 
# as I've come to learn, most free proxies don't work. So this code checks the 
# list of proxies (usually around 1200 proxies) sequentially. 
# If a connection can be establisd successfully, I write the
# the proxies IP to a file called working_proxies.txt.
# However, this sequential code takes an exceptionally long time 
# And I've never been patient enought to wait for it execution completion. 
# I estimate that it will take an upper bound of 75 minutes to run. Feel free
# to find out for yourself if you have some free time. The longest I waited was
# nearly 10 minutes. 

valid_proxies = []

#Function to check if a free proxy is valid or not
def check_proxies(proxy):
    try: 
        # print("checking " + proxy + "...")
        # Check if the proxy can connect to the books.toscrape website that I scrape in scala
        res = requests.get("http://books.toscrape.com/catalogue/category/books_1/index.html", proxies={"http": proxy, "https": proxy}, timeout=3)
        if res.status_code == 200:
            with open("working_proxies.txt", "a") as out_f:
                out_f.write(proxy + "\n")
                print(f"SUCCESS: {proxy}")
    except: 
        print(proxy + " Failed")
        # continue


#Ask proxy scrape for the newest list of free proxies with their GET API call
#Proxy scrape updates this list of proxies every 5 minutes so the API call gets the newest list of proxies
api_url = "https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&proxy_format=ipport&format=text"
proxies_response = requests.get(api_url)

#If the response is valid, then I want to save the proxies to a file
if proxies_response.status_code == 200:
    file_text = proxies_response.text 
    with open("potential_proxies.txt", "w") as in_file:
        in_file.write(file_text)

# Start the timer
start_time = time.time()

#Open the list of free proxies and use concurrent futures
# to Check the proxies in parallel
with open("http_proxies.txt", "r") as f: 
    print(f)
    proxies = []
    proxies = f.read().split("\n")
    #Seqeuntailly traverse the file and check proxies
    for prox in range(1, len(proxies)):
        p = proxies[prox]
        print(f"Checking proxy number: {prox} for proxy {p}")
        check_proxies(p)

    # for p in proxies:
    #     check_proxies(p)


# End the timer
end_time = time.time()

# Calculate and print the elapsed time
elapsed_time = end_time - start_time
print(f"Time taken to check all proxies sequentially: {elapsed_time:.2f} seconds")

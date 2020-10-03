import requests 
from bs4 import BeautifulSoup 
  
def generateWords(): 
    #open document
    with open('words.txt', 'w') as f:
        f.write("data[\'words\'.append({")
        count = 0
        word = "building"
        f.write(str(count) + ": { \'word' : \'" + word + "\',\n")
        # the target we want to open
        url='http://wordassociation.org/words/' + word
        
        #open with GET method 
        resp=requests.get(url) 
        
        #http_respone 200 means OK status 
        if resp.status_code==200: 
            print("Successfully opened the web page")
            print("word is " + word)
            # we need a parser,Python built-in HTML parser is enough. 
            soup=BeautifulSoup(resp.text,'html.parser')
            all_related = soup.find_all("a", class_="stats")
            related_from = all_related[6:]
            taboo = 1
            for connection in related_from:
                f.write("\'taboo" + str(taboo) + "\': \'")
                f.write(str(connection) + "\'\n")
        else:
            print("Couldn't open webpage for " + word)

            
        
generateWords()
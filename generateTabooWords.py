import requests
from bs4 import BeautifulSoup
import re

def generateWords():
    collectionName = 'things'
    #open document
    with open('things.txt', 'r') as j:
        with open('words.txt', 'w') as f:
            word = re.sub(r'[^A-Za-z]', '', j.readline())
            count = 0
            while word:
                # the target we want to open
                url='http://wordassociation.org/words/' + word

                #open with GET method
                resp=requests.get(url)

                #http_respone 200 means OK status
                if resp.status_code==200:
                    print("Successfully opened the web page")
                    print("word is " + word)
                    f.write("data[\'words\'][\'" + collectionName + "\'].append({")
                    f.write(str(count) + ": { \'word' : \'" + word + "\',\n")
                    # we need a parser,Python built-in HTML parser is enough.
                    soup=BeautifulSoup(resp.text,'html.parser')
                    all_related = soup.find_all("a", class_="stats")
                    related_from = all_related[6:]
                    taboo = 1
                    for connection in related_from:
                        f.write("\'taboo" + str(taboo) + "\': \'")
                        f.write(str(connection.string) + "\'")
                        if taboo != 5:
                            f.write(",")
                        f.write("\n")
                        taboo+=1
                    f.write("}}]}\n")
                else:
                    print("Couldn't open webpage for " + word)
                word = re.sub(r'[^A-Za-z]', '', j.readline())
                count+=1

generateWords()

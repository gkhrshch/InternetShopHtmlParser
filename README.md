# InternetShopHtmlParser

# Table of Contents
* [Project description](#description)
* [Technologies applied](#technologies)
* [How to run the project](#launch)

# <a name="description"></a>Project description
Quite a primitive parser, extracts items loaded on initial http request to https://www.aboutyou.de/maenner/bekleidung internet shop website. Probably thread-safe.

# <a name="technologies"></a>Technologies used
* Java 12
* Maven 4.0.0
* Jsoup 1.11.3
* Gson 2.8.5
* Log4j 1.2.17

# <a name="launch"></a>How to run the project?
1. Download JAR file:
https://github.com/gkhrshch/InternetShopHtmlParser/blob/master/InternetShopHtmlParser.jar
2. Open cmd/bash/other console app
3. ~$ cd {$path to InternetShopHtmlParser.jar file} 
4. ~$ java -jar InternetShopHtmlParser.jar 
5. Enjoy your parsed items in newly-created Products.json file in the same directory where the InternetShopHtmlParser.jar file is 

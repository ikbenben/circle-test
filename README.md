# Architectural Overview

The application is built with Scala (http://scala-lang.org) using the PlayFramwork 2.5 (https://www.playframework.com/). I chose this combination as I have not ever used Scala before and I wanted to play if I was going to do some tech test. In total it took me about a day, maybe just a bit over a day to put this together.

The app is structured following a MVC patterns, with a Line model and a LinesController controller. There isn't a view as it isn't required for this exercise. Busines logic can be found in the LinesService trait and it's concrete implementation CachingLinesService. I chose to create a Line model with a LineService trait so that the backing class for the service can be easily changed, ie to a db backing, without affecting any of the code using the service. It is abstracted away from the controllers / views

The CachingLinesService uses the Play CacheApi, using dependancy injection, to pre-parse the file and store it for fast retrieval. Each line is stored as its own entry in the cache so the entire file / string array does not need to be kept in memory. This should allow the app to scale appropriately. Play CacheApi uses EhCache (http://ehcache.org) for its backend by default and is configured to use local disk caching. It is currently configured to store up to 10,000,000 entries on disk with 10,000 in memory. These settings can be updated in conf/ehcache.xml if needed. The CacheApi is configurable so a different backing implementation such as Redis could be used for distributed caching

For simplicty, the system is using SBT to run the app but should be packaged and deployed to a production environment, such as aws or cloudfoundry, if this was to be deployed for real. SBT is packaged in the source tree so there should be no need to install it.

# Prerequisites

Scala needs to be installed and configured properly before the app will run.

# How to Run

Build script simply runs the unit tests

    ./build.sh

To run the app, simply run the run.sh script, passing in the file to use to load the lines. An example is as follows:

    ./run.sh ./dist/test.txt

# Performance

Because we are using Ehcache with disk storage and each row is stored as its own entry in the cache, there should not be any performance issue as the size of the file changes.

Peformance as the number of users grow could become a problem due to too many requests for the server to handle. Ehcache is pretty performant but it will eventually hit a wall. In this case, it is easy enough to scale out the application on multiple server to distribute the load

# Things to change

* The first request can take a long time to load for large files as this is when the code to parse the file gets invoked. It would be better to preload the data into cache during application start up and before it starts to accept requests.
* The cache initialization code could be updated to only parse the file if the supplied filename has changed from the last run. This would help offset the first request load time issue identified above so that it was not experienced across restarts
* The cache initialization code could use some refactoring. See the comments in the LinesService.scala file for details
* Using cache to store the entire contents of the file could be problematic if the number of rows in the file exceeds the cache size configuration. In this scenario, itemsitems will be expired from cache which will cause 413 exceptions when it shoudn't. Would be better to use a more persistent backend to store the entire contents such as a database.
* Application should be returning a 404 response codes instead of 413 if the index does not exist. From w3, a 413 response code means
> The server is refusing to process a request because the request entity is larger than the server is willing or able to process. The server MAY close the connection to prevent the client from continuing the request
* Add end points to list all lines, create a new line, update an exisitng line or delete a line. modifications would update file appropriately
* Add code that checks if the file has changed and if so, reloads the data in a background task
* add better exception handling for when reading the file in case file is not found or it is not readable (ie permissions)

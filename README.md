Welcome to Don't Lose Hope!
===================

At one point or another, everyone knows the feeling of misplacing an item. At best, the lost is inconvenient. At worst, the item can be deeply sentimental and irreplaceable.

By leveraging the MTA Lost and Found feed to create a centralized portal for filing a claim, our app enables commuters to visualize different categories of lost items and become notified when their item has been recovered.

Since the time that the MTA released this data, more than 180,000 items has been catalogued, yet only 60,000 has been claimed. We believe that our app can help close this gap.

Whether if you lost your wallet, keys, or a sentimental hand-knitted scarf, FoundIt will help make sure you find it.

----------

## Getting Started

In order to start the web application, you will need Java 8 to installed in your local machine.

Following that, make sure you fill out the play configuration file with your AWS Credentials and also you should rename your table if you desire. It's defaulted to 'lost-property' in this case:

> aws.access.key="" aws.secret.key="" aws.dynamodb.table=lost-property

To run the application use the activator script:

> ./activator run -- starts the web application ./activator war --
> packages the application into a .WAR file so you can deploy it to
> Tomcat, Jetty, etc.


----------

## Available Endpoints
All the available endpoints are under the routes file inside the conf directory.

> GET		/laf/latest						controllers.LostAndFound.getLatestData()
> 
> GET	  /laf/category/:category/:days	  controllers.LostAndFound.getLastItems(category:String, days: Integer)
> 
> GET /laf/category/:category		controllers.LostAndFound.getDataForCategory(category: String)
> 
> GET 	/*file							controllers.Assets.at(path="/public", file)

----------

## DynamoDB tables
Currently DynamoDB tables use a Category Name as the Hash Key and use a Timestamp as a Range Key. These are baked into the application. We will be moving these away to a configuration file, which will be read by a CFN script to either create or update our tables.




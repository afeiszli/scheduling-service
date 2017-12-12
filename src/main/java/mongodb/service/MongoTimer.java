package mongodb.service;

import static java.util.concurrent.TimeUnit.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import com.mongodb.*;
import com.mongodb.util.JSON;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/*
 * A class that contains the logic to 
 * get the parameter for the current time,
 * query mongo db every X seconds,
 * change the mongo results to sent = true,
 * and then print out the returned query
 * TODO: Post to a message queue instead of console
 * TODO: only change sent to true AFTER confirming the post to message queue
 */
@Component
@PropertySource("classpath:application.properties")
public class MongoTimer {

	//scheduler is used for mongoFuture and will set params for frequency of mongo query
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);	

	//This method gets called in main to run queries. Runs queryMongo method every X seconds
	public void runMongoQuery() {
		Runnable queryMongo = new QueryMongo();
		@SuppressWarnings("unused")
		ScheduledFuture<?> mongoFuture = this.scheduler.scheduleAtFixedRate(queryMongo, 0, 5, SECONDS);
	}
	
	//Actually does the legwork for mongo querying
	@Component
	public static final class QueryMongo implements Runnable {
		
		public void run() {
			//setup mongo connection
			MongoClient mc = null;
			try {
				mc = new MongoClient("localhost", 27017);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//use TimePeriod class to get a string that represents current time period,
			//used to query mongo collection to find currently relevant messages
			String timePeriod = TimePeriod.timePeriod();
			
			
			
			//get appropriate DB/Collection and then query using param above
			DB db = mc.getDB("test");
			DBCollection dbc = db.getCollection("testMicro2");
			DBObject query = new BasicDBObject("timePeriod", timePeriod);
			DBCursor results = dbc.find(query);
			//iterates through query, changes 'sent' to true, and posts queries to console
			for( DBObject result : results ) {
				System.out.println("This is a result: "+ result);
				//dbc.update(result,new BasicDBObject("$set", new BasicDBObject("sent",true)));
				String message = result.toString();
				
				MessageProducer mp = new MessageProducer();
				
				String returnedString = "";
				try {
					returnedString = mp.service(message);
					System.out.println(returnedString + " In Try");
				
				} catch (ServletException e) {
					System.out.println("ServletException on message: "+ message );
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException on message: "+ message );
					e.printStackTrace();
				}
				System.out.println(returnedString);
				
			}
		}

	}
	/*
	@Bean 
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	*/
}



/*

//Inject these values for all vars at some point

@Value("${mongoIP}")
String host;

@Value("${mongoPort}")
int port;

@Value("${schedulerDelay}")
int delay;

@Value("${schedulerFrequencySeconds}")
int frequency;

@Value("${mongoDBName}")
String db;

@Value("${mongoDBCollection}")
String collection;

@Value("${mongoDBQueryField}")
String queryField;

@Value("${mongoDBUpdateField}")
String updateField;
*/
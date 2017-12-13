package mongodb.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


@RestController
public class MessageController {

	@Autowired
	private Environment env;

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String returnHelloWorld() {
		return "Hello World";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/sendMessage/", method = RequestMethod.POST)
    public String returnMessage(@RequestBody ScheduledMessage message) throws JsonProcessingException {
      
       	System.out.println(message.toJSONString());
   
       	String tp = TimePeriod.timePeriod();
    	
    	if( message.getTimeFrame() == tp || message.getTimeFrame() == "anytime" ) {
    	 		
			String brokerMessage = message.toJSONString();
    		
			MessageProducer mp = new MessageProducer();
			
			String returnedString = "";
			try {
				returnedString = mp.service(brokerMessage);
			
			} catch (ServletException e) {
				System.out.println("ServletException on message: "+ brokerMessage );
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException on message: "+ brokerMessage );
				e.printStackTrace();
			}
			System.out.println(returnedString);
    	}
    	
    	else {
    		
    		MongoClient mc = null;
			try {
				

				 MongoCredential credential = MongoCredential.createMongoCRCredential("admin", "test", "password".toCharArray() );

				 MongoClientOptions options = MongoClientOptions.builder().build();

				 mc = new MongoClient(new ServerAddress("localhost", 27017),
				                                           Arrays.asList(credential),
				                                           options);
			
//				mc = new MongoClient("localhost", 27017);
//				mc = new MongoClient(new MongoClientURI( "mongodb://admin:password@localhost/data" ));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DB db = mc.getDB("test");
			DBCollection dbc = db.getCollection("testMicro2");
			
			dbc.insert(message.toDBObject());
    	}
    	String msg = message.toString();
		String path = env.getProperty("server.port");
		return msg + "\nServer Port=" + path;
    }
    

	/*
	@RequestMapping( value = "/testSend/", method = RequestMethod.GET )
	public String returnTest() {
		return "it works";
	}
	
	@RequestMapping( value = "/testSend2/", method = RequestMethod.POST )
	public String returnTest2(@RequestBody ScheduledMessage message) {
	//public String returnTest2() {
	//return "it worked?";
		return message.toString();
	}
	
	@RequestMapping( value = "/testSendService3/", method = RequestMethod.POST )
	public String returnTest3(@RequestBody ScheduledMessage message) throws JsonProcessingException {
		String msg = message.toString();
		String path = env.getProperty("server.port");
		return msg + "\nServer Port=" + path;
	}
	*/
}

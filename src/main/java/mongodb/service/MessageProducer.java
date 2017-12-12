package mongodb.service;

import java.io.IOException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Component
public class MessageProducer extends HttpServlet {
	
	//@HystrixCommand(fallbackMethod="callFailed")
    protected String service( String message ) throws ServletException, IOException {
        
		try {
            //created ConnectionFactory object for creating connection 
            ConnectionFactory factory = new ActiveMQConnectionFactory("admin", "password", "tcp://0.0.0.0:61613");
            //Establish the connection
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("Test");
            //Added as a producer
            javax.jms.MessageProducer producer = session.createProducer(queue);
            // Create and send the message
            TextMessage msg = session.createTextMessage();
            msg.setText(message);
            producer.send(msg);
            return "Message Sent";
            
            
        } catch (Exception e) {
            System.out.print("something broke! ");
            return "Exception";
        }
    }
	
	protected String callFailed( String message ) {
		return "Message Send Failed";
	}
	
}
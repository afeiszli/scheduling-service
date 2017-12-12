package mongodb.service;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ScheduledMessage {
	
	@Id
	private String id;
	
	private String messageBody;
	private String timeFrame;
	private String messageType;
	private Boolean isSent;
	
	public ScheduledMessage( String messageBody, String timeFrame, String type ) {
		
		this.messageBody = messageBody;
		this.timeFrame = timeFrame;
		this.messageType = type;
		this.isSent = false;
		
	}
	
	public ScheduledMessage() {
		this.isSent = false;
	}

	public String getID() {
		return this.id;
	}
	
	public String getMessageBody() {
		return this.messageBody;
	}
	
	public String getTimeFrame() {
		return this.timeFrame;
	}
	
	public Boolean getIsSent() {
		return this.isSent;
	}
	
	public void setID( String id ) {
		this.id=id;
	}
	
	public void setMessageBody( String mb ) {
		this.messageBody = mb;
	}
	
	public void setTimeFrame( String tf ) {
		this.timeFrame = tf;
	}
	public void setIsSent( Boolean sent ) {
		this.isSent = sent;
	}
	
	public String toString() {
		return String.format( "Message[id=%s, body=%s, time frame=%s, sent=%s]", this.id, this.messageBody, this.timeFrame, this.isSent );
	}
	
	public String toJSONString() throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
	
	public DBObject toDBObject() {
		return new BasicDBObject("body", this.messageBody)
				.append("timePeriod", this.timeFrame)
				.append(messageType, this.messageType)
				.append("sent", this.isSent);
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}

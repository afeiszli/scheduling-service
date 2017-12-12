package mongodb.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepo extends MongoRepository<ScheduledMessage, String>{
	
	public List<ScheduledMessage> findByTimeFrame( String timeFrame );
	
}

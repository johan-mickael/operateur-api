package function;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Mongo {
	public static String uri = "mongodb://localhost:27017";
	public static String database  = "s5final";
	
	public static MongoClient connect() {
		return MongoClients.create(Mongo.uri);
	}
}

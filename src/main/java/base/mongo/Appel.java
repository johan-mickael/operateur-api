package base.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import function.Mongo;
import helper.Helper;

@Document(collection="appel")
public class Appel {
	String destinateur;
	String destinataire;
	String minute;
	String seconde;
	String date;
	
	public Appel(String destinateur, String destinataire, String minute,
			String seconde, String date) {
		super();
		this.destinateur = destinateur;
		this.destinataire = destinataire;
		this.minute = minute;
		this.seconde = seconde;
		this.date = date;
	}
	
	
	public Appel(String destinataire, String minute, String seconde, String date) {
		super();
		this.destinataire = destinataire;
		this.minute = minute;
		this.seconde = seconde;
		this.date = date;
	}


	public Appel() {
		super();
	}
	public String getDestinateur() {
		return destinateur;
	}
	public void setDestinateur(String destinateur) {
		this.destinateur = destinateur;
	}
	public String getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSeconde() {
		return seconde;
	}
	public void setSeconde(String seconde) {
		this.seconde = seconde;
	}
	public void setDate() throws Exception {
		Date date = Helper.HTMLDatetimeLocalToDate(this.date, "T");
		this.date = Helper.prettyDate(date, false);
	}
	
	public static ArrayList<Appel> historique(String numero) {
		ArrayList<Appel> array = new ArrayList<Appel>();
		MongoClient mongoClient = null;
        MongoDatabase database = null;
        try {
        	mongoClient = MongoClients.create(Mongo.uri);
        	database = mongoClient.getDatabase(Mongo.database);
        	
        	String collection_name = "appel";
            MongoCollection<org.bson.Document> collection = database.getCollection(collection_name);
            org.bson.Document query = new org.bson.Document(
            		"$or", Arrays.asList(
              		      new org.bson.Document("destinataire", numero),
              		      new org.bson.Document("destinateur", numero)));
            FindIterable<org.bson.Document> res= collection.find(query);
            for(org.bson.Document document : res) {
            	Appel appel = new Appel(document.getString("destinateur"), document.getString("destinataire"), document.getString("minute"), document.getString("seconde"), document.getString("date"));
            	array.add(appel);
            }
        } catch(Exception ex) {
        	ex.printStackTrace();
        	throw ex;
        } finally {
        	if(mongoClient != null) mongoClient.close();
        }
       
		return array;
	}
}

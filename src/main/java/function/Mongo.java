package function;


import java.util.ArrayList;

import org.bson.Document;

import base.mongo.Forfait;
import base.mongo.Offres;
import base.mongo.Utilisation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Mongo {
//	public static String uri = "mongodb+srv://root:root@mobiledb.f2sbp.mongodb.net";
	public static String uri = "mongodb://localhost:27017";
	public static String database  = "s5final";

	public static Offres getById(String id) throws Exception {
		MongoClient mongoClient = null;
        MongoDatabase database = null;
        Offres offre = null;
        try{
            mongoClient = MongoClients.create(Mongo.uri);
            database = mongoClient.getDatabase(Mongo.database);
           
            String collection_name = "offres";
            MongoCollection<Document> collection = database.getCollection(collection_name);
            Document query = new Document();
            query.append("idOffre", id);
            
            FindIterable<Document> res= collection.find(query);
            Document offer = null;
            for (Document document : res) {
                offer = document;
                break;
            }
            if(offer == null) throw new Exception("Aucune offre correspondante");
            offre = new Offres(offer.getString("id"), offer.getString("idOffre"), offer.getString("nom"), Double.parseDouble(offer.getString("tarif")), Integer.parseInt(offer.getString("validite")));
            @SuppressWarnings("unchecked")
			ArrayList<Document> forfaits = (ArrayList<Document>) offer.get("forfaits");
            for (Document document : forfaits) {
            	Forfait forfait = null;
            	Document doc = (Document) document.get("utilisation");
            	Utilisation util = null;
            	if(doc != null) util = new Utilisation(doc.getString("unite"), Double.parseDouble(doc.getString("operateur")) , Double.parseDouble(doc.getString("autres")), Double.parseDouble(doc.getString("international")));
            	if(util == null) {
            		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")));
            	} else if(document.getString("valeur") == null) {
            		forfait = new Forfait(document.getString("produit"), document.getString("unite"), util);
            	} else {
            		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")), util);
            	}
            	offre.getForfaits().add(forfait);
            }
            
        }  catch(Exception e){
            throw e;
        }  finally{
            if(mongoClient != null) mongoClient.close();
        }
		return offre;
	}
	
	public static ArrayList<Offres> findAll() throws Exception {
		MongoClient mongoClient = null;
        MongoDatabase database = null;
        ArrayList<Offres> array = new ArrayList<Offres>();
//        Offres offre = null;
        try{
            mongoClient = MongoClients.create(Mongo.uri);
            database = mongoClient.getDatabase(Mongo.database);
           
            String collection_name = "offres";
            MongoCollection<Document> collection = database.getCollection(collection_name);
            Document query = new Document();
            
            FindIterable<Document> res= collection.find(query);
            for (Document offer : res) { 
            	Offres offre = new Offres(offer.getString("id"), offer.getString("idOffre"), offer.getString("nom"), Double.parseDouble(offer.getString("tarif")), Integer.parseInt(offer.getString("validite")));
                @SuppressWarnings("unchecked")
    			ArrayList<Document> forfaits = (ArrayList<Document>) offer.get("forfaits");
                System.out.println(forfaits.size());
                for (Document document : forfaits) {
                	Forfait forfait = null;
                	Document doc = (Document) document.get("utilisation");
                	Utilisation util = null;
                	if(doc != null) util = new Utilisation(doc.getString("unite"), Double.parseDouble(doc.getString("operateur")) , Double.parseDouble(doc.getString("autres")), Double.parseDouble(doc.getString("international")));
                	if(util == null) {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")));
                	} else if(document.getString("valeur") == null) {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), util);
                	} else {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")), util);
                	}
                	offre.getForfaits().add(forfait);
                }
                array.add(offre);
            }
//            if(offer == null) throw new Exception("Aucune offre correspondante");
            
        } catch(Exception e){
            throw e;
        }  finally{
            if(mongoClient != null) mongoClient.close();
        }
		return array;
	}
	
	public static String findNameById(String idOffre, MongoClient mongoClient) throws Exception {
		MongoDatabase database = null;
		String name = null;
		try{
            database = mongoClient.getDatabase(Mongo.database);
           
            String collection_name = "offres";
            MongoCollection<Document> collection = database.getCollection(collection_name);
            Document query = new Document();
            query.append("idOffre", idOffre);
            
            FindIterable<Document> res= collection.find(query);
            Document offer = null;
            for (Document document : res) {
                offer = document;
                break;
            }
            if(offer == null) throw new Exception("Aucune offre correspondante");
            name = offer.getString("nom");
            
        } catch(Exception e){
            throw e;
        }
		return name;
	}
	
	public static ArrayList<Offres> findAllByType(String idType) throws Exception {
		MongoClient mongoClient = null;
        MongoDatabase database = null;
        ArrayList<Offres> array = new ArrayList<Offres>();
//        Offres offre = null;
        try{
            mongoClient = MongoClients.create(Mongo.uri);
            database = mongoClient.getDatabase(Mongo.database);
           
            String collection_name = "offres";
            MongoCollection<Document> collection = database.getCollection(collection_name);
            Document query = new Document();
            query.append("idType", idType);
            
            FindIterable<Document> res= collection.find(query);
            for (Document offer : res) { 
            	Offres offre = new Offres(offer.getString("id"), offer.getString("idOffre"), offer.getString("nom"), Double.parseDouble(offer.getString("tarif")), Integer.parseInt(offer.getString("validite")));
                @SuppressWarnings("unchecked")
    			ArrayList<Document> forfaits = (ArrayList<Document>) offer.get("forfaits");
                System.out.println(forfaits.size());
                for (Document document : forfaits) {
                	Forfait forfait = null;
                	Document doc = (Document) document.get("utilisation");
                	Utilisation util = null;
                	if(doc != null) util = new Utilisation(doc.getString("unite"), Double.parseDouble(doc.getString("operateur")) , Double.parseDouble(doc.getString("autres")), Double.parseDouble(doc.getString("international")));
                	if(util == null) {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")));
                	} else if(document.getString("valeur") == null) {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), util);
                	} else {
                		forfait = new Forfait(document.getString("produit"), document.getString("unite"), Double.parseDouble(document.getString("valeur")), util);
                	}
                	offre.getForfaits().add(forfait);
                }
                array.add(offre);
            }
//            if(offer == null) throw new Exception("Aucune offre correspondante");
            
        } catch(Exception e){
            throw e;
        }  finally{
            if(mongoClient != null) mongoClient.close();
        }
		return array;
	}
}

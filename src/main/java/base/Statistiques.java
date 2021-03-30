package base;

import helper.Helper;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import function.Function;
import function.Mongo;
import function.Response;

@SuppressWarnings("serial")
public class Statistiques implements Serializable {
	ArrayList<String> x;
	ArrayList<DataStat> y;
	String titre;
	public Statistiques(Connection co, String titre, String mois, String annee) throws Exception {
		super();
		this.titre = titre;
		try {
			this.setX(co, mois, annee);
			this.setY(co, mois, annee);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static Response getData(String str, String token) {
//		System.out.println(str);
		Response response = null;
		Connection co = null;
		try {
			Date date = Helper.HTMLDatetimeLocalToDate(str, "T");
			String mois = (date.getMonth()+1)+"";
			String annee = (date.getYear()+1900)+"";
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				response = new Response("200", "Get Stat OK", new Statistiques(co, "Offres", mois, annee));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		}
		return response;
	}
	
	public Statistiques() {
		super();
	}



	public void setX(Connection co, String mois, String annee) throws SQLException {
		this.x = new ArrayList<String>();
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT jour from v_joursMois where mois = ? and annee = ? order by jour";
		try {
			st = co.prepareStatement(sql);
			st.setString(1, mois);
			st.setString(2, annee);
			result = st.executeQuery();
			while(result.next()) this.x.add(result.getDate("jour").toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
	}
	
	public void setY(Connection co, String mois, String annee) throws Exception {
		this.y = new ArrayList<DataStat>();
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from v_offreStat";
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create(Mongo.uri);
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				this.y.add(new DataStat(co, result.getString("idOffre"), mois, annee, mongoClient));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
			if(mongoClient != null) mongoClient.close();
		}
	}


	public ArrayList<String> getX() {
		return x;
	}


	public ArrayList<DataStat> getY() {
		return y;
	}


	public String getTitre() {
		return titre;
	}

	
}

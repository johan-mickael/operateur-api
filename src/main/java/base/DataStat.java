package base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mongodb.client.MongoClient;

import function.Mongo;

public class DataStat {
	ArrayList<Double> data;
	String label;
	public DataStat(Connection co, String idOffre, String mois, String annee, MongoClient mongo) throws Exception {
		super();
		this.label = Mongo.findNameById(idOffre, mongo);
		this.setData(co, mois, annee, idOffre);
	}
	public ArrayList<Double> getData() {
		return data;
	}
	public String getLabel() {
		return label;
	}
	
	public void setData(Connection co, String mois, String annee, String idOffre) throws Exception {
		this.data = new ArrayList<Double>();
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from v_statOffre where mois = ? and annee = ? and idOffre = ? order by jour";
		try {
			st = co.prepareStatement(sql);
			st.setString(1, mois);
			st.setString(2, annee);
			st.setString(3, idOffre);
			result = st.executeQuery();
			while(result.next()) this.data.add(result.getDouble("somme"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
	}
}

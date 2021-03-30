package base;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import function.Function;

public class SoldeOffre {
	int id;
	int idClient;
	int idOffre;
	Date fin;
	public SoldeOffre(int id, int idClient, int idOffre, Date fin) {
		super();
		this.id = id;
		this.idClient = idClient;
		this.idOffre = idOffre;
		this.fin = fin;
	}
	
	public SoldeOffre() {
		super();
	}

	
	
	public static ArrayList<SoldeOffre> soldeValide(Connection co, Date debut, String idClient) throws Exception {
		ArrayList<SoldeOffre> array = new ArrayList<SoldeOffre>();
//		Connection co = null;
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from achatOffre where debut <= ? and fin >= ? and idClient = " + idClient;
		try {
			co = Function.getConnect();
			st = co.prepareStatement(sql);
			st.setDate(1, debut);
			st.setDate(2, debut);
			result = st.executeQuery();
			if(result.next()) array.add(new SoldeOffre(result.getInt("id"), result.getInt("idClient"), result.getInt("idOffre"), result.getDate("fin")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
//			if(co != null) co.close();
		}
		return array;
	}
	
	public DetailsSoldeOffre getDetails(Connection co, int produit, String operateur) throws Exception {
		DetailsSoldeOffre array = null;
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from soldeOffre where idAchat = ? and produit = ?";
		try {
			co = Function.getConnect();
			st = co.prepareStatement(sql);
			st.setInt(1, id);
			st.setInt(2, produit);
			result = st.executeQuery();
			if(result.next()) array = new DetailsSoldeOffre(result.getInt("idAchat"), result.getInt("produit"), result.getInt("valeur"), result.getString("unite"), result.getDouble(operateur), result.getDate("delai"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
//			if(co != null) co.close();
		}
		return array;
	}
	
	
}

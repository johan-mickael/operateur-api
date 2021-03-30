package base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import function.Function;

public class TypeOffre {
	String idOffre;
	String nom;
	public TypeOffre(String idOffre, String nom) {
		super();
		this.idOffre = idOffre;
		this.nom = nom;
	}
	public TypeOffre() {
		super();
	}
	public String getIdOffre() {
		return idOffre;
	}
	public String getNom() {
		return nom;
	}
	
	public static ArrayList<TypeOffre> getData() throws Exception {
		ArrayList<TypeOffre> array = new ArrayList<TypeOffre>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet result = null;
		try {
			co = Function.getConnect();
			String sql = "select * from typeOffre";
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) array.add(new TypeOffre(result.getInt("id")+"", result.getString("types")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null)st.close();
			if(co != null) co.close();
		}
		return array;
	}
}

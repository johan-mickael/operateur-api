package function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Function {
	public static Connection getConnect() throws Exception {
		Class.forName("org.postgresql.Driver");
//        Connection co = DriverManager
//           .getConnection("jdbc:postgresql://ec2-54-211-176-156.compute-1.amazonaws.com:5432/dfipgrpro24pi4",
//           "kmfflyzndyujjm", "4964e91fd56ca6b8c4f050f543c244e78def5114ba39b77373ea2648516d3be3");
			Connection co = DriverManager.getConnection("jdbc:postgresql://localhost:5432/s5final", "postgres", "root");
        co.setAutoCommit(false);
		return co;
	}	
	
	public static int getProduit(String produit) {
		if(produit.compareToIgnoreCase("appel") == 0) return 1;
		if(produit.compareToIgnoreCase("message") == 0) return 2;
		return 3;
	}
	
	public static Integer nextVal(Connection co) throws Exception {
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "select nextval('idOffre') as next";
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) return result.getInt("next");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
		return null;
	}
	
	public static ArrayList<String> getProduits() throws Exception {
		ArrayList<String> array = new ArrayList<String>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet result = null;
		try {
			co = Function.getConnect();
			String sql = "SELECT nom from produits";
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next())  array.add(result.getString("nom"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(result != null) result.close();
			if(st  != null) st.close();
			if(co != null) co.close();
		}
		return array;
	}
}

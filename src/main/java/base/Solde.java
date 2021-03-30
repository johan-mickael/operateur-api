package base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Solde {
	int idClient;
	Double credit;
	Double mobilemoney;
	public Solde(int idClient, Double credit, Double mobilemoney) {
		super();
		this.idClient = idClient;
		this.credit = credit;
		this.mobilemoney = mobilemoney;
	}
	public Solde(int idClient, Connection co) throws Exception {
		super();
		this.idClient = idClient;
		PreparedStatement st = null;
		ResultSet result = null;
		try {
//			co = Function.getConnect();
			String sql = "SELECT * from solde where idClient = " + this.idClient;
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				this.credit = result.getDouble("credit");
				this.mobilemoney = result.getDouble("mobilemoney");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
//			if(co != null) co.close();
		}
	}
	
	public void update(Connection co, String methode, Double solde) throws SQLException {
		PreparedStatement st = null;
		String sql = "UPDATE solde set "+methode+" = ? where idClient = ?";
		try {
			st = co.prepareStatement(sql);
//			st.setString(1, methode);
			st.setDouble(1, solde);
			st.setInt(2, this.idClient);
			System.out.println("ST ********** " + st);
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(st != null) st.close();
		}
		
	}
}

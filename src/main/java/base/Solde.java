package base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import function.Function;
import function.Response;

@SuppressWarnings("serial")
public class Solde  implements Serializable {
	int idClient;
	Double credit;
	Double mobilemoney;
	
	public Solde() {
		super();
	}
	
	
	public int getIdClient() {
		return idClient;
	}


	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}


	public Double getCredit() {
		return credit;
	}


	public void setCredit(Double credit) {
		this.credit = credit;
	}


	public Double getMobilemoney() {
		return mobilemoney;
	}


	public void setMobilemoney(Double mobilemoney) {
		this.mobilemoney = mobilemoney;
	}


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
		}
	}
	
	public static Response achatCredit(String token, Double montant) {
		Response response = null;
		Connection co = null;
		try {
			co = Function.getConnect();
			response = Login.getIdUserTokenRequired(co, token, Login.table2);
			if(response.data != null) {
				String id = (String) response.data;
				int idClient = Integer.parseInt(id);
				Solde solde = new Solde(idClient, co);
				if(solde.mobilemoney >= montant) {
					solde.credit += montant;
					solde.mobilemoney -= montant;
					solde.update(co, "credit", solde.credit);
					solde.update(co, "mobileMoney", solde.mobilemoney);
					response = new Response("200", "Achat reussi", solde);
				} else {
					response = new Response("204", "Solde insuffisant", solde);
				}
				co.commit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				co.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				response = new Response("400", e1.toString());
				e1.printStackTrace();
			}
			response = new Response("400", e.toString());
			e.printStackTrace();
		} finally {
			if(co != null)
				try {
					co.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					response = new Response("400", e.toString());
					e.printStackTrace();
				}
		}
		return response;
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
	
	public static Response getSolde(String token)  {
		Response response = null;
		Connection co = null;
		try {
			co = Function.getConnect();
			response = Login.getIdUserTokenRequired(co, token, Login.table2);
			if(response.data != null) {
				String id = (String) response.data;
				Solde solde = new Solde(Integer.parseInt(id), co);
				response = new Response("200", "Solde ok", solde);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			if(co != null)
				try {
					co.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response = new Response("400", e.toString());
				}
		}
		return response;
	}
}

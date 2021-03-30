package base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import function.Function;
import function.Response;

@SuppressWarnings("serial")
public class Tarif implements Serializable {
	int id;
	String operateur;
	String produit;
	Double prix;
	
	public Tarif() {
		super();
	}



	public Tarif(int id, String operateur, String produit, Double prix) {
		super();
		this.setId(id);
		this.setOperateur(operateur);
		this.setProduit(produit);
		this.setPrix(prix);
	}
	
	
	
	public Tarif(int id, Double prix) {
		super();
		this.setId(id);
		this.setPrix(prix);
	}



	public int getId() {
		return id;
	}


	public String getOperateur() {
		return operateur;
	}


	public String getProduit() {
		return produit;
	}


	public Double getPrix() {
//		DecimalFormat df = new DecimalFormat("###.##");
//		return Double.parseDouble(df.format(this.prix)) ;
		return this.prix;
	}


	public void setId(int id) {
		this.id = id;
	}



	public void setOperateur(String operateur) {
		this.operateur = operateur;
	}



	public void setProduit(String produit) {
		this.produit = produit;
	}



	public void setPrix(Double prix) {
		this.prix = prix;
	}



	public void update(Connection co) throws Exception {
		PreparedStatement st = null;
		try {
			String sql = "Update tarifs set prix = ? where id = ?";
			st = co.prepareStatement(sql);
			st.setDouble(1, this.prix);
			st.setInt(2, this.id);
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (st != null) st.close();
		}
	}
	
	public Response update(String token) {
		Response res;
		Connection co = null;
		Tarif tarif = null;
		try {
			co = Function.getConnect();
			res = Login.tokenRequired(co, token, Login.table1);
			if(res == null) {
				this.update(co);
				res = Tarif.getById(this.id+"", token);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = new Response("400", e.toString());
		} finally {
				try {
					if (co != null) co.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					res = new Response("500", e.toString());
				}
		}
		return res;
	}
	
	public static Response getData(String token) {
		Response res; 
		ArrayList<Tarif> array = new ArrayList<Tarif>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet result = null;
		try {
			co = Function.getConnect();
			res = Login.tokenRequired(co, token, Login.table1);
			if(res == null) {
				String sql = "select * from v_tarif";
				st = co.prepareStatement(sql);
				result = st.executeQuery();
				while(result.next()) array.add(new Tarif(result.getInt("id"), result.getString("operateurs"), result.getString("produits"), Double.valueOf(result.getDouble("prix"))));
				res = new Response("200", "Get Tarif OK", array);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = new Response("400", e.toString());
		} finally {
				try {
					if (result != null) result.close();
					if (st != null) st.close();
					if (co != null) co.close();
				} catch (SQLException e) {
					e.printStackTrace();
					res = new Response("500", e.toString());
				}
		}
		return res;
	}
	
	public static Response getById(String id, String token) {
		Response res;
		Tarif tarif = null;
		PreparedStatement st = null;
		ResultSet result = null;
		Connection co = null;
		try {
			co = Function.getConnect();
			res = Login.tokenRequired(co, token, Login.table1);
			if(res == null) {
				String sql = "select * from v_tarif where id = " + id;
				st = co.prepareStatement(sql);
				result = st.executeQuery();
				while(result.next()) tarif = new Tarif(result.getInt("id"), result.getString("operateurs"), result.getString("produits"), result.getDouble("prix"));
				res = new Response("200", "modification tarif reussie ", tarif);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = new Response("400", e.toString());
		} finally {
				try {
					if (result != null) result.close();
					if (st != null) st.close();
					if (co != null) co.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					res = new Response("500", e.toString());
				}
		}
		return res;
	}
}

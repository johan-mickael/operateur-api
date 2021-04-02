package base.vue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import base.Login;
import base.MobileMoney;
import base.Solde;
import function.Function;
import function.Response;
import helper.Helper;

public class DepotNonValide {
	private Integer id;
	private String nom;
	private String numero;
	private String cin;
	private BigDecimal valeur;
	private String dateMobileMoney;
	private Boolean estValidee;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public BigDecimal getValeur() {
		return valeur;
	}
	public void setValeur(BigDecimal valeur) {
		this.valeur = valeur;
	}
	public String getDateMobileMoney() {
		return dateMobileMoney;
	}
	public void setDateMobileMoney(String dateMobileMoney) {
		this.dateMobileMoney = dateMobileMoney;
	}
	public Boolean getEstValidee() {
		return estValidee;
	}
	public void setEstValidee(Boolean estValidee) {
		this.estValidee = estValidee;
	}
	public DepotNonValide(Integer id, String nom, String numero, String cin, BigDecimal valeur, String dateMobileMoney,
			Boolean estValidee) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setValeur(valeur);
		this.setDateMobileMoney(dateMobileMoney);
		this.setEstValidee(estValidee);
	}
	public DepotNonValide(String nom, String numero, String cin, BigDecimal valeur, String dateMobileMoney, Boolean estValidee) {
		super();
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setValeur(valeur);
		this.setDateMobileMoney(dateMobileMoney);
		this.setEstValidee(estValidee);
	}
	public DepotNonValide(Integer id) {
		super();
		this.id = id;
	}	
	public DepotNonValide() {
		super();
	}
	
	public static Response get(String token) {
		Response res = null;
		ArrayList<DepotNonValide> array = new ArrayList<DepotNonValide>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			res = Login.tokenRequired(co, token, Login.table1);
			if(res == null) {
				String sql = "select * from v_depot_non_valide order by dateMobileMoney asc";
				st = co.prepareStatement(sql);
				rs = st.executeQuery();
				while(rs.next()) {
					Integer id = rs.getInt("id");
					String nom = rs.getString("nom");
					String numero = rs.getString("numero");
					String cin = rs.getString("cin");
					BigDecimal valeur = rs.getBigDecimal("valeur");
					String strDate = rs.getString("dateMobileMoney");
					Date dateMobileMoney = Helper.SQLDateToJavaDate(strDate);
					strDate = Helper.prettyDate(dateMobileMoney, false);
					Boolean estValidee = rs.getBoolean("estValidee");
					DepotNonValide temp = new DepotNonValide(id, nom, numero, cin, valeur, strDate, estValidee);
					array.add(temp);
				}
				res = new Response("200", "Get Depot non valide OK", array);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = new Response("400", e.toString());
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				res = new Response("400", ex.toString());
			}
		}
		return res;
	}
	
	public static ArrayList<String> getIdClient(Connection co, String id) throws Exception {
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<String> array = new ArrayList<String>();
		String sql = "select * from mobilemoney where id = " +id;
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			result.next();
			array.add(result.getString("idClient"));
			array.add(result.getString("valeur"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
		return array;
				
	}
	
	public static Response update(DepotNonValide inputDepot, String token) {
		Response res =  null;
		Connection co = null;
		PreparedStatement st = null;
		try {
			co = Function.getConnect();
			res = Login.tokenRequired(co, token, Login.table1);
			if(res == null) {
				String sql = "update MobileMoney set estValidee = true where id = ?";
				st = co.prepareStatement(sql);
				st.setInt(1, inputDepot.id);
				st.execute();
				res = new Response("200", "Validation mobile money réussie!", inputDepot);
				ArrayList<String> array = (ArrayList<String>) DepotNonValide.getIdClient(co, inputDepot.id + "");
				Solde solde = new Solde(Integer.parseInt(array.get(0)), co);
				solde.update(co, "mobilemoney", solde.getMobilemoney() + Double.parseDouble(array.get(1)));
				co.commit();
			}
		} catch(Exception ex) {
			try {
				co.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				res = new Response("500", ex.toString());
			}
			ex.printStackTrace();
			res = new Response("400", ex.toString());
		} finally {
			try {
				if(st != null) st.close();
				if(co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				res = new Response("400", ex.toString());
			}
		}
		return res;
	}
	
	public Response update(String token) {
		return DepotNonValide.update(this, token);
	}
	
	
}

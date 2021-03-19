package base.vue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import base.MobileMoney;
import function.Function;
import function.Response;
import helper.Helper;

public class DepotNonValide {
	private Integer id;
	private String nom;
	private String numero;
	private BigDecimal valeur;
	private Date dateMobileMoney;
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
	public BigDecimal getValeur() {
		return valeur;
	}
	public void setValeur(BigDecimal valeur) {
		this.valeur = valeur;
	}
	public Date getDateMobileMoney() {
		return dateMobileMoney;
	}
	public void setDateMobileMoney(Date dateMobileMoney) {
		this.dateMobileMoney = dateMobileMoney;
	}
	public Boolean getEstValidee() {
		return estValidee;
	}
	public void setEstValidee(Boolean estValidee) {
		this.estValidee = estValidee;
	}
	public DepotNonValide(Integer id, String nom, String numero, BigDecimal valeur, Date dateMobileMoney,
			Boolean estValidee) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setNumero(numero);
		this.setValeur(valeur);
		this.setDateMobileMoney(dateMobileMoney);
		this.setEstValidee(estValidee);
	}
	public DepotNonValide(String nom, String numero, BigDecimal valeur, Date dateMobileMoney, Boolean estValidee) {
		super();
		this.setNom(nom);
		this.setNumero(numero);
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
	
	public static Response get() {
		Response res = new Response();
		ArrayList<DepotNonValide> array = new ArrayList<DepotNonValide>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from v_depot_non_valide";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			while(rs.next()) {
				Integer id = rs.getInt("id");
				String nom = rs.getString("nom");
				String numero = rs.getString("numero");
				BigDecimal valeur = rs.getBigDecimal("valeur");
				Date dateMobileMoney = rs.getDate("dateMobileMoney");
				Boolean estValidee = rs.getBoolean("estValidee");
				DepotNonValide temp = new DepotNonValide(id, nom, numero, valeur, dateMobileMoney, estValidee);
				array.add(temp);
			}
			res = new Response("200", "Get Depot non valide OK", array);
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
	
	public static Response update(DepotNonValide inputDepot) {
		Response res = new Response();
		Connection co = null;
		PreparedStatement st = null;
		try {
			co = Function.getConnect();
			String sql = "update MobileMoney set estValidee = true where id = ?";
			st = co.prepareStatement(sql);
			st.setInt(1, inputDepot.id);
			st.execute();
			res = new Response("200", "Update Mobile Money OK", inputDepot);
		} catch(Exception ex) {
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
	
	public Response update() {
		return DepotNonValide.update(this);
	}
	
	
}

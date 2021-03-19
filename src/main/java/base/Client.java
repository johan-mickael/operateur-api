package base;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import function.Function;
import function.Response;

public class Client {
	private Integer id;
	private String nom;
	private String numero;
	private String cin;
	private String codeSecret;
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
	public String getCodeSecret() {
		return codeSecret;
	}
	public void setCodeSecret(String codeSecret) {
		this.codeSecret = codeSecret;
	}
	public Client(Integer id, String nom, String numero, String cin, String codeSecret) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setCodeSecret(codeSecret);
	}
	public Client(String nom, String numero, String cin, String codeSecret) {
		super();
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setCodeSecret(codeSecret);
	}
	
	public static Response get() {
		Response res = new Response();
		ArrayList<Client> array = new ArrayList<Client>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from client";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			while(rs.next()) {
				Integer id = rs.getInt("id");
				String nom = rs.getString("nom");
				String numero = rs.getString("numero");
				String cin = rs.getString("cin");
				String codeSecret = rs.getString("codeSecret");
				Client temp = new Client(id, nom, numero, cin, codeSecret);
				array.add(temp);
			}
			res = new Response("200", "Get Client OK", array);
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
	
	public Response depot(BigDecimal val) {
		return new 	MobileMoney(this.id, val) 
					.post();
	}
	
}

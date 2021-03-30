package base;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import function.Function;
import function.Response;
import helper.Helper;
import helper.NumeroHelper;

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
	public void setNumero(String numero) throws Exception {
		new NumeroHelper(numero);
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
	public Client(Integer id, String nom, String numero, String cin, String codeSecret) throws Exception {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setCodeSecret(codeSecret);
	}
	public Client(String nom, String numero, String cin, String codeSecret) throws Exception {
		super();
		this.setNom(nom);
		this.setNumero(numero);
		this.setCin(cin);
		this.setCodeSecret(codeSecret);
	}
	public Client(String numero, String codeSecret) throws Exception {
		super();
		this.setNumero(numero);
		this.setCodeSecret(codeSecret);
	}
	public Client() {
		super();
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
	
	public static Client get(String numero, String codesecret) throws Exception {
		Client ret = null;
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from client";
			sql += " where numero = '" + numero + "'";
			sql += " and codesecret = '"+ codesecret + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				Integer id = rs.getInt("id");
				String nom = rs.getString("nom");
				String num = rs.getString("numero");
				String cin = rs.getString("cin");
				String code = rs.getString("codesecret");
				ret = new Client(id, nom, num, cin, code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return ret;
	}
	
	public static Login generateLogin(Connection co, Client client) throws Exception {
		Integer id = Helper.getNextId(co, Login.table1);
		String token = Helper.getMD5Hash(Login.generateTokenClient(client));
		Date expiration = new Date();
		expiration = Helper.addMinute(expiration, Login.getDelaiExpiration());
		return new Login(id, client.id, token, expiration);
	}
	
	public static Login getLogin(Connection co, Client client) throws Exception {
		PreparedStatement st = null;
		Login ret = new Login (client.id);
		try {
			Login isLogged = ret.getLoginConnecte(co, Login.table2);
			if(isLogged != null) 
				return isLogged;
			ret = generateLogin(co, client);
			ret.post(co, Login.table2);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if(st != null) st.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return ret;
	}
	
	public Login getLogin(Connection co) throws Exception {
		return Client.getLogin(co, this);
	}
	
	public static Login login(Connection co, String numero, String codesecret) throws Exception {
		codesecret = Helper.getMD5Hash(codesecret);
		Client client = Client.get(numero, codesecret);
		if(client != null) return client.getLogin(co);
		throw new Exception("numero ou codesecret invalide");
	}
	
	public static Login login(Connection co, Client client) throws Exception {
		return Client.login(co, client.numero, client.codeSecret);
	}
	
	public static Response login(Client client) {
		Connection co = null;
		Response ret = null;
		try {
			co = Function.getConnect();
			Login login = Client.login(co, client);
			ret = new Response("200", "Login OK", login);
		} catch(Exception ex) {
			ex.printStackTrace();
			ret = new Response("401", ex.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				ret = new Response("500", ex.toString());
			}
		}
		return ret;
	}
	
	public Response login() {
		return Client.login(this);
	}
	
	
	public Client(Connection co, Integer id) throws Exception {
		this.id = id;
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from client";
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				this.cin = result.getString("cin");
				this.nom = result.getString("nom");
				this.numero = result.getString("numero");
				this.codeSecret =  result.getString("codeSecret");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
		
	}
	public static Response getSoldeMobileMoney(String numero) {
		Response res = null;
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from v_somme_solde_mobile_money";
			sql += " where numero = '" + numero + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			Double solde = null;
			if(rs.next()) {
				solde = rs.getDouble("valeur");
			}
			res = new Response("200", "Get solde mobile money OK", solde);
		} catch(Exception ex)  {
			ex.printStackTrace();
			res = new Response("401", ex.toString());
		} finally {
			try {
				if(rs != null) rs.close();
				if(st != null) st.close();
				if(co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				res = new Response("500", ex.toString());
			}
		}
		return res;
	}
	
	public static Response getSoldeCredit(String numero) {
		Response res = null;
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from v_somme_solde_credit";
			sql += " where numero = '" + numero + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			Double solde = null;
			if(rs.next()) {
				solde = rs.getDouble("valeur");
			}
			res = new Response("200", "Get solde credit OK", solde);
		} catch(Exception ex)  {
			ex.printStackTrace();
			res = new Response("401", ex.toString());
		} finally {
			try {
				if(rs != null) rs.close();
				if(st != null) st.close();
				if(co != null) co.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				res = new Response("500", ex.toString());
			}
		}
		return res;
	}
	
	public static Response getSoldeMobileMoney(Client client) {
		return Client.getSoldeMobileMoney(client.numero);
	}
	
	public Response getSoldeMobileMoney() {
		return Client.getSoldeMobileMoney(this);
	}
	
	public static Response getSoldeCredit(Client client) {
		return Client.getSoldeCredit(client.numero);
	}
	
	public Response getSoldeCredit() {
		return Client.getSoldeCredit(this);
	}
	
//	public Response retraitMobileMoney(String num, Double val) {
//		Response res = null;
//		Double soldeMobilemoney = (double) Client.getSoldeMobileMoney(num).data;
//		if(val > soldeMobilemoney)
//			return new Response("210", "Solde insuffisant");
//		
//	}
}

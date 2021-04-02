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
//		new NumeroHelper(numero);
		
		this.numero = numero.trim();
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
	public Client(String nom, String cin, String codeSecret) throws Exception {
		super();
		this.setNom(nom);
		this.setCin(cin);
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
			System.out.println(sql);
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
			ArrayList<Object> array = new ArrayList<Object>();
			Login login = Client.login(co, client);
			array.add(login);
			Client cl = new Client(co, client.numero);
			array.add(cl);
			ret = new Response("200", "Login OK", array);
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
		String sql = "SELECT * from client where id = " + this.id;
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
	
	public Client(Connection co, String numero) throws Exception {
		this.numero = numero;
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT * from client where numero = '" + this.numero + "'";
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				this.cin = result.getString("cin");
				this.nom = result.getString("nom");
				this.id = result.getInt("id");
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
	
	public static String getNouveauNumero(Connection co) throws Exception {
		String num = NumeroHelper.genererNumero();
		while(true) {
			if(NumeroHelper.verifierNouveauNumero(co, num)) {
				break;
			} else {
				num = NumeroHelper.genererNumero();
			}
		}
		return num;
	}
	
	public void setNouveauNumero(Connection co) throws Exception {
		this.numero = Client.getNouveauNumero(co);
	}
	
	public static void post(Connection co, Client client) throws Exception {
		PreparedStatement st = null;
		String sql = "insert into client (id, nom, numero, cin, codesecret) values (?, ?, ?, ?, ?)";
		try {
			st = co.prepareStatement(sql);
			Integer id = Helper.getNextId(co, "client");
			st.setInt(1, id);
			st.setString(2, client.getNom());
			st.setString(3, client.getNumero());
			st.setString(4, client.getCin());
			st.setString(5, Helper.getMD5Hash(client.getCodeSecret()));
			st.execute();
			co.commit();
		} catch(Exception ex) {
			co.rollback();
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
	}
	
	public void post(Connection co) throws Exception {
		Client.post(co, this);
	}
	
	public static void inscription(Connection co, Client client) throws Exception {
		client.setNouveauNumero(co);
		client.post(co);
	}
	
	public void inscription(Connection co) throws Exception {
		Client.inscription(co, this);
	}
	
	public Response inscription() {
		Response res = null;
		Connection co = null;
		try {
			co = Function.getConnect();
			inscription(co);
			String message = "Inscription effectué avec succes! votre numero : " + this.getNumero();
			res = new Response("200", message);
		} catch (Exception e) {
			e.printStackTrace();
			res = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch (Exception e) {
				e.printStackTrace();
				res = new Response("500", e.toString());
			}
		}
		return res;
	}
	
	public static Response getClientByToken(String token) {
		Connection co = null;
		Response res = null;
		try {
			co = Function.getConnect();
			res = Login.getIdUserTokenRequired(co, token, Login.table2);
			if(res.code.compareTo("200")==0) {
				Integer id = Integer.parseInt((String)res.data);
				Client c = new Client(co, id);
				res = new Response("200", "Get Client ok", c);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			res = new Response("400", ex.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException ex) {	
				ex.printStackTrace();
				res = new Response("500", ex.toString());
			}
		}
		return res;
	}

}

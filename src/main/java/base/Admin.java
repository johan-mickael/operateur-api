package base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import function.Function;
import function.Response;
import helper.Helper;

public class Admin {
	private Integer id;
	private String nom;
	private String identifiant;
	private String mdp;
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
	public String getIdentifiant() {
		return identifiant;
	}
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public Admin() {
		super();
	}
	public Admin(Integer id, String nom, String identifiant, String mdp) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setIdentifiant(identifiant);
		this.setMdp(mdp);
	}
	public Admin(String identifiant, String mdp) {
		super();
		this.setIdentifiant(identifiant);
		this.setMdp(mdp);
	}
	
	public static Admin get(String identifiant, String mdp) throws Exception {
		Admin ret = null;
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from admin";
			sql += " where identifiant = '" + identifiant + "'";
			sql += " and mdp = '"+ mdp + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				Integer id = rs.getInt("id");
				String nom = rs.getString("nom");
				String ident = rs.getString("identifiant");
				String pass = rs.getString("mdp");
				ret = new Admin(id, nom, ident, pass);
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
	
	public static Login generateLogin(Connection co, Admin admin) throws Exception {
		Integer id = Helper.getNextId(co, Login.table1);
		String token = Helper.getMD5Hash(Login.generateTokenAdmin(admin));
		Date expiration = new Date();
		expiration = Helper.addMinute(expiration, Login.getDelaiExpiration());
		return new Login(id, admin.id, token, expiration);
	}
	
	public static Login getLogin(Connection co, Admin admin) throws Exception {
		PreparedStatement st = null;
		Login ret = new Login (admin.id);
		try {
			Login isLogged = ret.getLoginConnecte(co, Login.table1);
			if(isLogged != null) 
				return isLogged;
			ret = generateLogin(co, admin);
			ret.post(co, Login.table1);
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
		return Admin.getLogin(co, this);
	}
	
	public static Login login(Connection co, String identifiant, String mdp) throws Exception {
		mdp = Helper.getMD5Hash(mdp);
		Admin admin = Admin.get(identifiant, mdp);
		if(admin != null) return admin.getLogin(co);
		throw new Exception("identifiant ou mot de passe invalide");
	}
	
	public static Login login(Connection co, Admin admin) throws Exception {
		return Admin.login(co, admin.identifiant, admin.mdp);
	}
	
	public static Response login(Admin admin) {
		Connection co = null;
		Response ret = null;
		try {
			co = Function.getConnect();
			Login login = Admin.login(co, admin);
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
		return Admin.login(this);
	}
}

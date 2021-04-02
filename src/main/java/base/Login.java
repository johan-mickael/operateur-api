package base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import function.Response;
import helper.Helper;

public class Login {
	private Integer id;
	private Integer user;
	private String token;
	private Date expiration;
	
	private static final Integer delai_expiration = 30;
	
	public static final String table1 = "login";
	public static final String table2 = "loginClient";
	public static final String mode1 = "admin";
	public static final String mode2 = "client";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public static Integer getDelaiExpiration() {
		return delai_expiration;
	}
	public Login() {
		super();
	}
	public Login(Integer id, Integer admin, String token, Date expiration) {
		super();
		this.setId(id);
		this.setUser(admin);
		this.setToken(token);
		this.setExpiration(expiration);
	}
	public Login(Integer admin, String token, Date expiration) {
		super();
		this.setUser(admin);
		this.setToken(token);
		this.setExpiration(expiration);
	}	
	public Login(Integer admin) {
		super();
		this.setUser(admin);
	}
	public static String generateTokenAdmin(Admin admin) {
		Date now = new Date();
		String str = now.toString() + admin.toString();
		return Helper.getMD5Hash(str);
	}
	public static String generateTokenClient(Client client) {
		Date now = new Date();
		String str = now.toString() + client.toString();
		return Helper.getMD5Hash(str);
	}
	
	public static Login post(Connection co, Login login, String table) throws Exception {
		PreparedStatement st = null;
		try {
			String sql = "insert into "+table+" values(?, ?, ?, ?)";
			st = co.prepareStatement(sql);
			Integer id = Helper.getNextId(co, table);
			st.setInt(1, id);
			st.setInt(2, login.user);
			st.setString(3, login.token);
			st.setTimestamp(4, new java.sql.Timestamp(login.expiration.getTime()));
			st.execute();
			co.commit();
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
		return login;
	}
	
	public Login post(Connection co, String table) throws Exception {
		return Login.post(co, this, table);
	}
	
	public static Login getLoginConnecte(Connection co, Login login, String table) throws Exception {
		Login ret = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "select * from "+table;
			String mode = mode1;
			if(table.compareTo(table2) == 0)
				mode = mode2;
			sql += " where " + mode + "=" + login.user;
			String now = Helper.dateToString(new Date());
			sql += " and expiration > '" + now + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				Integer id = rs.getInt("id");
				Integer admin = rs.getInt(mode);
				String token =  rs.getString("token");
				Date expiration = rs.getDate("expiration");
				ret = new Login(id, admin, token, expiration);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return ret;
	}
	
	public Login getLoginConnecte(Connection co, String table) throws Exception {
		return Login.getLoginConnecte(co, this, table);
	}
	
	public static Boolean tokenEstValide(Connection co, String token, String table) throws Exception {
		Boolean ret = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "select * from "+table;
			sql += " where token = '" + token + "'";
			String now = Helper.dateToString(new Date());
			sql += " and expiration > '" + now + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return ret;
	}
	
	public static String getIdUserTokenValide(Connection co, String token, String table) throws Exception {
		String ret = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String column = (table.compareTo(table1)==0)? mode1: mode2;
			String sql = "select "+column+" from "+table;
			sql += " where token = '" + token + "'";
			String now = Helper.dateToString(new Date());
			sql += " and expiration > '" + now + "'";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				ret = rs.getString(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return ret;
	}
	
	
	public static Response tokenRequired(Connection co, String token, String table) {
		try {
			Boolean tokenValide = Login.tokenEstValide(co, token, table);
			if(!tokenValide) {
				return new Response("403", "Veuillez vous connecter");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return new Response("500", e1.toString());
		}
		return null;
	}
	
	public static Response getIdUserTokenRequired(Connection co, String token, String table) {
		if(token.split(" ").length > 1) {
			token = token.split(" ")[1];
		}
		Response res = null;
		String id = null;
		try {
			id = Login.getIdUserTokenValide(co, token, table);
			if(id == null) {
				res = new Response("403", "Veuillez vous connecter");
			} else {
				res = new Response("200", "getIdUserTokeRequired", id);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			res = new Response("500", e1.toString());
		}
		return res;
	}
}

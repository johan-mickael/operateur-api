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

public class MobileMoney {
	private Integer id;
	private Integer idClient;
	private BigDecimal valeur;
	private Date dateMobileMoney;
	private Boolean estValidee;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdClient() {
		return idClient;
	}
	public void setIdClient(Integer idClient) {
		this.idClient = idClient;
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
	public MobileMoney(Integer id, Integer idClient, BigDecimal valeur, Date dateMobileMoney, Boolean estValidee) {
		super();
		this.setId(id);
		this.setIdClient(idClient);
		this.setValeur(valeur);
		this.setDateMobileMoney(dateMobileMoney);
		this.setEstValidee(estValidee);
	}
	public MobileMoney(Integer idClient, BigDecimal valeur, Date dateMobileMoney, Boolean estValidee) {
		super();
		this.setIdClient(idClient);
		this.setValeur(valeur);
		this.setDateMobileMoney(dateMobileMoney);
		this.setEstValidee(estValidee);
	}
	public MobileMoney(Integer idClient, BigDecimal valeur) {
		super();
		this.setIdClient(idClient);
		this.setValeur(valeur);
	}
	public MobileMoney(BigDecimal valeur) {
		super();
		this.setValeur(valeur);
	}
	public MobileMoney() {
		super();
	}
	
	public static Response get() {
		Response res = new Response();
		ArrayList<MobileMoney> array = new ArrayList<MobileMoney>();
		Connection co = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			co = Function.getConnect();
			String sql = "select * from MobileMoney";
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			while(rs.next()) {
				Integer id = rs.getInt("id");
				Integer idClient = rs.getInt("idClient");
				BigDecimal valeur = rs.getBigDecimal("valeur");
				Date dateMobileMoney = rs.getDate("dateMobileMoney");
				Boolean estValidee = rs.getBoolean("estValidee");
				MobileMoney temp = new MobileMoney(id, idClient, valeur, dateMobileMoney, estValidee);
				array.add(temp);
			}
			res = new Response("200", "Get Mobile Money OK", array);
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
	
	public static Response post(MobileMoney inputMobileMoney) {
		Response res = new Response();
		Connection co = null;
		PreparedStatement st = null;
		try {
			co = Function.getConnect();
			String sql = "insert into MobileMoney values(?, ?, ?)";
			st = co.prepareStatement(sql);
			Integer id = Helper.getNextId(co, "MobileMoney");
			st.setInt(1, id);
			st.setInt(2, inputMobileMoney.idClient);
			st.setBigDecimal(3, inputMobileMoney.valeur);
			System.out.println(st);
			st.execute();
			res = new Response("200", "Insertion Mobile Money OK", inputMobileMoney);
			co.commit();
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
	
	public Response post() {
		return MobileMoney.post(this);
	}
	
}

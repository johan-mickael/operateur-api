package helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
	public static String[] jours = {"dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"};
	public static String[] mois = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
	
	@SuppressWarnings("deprecation")
	public static String prettyDate(Date d, boolean sec) {
		String sp = " ";
		String day = jours[d.getDay()];
		String dd = d.getDate()+"";
		String MM = mois[d.getMonth()];
		String YYYY = (d.getYear() + 1900) + "";
		String hh = d.getHours()+"h";
		if(hh.length() == 2)
			hh = "0"+hh;
		String mm = d.getMinutes()+"m";
		if(mm.length() == 2)
			mm = "0"+mm;
		String s = d.getSeconds()+"s";
		String ret = day+sp+dd+sp+MM+sp+YYYY+sp+"à"+sp+hh+sp+mm;
		if(sec) ret += sp+s;
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	public static Date addMinute(Date date, Integer mn) {
		date.setMinutes(date.getMinutes() + mn);
		return date;
	}
	
	public static Integer getMaxId(Connection co, String table) throws Exception {
		Integer ret = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try { 
			String sql = "select max(id) id from "+table;
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				ret = rs.getInt("id");
			}
		} catch(Exception ex) {
			throw ex;
		}
		return ret;
	}
	
	public static Integer getNextId(Connection co, String table) throws Exception {
		Integer max = getMaxId(co, table);
		return (max == null)? 1: max + 1;
	}
	
	public static String dateToString(Date d) throws Exception {
		if(d == null)
			return null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String date = dateFormat.format(d) + ".0";
        return date; 
	}
	
	@SuppressWarnings("deprecation")
	public static Date SQLDateToJavaDate(String dt) throws Exception {
		if (dt == null)
			return null;
		String[] date = dt.split(" ");
		String[] date_date = date[0].split("-");
		String[] date_time = date[1].split(":");
		int year = Integer.parseInt(date_date[0]) - 1900;
		int month = Integer.parseInt(date_date[1]) - 1;
		int dateDay = Integer.parseInt(date_date[2]);
		int hour = Integer.parseInt(date_time[0]);
		int minute = Integer.parseInt(date_time[1]);
		String[] second = date_time[2].split("\\.");
		int sec = Integer.parseInt(second[0]);
		return new Date(year, month, dateDay, hour, minute, sec);
	}
	
	@SuppressWarnings("deprecation")
	public static Date HTMLDatetimeLocalToDate(String dt, String sep) throws Exception {
		try {
			String[] date = dt.split(sep);
			String[] date_date = date[0].split("-");
			String[] date_time = date[1].split(":");
			int year = Integer.parseInt(date_date[0]) - 1900;
			int month = Integer.parseInt(date_date[1]) - 1;
			int dateDay = Integer.parseInt(date_date[2]);
			int hour = Integer.parseInt(date_time[0]);
			int minute = Integer.parseInt(date_time[1]);
			return new Date(year, month, dateDay, hour, minute, 0);
		} catch(Exception ex) {
			throw new Exception("Date Invalide");
		}	
	}
	
	private static byte[] digest(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return md.digest(input);
	}
	
	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b: bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	public static String getMD5Hash(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
		byte[] digest = digest(bytes);
		return bytesToHex(digest);
	}
}
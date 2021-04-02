package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumeroHelper {
	private String numero;
	
	private static final String prefixe = "034";
	
	private static final int tailleNumero = 10;
	
	public static final String memeOperateur = "operateur";
	public static final String autreOperateur = "Autres";
	public static final String international = "International";

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) throws Exception {
		numero = numero.trim();
        String regex = "[0-9]+"; 
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(numero); 
        System.out.println(numero);
        if(m.find() && m.group().equals(numero)) 
        	this.numero = numero; 
        else
        	throw new Exception("Numero invalide");
	}

	public NumeroHelper(String numero) throws Exception {
		super();
		this.setNumero(numero);
	}
	
	public static boolean isInternational(String numero) {
		return numero.length() != tailleNumero;
	}
	public boolean isInternational() {
		return NumeroHelper.isInternational(this.numero);
	}
	
	public static String getPrefixe(String numero) {
		if(isInternational(numero))
			return null;
		return numero.substring(0, 3);
	}
	public String getPrefixe() {
		return NumeroHelper.getPrefixe(this.numero);
	}
	
	public static String getOperateur(String prefixe) {
		if(prefixe == null)
			return international;
		if(prefixe.compareTo(NumeroHelper.prefixe) == 0)
			return memeOperateur;
		return autreOperateur;
	}
	public String getOperateur() {
		String prefixe = this.getPrefixe();
		return NumeroHelper.getOperateur(prefixe);
	}
	
	public static String genererNumero() {
		String num = prefixe;
		int min = 0;
		int max = 9999999;
		String rand = (min + (int)(Math.random() * ((max + min) + 1))) + "";
		while(rand.length() < 7) {
			rand = "0" + rand;
		}
		return num + rand;
	}
	
	public static boolean verifierNouveauNumero(Connection co, String num) throws Exception {
		boolean ret = true;
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT numero from client where numero = '"+num+"'";
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				ret = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(result != null) result.close();
				if(st != null) st.close();
			} catch(SQLException ex) {	
				throw ex;
			}
		}
		return ret;
	}
	
}

package helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumeroHelper {
	private String numero;
	
	private static final String prefixe = "034";
	
	private static final int tailleNumero = 10;
	
	public static final String memeOperateur = "operateur";
	public static final String autreOperateur = "autre";
	public static final String international = "international";

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) throws Exception {
        String regex = "[0-9]+"; 
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(numero); 
        
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
	
	
}

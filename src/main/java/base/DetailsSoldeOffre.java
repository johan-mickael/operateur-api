package base;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailsSoldeOffre {
	int idAchat;
	int produit;
	int valeur;
	String unite;
	Double operateur;
	Double international;
	Double autres;
	Double solde;
	Date delai;
	public DetailsSoldeOffre(int idAchat, int produit, int valeur, String unite,
			Double operateur, Double international, Double autres, Date delai) {
		super();
		this.idAchat = idAchat;
		this.produit = produit;
		this.valeur = valeur;
		this.unite = unite;
		this.operateur = operateur;
		this.international = international;
		this.autres = autres;
		this.delai = delai;
	}
	
	
	
	public DetailsSoldeOffre(int idAchat, int produit, int valeur,
			String unite, 
			Double solde, Date delai) {
		super();
		this.idAchat = idAchat;
		this.produit = produit;
		this.valeur = valeur;
		this.unite = unite;
		this.solde = solde;
		this.delai = delai;
	}



	public int getIdAchat() {
		return idAchat;
	}
	public int getProduit() {
		return produit;
	}
	public int getValeur() {
		return valeur;
	}
	public String getUnite() {
		return unite;
	}
	public Double getOperateur() {
		return operateur;
	}
	public Double getInternational() {
		return international;
	}
	public Double getAutres() {
		return autres;
	}
	public Date getDelai() {
		return delai;
	}
	
	public void updateAppel(Connection co, String operateur, boolean ar) throws Exception {
		String sql = "";
		PreparedStatement st = null;
		try {
			if(ar) {
				sql = "UPDATE soldeOffre set valeur = ? where idAchat = ? and produit = 1";
				st = co.prepareStatement(sql);
				st.setInt(1, this.valeur);
				st.setInt(2, this.idAchat);
			} else {
				sql = "UPDATE soldeOffre set "+operateur+" = ? where idAchat = ? and produit = 1";
				st = co.prepareStatement(sql);
//				st.setString(1, operateur);
				st.setDouble(1, this.solde);
				st.setInt(2, this.idAchat);
			}
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(st != null) st.close();
		}
	}
}

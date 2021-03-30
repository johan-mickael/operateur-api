package base.mongo;

public class Forfait {
	String produit;
	String unite;
	Double valeur;
	Utilisation utilisation;
	public Forfait(String produit, String unite, Double valeur,
			Utilisation utilisation) {
		super();
		this.produit = produit;
		this.unite = unite;
		this.valeur = valeur;
		this.utilisation = utilisation;
	}
	public Forfait(String produit, Utilisation utilisation) {
		super();
		this.produit = produit;
		this.utilisation = utilisation;
	}
	
	
	
	public Forfait(String produit, String unite, Utilisation utilisation) {
		super();
		this.produit = produit;
		this.unite = unite;
		this.utilisation = utilisation;
	}
	public Forfait() {
		super();
	}
	public Forfait(String produit, String unite, Double valeur) {
		super();
		this.produit = produit;
		this.unite = unite;
		this.valeur = valeur;
	}
	public String getProduit() {
		return produit;
	}
	public void setProduit(String produit) {
		this.produit = produit;
	}
	public String getUnite() {
		return unite;
	}
	public void setUnite(String unite) {
		this.unite = unite;
	}
	public Double getValeur() {
		return valeur;
	}
	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}
	public Utilisation getUtilisation() {
		return utilisation;
	}
	public void setUtilisation(Utilisation utilisation) {
		this.utilisation = utilisation;
	}
	
	
}

package base.mongo;

public class Utilisation {
	String unite;
	Double operateur;
	Double autres;
	Double international;
	
	public Utilisation(String unite, Double operateur, Double autres,
			Double international) {
		super();
		this.unite = unite;
		this.operateur = operateur;
		this.autres = autres;
		this.international = international;
	}
	
	
	
	public Utilisation(Double operateur, Double autres, Double international) {
		super();
		this.operateur = operateur;
		this.autres = autres;
		this.international = international;
	}



	public Utilisation() {
		super();
	}

	public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite;
	}

	public Double getOperateur() {
		return operateur;
	}
	public void setOperateur(Double operateur) {
		this.operateur = operateur;
	}
	public Double getAutres() {
		return autres;
	}
	public void setAutres(Double autres) {
		this.autres = autres;
	}
	public Double getInternational() {
		return international;
	}
	public void setInternational(Double international) {
		this.international = international;
	}
	
	
}

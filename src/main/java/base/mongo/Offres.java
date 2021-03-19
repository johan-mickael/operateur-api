package base.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="offres")
public class Offres {
	@Id
	String id;
	String idOffre;
	String nom;
	Double tarif;
	Integer validite;
	Forfait[] forfaits;
	
	public Offres(String id, String idOffre, String nom, Double tarif,
			Integer validite, Forfait[] forfaits) {
		super();
		this.id = id;
		this.idOffre = idOffre;
		this.nom = nom;
		this.tarif = tarif;
		this.validite = validite;
		this.forfaits = forfaits;
	}
	
	
	
	public Offres() {
		super();
	}



	public Offres(String id) {
		super();
		this.idOffre = id;
		System.out.println(this.id);
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Double getTarif() {
		return tarif;
	}
	public void setTarif(Double tarif) {
		this.tarif = tarif;
	}
	public int getValidite() {
		return validite;
	}
	public void setValidite(int validite) {
		this.validite = validite;
	}
	public Forfait[] getForfaits() {
		return forfaits;
	}
	public void setForfaits(Forfait[] forfaits) {
		this.forfaits = forfaits;
	}
	
	
}

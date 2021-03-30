package base.mongo;

import java.util.ArrayList;

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
	ArrayList<Forfait> forfaits;
	
	public Offres(String id, String idOffre, String nom, Double tarif,
			Integer validite, ArrayList<Forfait> forfaits) {
		super();
		this.id = id;
		this.nom = nom;
		this.tarif = tarif;
		this.validite = validite;
		this.forfaits = forfaits;
		this.idOffre = idOffre;
	}
	
	
	
	public Offres(String id, String idOffre, String nom, Double tarif, Integer validite) {
		super();
		this.id = id;
		this.nom = nom;
		this.tarif = tarif;
		this.idOffre = idOffre;
		this.validite = validite;
		this.forfaits = new ArrayList<Forfait>();
	}



	public Offres() {
		super();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public ArrayList<Forfait> getForfaits() {
		return forfaits;
	}
	public void setForfaits(ArrayList<Forfait> forfaits) {
		this.forfaits = forfaits;
	}



	public String getIdOffre() {
		return idOffre;
	}



	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	
	
}

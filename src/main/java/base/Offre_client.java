package base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import base.mongo.Forfait;
import base.mongo.Offres;
import function.Function;
import function.Mongo;
import function.Response;

@SuppressWarnings("serial")
public class Offre_client implements Serializable{
	int id;
	int idClient;
	String idoffre;
	String methode;
	Date debut;
	Date fin;
	Offres offre;
	public Offre_client(int id, int idClient, String idoffre, String methode, Date debut,
			Date fin) {
		super();
		this.id = id;
		this.idClient = idClient;
		this.idoffre = idoffre;
		this.methode = methode;
		this.debut = debut;
		this.fin = fin;
	}
	public Offre_client(int idClient, String idoffre, Date debut, String methode) throws Exception {
		super();
		this.idClient = idClient;
		this.idoffre = idoffre;
		this.debut = debut;
		this.methode = methode;
		this.offre = Mongo.getById(this.idoffre);
		this.setFin();
	}
	
	
	
	public Offre_client() {
		super();
	}
	@SuppressWarnings("deprecation")
	public void setFin() {
		this.fin = (Date) this.debut.clone();
		this.fin.setDate(this.fin.getDate() + this.offre.getValidite());
	}
	
	public void insert(Connection co) throws Exception {
		PreparedStatement st = null;
		try {
			String sql = "INSERT INTO achatOffre (id, idClient, idOffre, debut, fin, methode) values (nextval('achat'), ?, ?, ?, ?, ?) ";
			st = co.prepareStatement(sql);
			st.setInt(1, this.idClient);
			st.setString(2, this.idoffre);
			st.setDate(3, this.debut);
			st.setDate(4, this.fin);
			st.setString(5, this.methode);
			st.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(st != null) st.close();
		}
	}
	
	public boolean verifier_solde(Connection co, String methode) throws Exception {	
		PreparedStatement st = null;
		ResultSet result = null;
		try {
			String sql = "SELECT * FROM SOLDE WHERE idClient = " + this.idClient;
			st = co.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				if(result.getDouble(methode) >= this.offre.getTarif()) {
					Solde solde = new Solde(result.getInt("idClient"), result.getDouble("credit"), result.getDouble("mobileMoney"));
					Double s = result.getDouble(methode) - this.offre.getTarif();
					solde.update(co, methode, s);
					return true;
				}
	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null)result.close();
			if(st != null) st.close();
		}
		return false;
	}
	
	public void insert_solde(Connection co) throws Exception {
		PreparedStatement st = null;
		try {
//			String sql = "SELECT MAX(ID) as id frm"
			
			String sql = "INSERT INTO soldeOffre (idAchat, produit, valeur, unite, operateur, autres, international, delai) values (currVal('achat'), ?, ?, ?, ?, ?, ?, ?) ";
			st = co.prepareStatement(sql);
			for(Forfait forfait : this.offre.getForfaits()) {
				st.setInt(1, Function.getProduit(forfait.getProduit()));
				if(forfait.getValeur() != null) st.setDouble(2, forfait.getValeur());
				else st.setObject(2, null);
				st.setString(3, forfait.getUnite());
				if(forfait.getUtilisation() != null) {
					st.setDouble(4, forfait.getUtilisation().getOperateur());
					st.setDouble(5, forfait.getUtilisation().getAutres());
					st.setDouble(6, forfait.getUtilisation().getInternational());
				} else {
					st.setObject(4, null);
					st.setObject(5, null);
					st.setObject(6, null);
				}
				st.setDate(7, this.fin);
				
				st.addBatch();
			}
			st.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(st != null) st.close();
		}
	}
	
	public void updateSolde(Connection co) {
		
	}
	
	public static Response achat(String token, String idOffre, String date, String methode) throws Exception {
		Connection co = null;
		Response response = null;
		
		try {
			co = Function.getConnect();
			response = Login.getIdUserTokenRequired(co, token, Login.table2);
			if(response.code.compareTo("200") == 0) {
				Offre_client offres = new Offre_client(Integer.parseInt(response.data.toString()), idOffre, Date.valueOf(date), methode);
//			Offre_client offres = new Offre_client(Integer.parseInt(id), idOffre, Date.valueOf(date), methode);
//			co.setAutoCommit(false);
				if(!offres.verifier_solde(co, methode)) throw new Exception("Solde insuffisant");
				offres.insert(co);
				offres.insert_solde(co);
				co.commit();
				response = new Response("200", "Achat Reussie", offres);
			}
		} catch(Exception ex) {
			co.rollback();
			throw ex;
		} finally {
			if(co != null) co.close();
		}
		return response;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdClient() {
		return idClient;
	}
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
	public String getIdoffre() {
		return idoffre;
	}
	public void setIdoffre(String idoffre) {
		this.idoffre = idoffre;
	}
	public String getMethode() {
		return methode;
	}
	public void setMethode(String methode) {
		this.methode = methode;
	}
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Offres getOffre() {
		return offre;
	}
	public void setOffre(Offres offre) {
		this.offre = offre;
	}
	
	
}

package base;

import function.Function;
import function.Response;
import helper.NumeroHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SimulationAppel {
	String numero;
	String operateur;
	int seconde;
	Date date;
	int min;
	public SimulationAppel(String numero, String operateur, int seconde) {
		super();
		this.numero = numero;
		this.operateur = operateur;
		this.seconde = seconde;
	}
	public SimulationAppel(String numero, String minute, String seconde) throws Exception {
		super();
		this.numero = numero;
		NumeroHelper helper = new NumeroHelper(numero);
		this.operateur = helper.getOperateur();
		this.setSeconde(minute, seconde);
	}
	
	void setSeconde(String minute, String seconde) {
		try {
			int min = Integer.parseInt(minute);
			int sec = Integer.parseInt(seconde);
			this.seconde = sec + min*60;
		} catch(Exception ex) {
			throw ex;
		}
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getOperateur() {
		return operateur;
	}
	public void setOperateur(String operateur) {
		this.operateur = operateur;
	}
	public int getSeconde() {
		return seconde;
	}
	public void setSeconde(int seconde) {
		this.seconde = seconde;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int resteSeconde(Connection co, int idClient) throws Exception{
		try {
			co = Function.getConnect();
			this.min = this.seconde/60;
			if(this.seconde%60 != 0) this.min++;
			ArrayList<SoldeOffre> array = SoldeOffre.soldeValide(co, date, idClient+"");
			for(SoldeOffre solde : array) {
				if(this.calculConsommation(co, solde)) break;
			}
			this.consoCredit(co, idClient);
			co.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			co.rollback();
			e.printStackTrace();
			throw e;
		} 
		return this.seconde;
	}
	
	public boolean calculConsommation(Connection co, SoldeOffre soldeOffre) throws Exception {
		boolean bool = true;
		DetailsSoldeOffre details = soldeOffre.getDetails(co, 1, this.operateur);
		if(details.unite.compareToIgnoreCase("minute") == 0) {
			if(details.solde >= this.min) {
				this.min = 0;
				this.seconde = 0;
				details.solde -= this.min;
			}
			else {
				this.min -= details.solde;
				this.seconde = this.min * 60;
				details.solde = 0.0;
				bool = false;
			} 
			details.updateAppel(co, this.operateur, false);
		} else {
			Double conso = this.seconde * details.solde;
			if(details.valeur >= conso) {
				this.seconde = 0;
				details.valeur -= conso;
			}
			else {
				this.seconde -= details.valeur / details.solde;
				details.valeur = 0;
				bool = false;
			}
			details.updateAppel(co, this.operateur, true);
		}
		return bool;		
	}
	
	public void consoCredit(Connection co, int idClient) throws Exception {
		if(this.operateur.compareToIgnoreCase("operateur") == 0) this.operateur = "OmG";
		PreparedStatement st = null;
		ResultSet result = null;
		String sql = "SELECT prix from v_tarif where produits = 'Appel' and operateurs = ?";
		try {
			st = co.prepareStatement(sql);
			st.setString(1, this.operateur);
			System.out.println(st);
			result = st.executeQuery();
			Double tarif = null;
			if(result.next()) tarif = result.getDouble("prix");
			Double conso = this.seconde * tarif;
			Solde solde = new Solde(idClient, co);
			if(solde.credit >= conso) {
				this.seconde = 0;
				solde.credit -= conso;
			}
			else {
				solde.credit = 0.0;
				this.seconde -= solde.credit/tarif;
			}
			solde.update(co, "credit", solde.credit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(result != null) result.close();
			if(st != null) st.close();
		}
	}
	
	public static Response simuler(String token, String numero, String minute, String seconde) throws Exception {
		Response response = null;
		Connection co = null;
		try {
			co = Function.getConnect();
			response = Login.getIdUserTokenRequired(co, token, Login.table2);
			if(response.data != null) {
				String id = (String) response.data;
				int idClient = Integer.parseInt(id);
				Client client = new Client(co, idClient);
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(client);
				SimulationAppel simulation = new SimulationAppel(numero, minute, seconde);
				int reste = simulation.resteSeconde(co, idClient);
				list.add(reste);
				response = new Response(null, null, list);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(co != null) co.close();
		}
		return response;
	}
}

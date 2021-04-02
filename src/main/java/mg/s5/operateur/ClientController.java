package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.Client;
import base.Login;
import base.MobileMoney;
import base.Solde;
import base.mongo.Appel;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("clients")
public class ClientController {
	@GetMapping("")
	public Response get() {
		return Client .get();
	}
	
	@PostMapping("{idClient}/depot")
	public Response depot(@PathVariable Integer idClient, @RequestBody MobileMoney mobileMoney) {
		mobileMoney.setIdClient(idClient);
		return mobileMoney .post();
	}
	
	@PostMapping("login")
	public Response login(@RequestBody Client client) {
		return client.login();
	}
	
	@GetMapping("{num}/solde/mobilemoney")
	public Response getSoldeMobileMoney(@PathVariable String num) {
		return Client.getSoldeMobileMoney(num);
	}
	
	@GetMapping("{num}/solde/credit")
	public Response getSoldeCredit(@PathVariable String num) {
		return Client.getSoldeCredit(num);
	}

	@GetMapping("historique")
	public Response historique(@RequestHeader("Authorization") String token) {
		Response response = null;
		try {
			response = Client.getClientByToken(token);
			if(response.code.compareTo("200") == 0) {
				Client client = (Client) response.data;
				response = new Response("200", "historique", Appel.historique(client.getNumero())) ;
			}
				
		} catch(Exception ex) {
			response = new Response("400", ex.toString());
		}
		return response;
	}
	
	@GetMapping("solde")
	public Response solde(@RequestHeader("Authorization") String token) {
		return Solde.getSolde(token);
	}

	@GetMapping("acheter/credit/{montant}")
	public Response achatCredit(@RequestHeader("Authorization") String token, @PathVariable Double montant) {
		return Solde.achatCredit(token, montant);
	}
	
	@PostMapping("inscription")
	public Response inscription(@RequestBody Client client) {
		return client.inscription();
	}
}

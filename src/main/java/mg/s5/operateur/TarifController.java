package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;



import base.Tarif;
import function.Response;
import function.Function;

@RestController
@CrossOrigin
public class TarifController {
	@GetMapping("tarifs")
	public Response getData(@RequestHeader("Authorization") String token){
		return Tarif.getData(token);
	}
	
	@PutMapping("tarifs/modifier")
	public Response update(@RequestHeader("Authorization") String token, @RequestBody Tarif tarif) {
		return tarif.update(token);
	}
	
	@GetMapping("tarif/{id}")
	public Response getById(@RequestHeader("Authorization") String token, @PathVariable String id) {
		return Tarif.getById(id, token);
	}
	
	@GetMapping("produits")
	public Response produits() {
		Response response = null;
		try {
			response = new Response("200", "getProduits", Function.getProduits());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response = new Response("400", e.toString());
			e.printStackTrace();
		}
		return response;
	}
}

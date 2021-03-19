package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import base.Tarif;
import function.Response;

@RestController
@CrossOrigin
public class TarifController {
	@GetMapping("tarifs")
	public Response getData(){
		Response response = new Response();
		try {
			response.data = Tarif.getData();
			response.code = "200";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.code = "400";
			response.message = e.getMessage();
		}
		return response;
	}
	
	@PutMapping("tarifs/modifier")
	public Response update(@RequestBody Tarif tarif) {
		Response response = new Response();
		try {
			tarif = tarif.update();
			response.data = tarif;
			response.code = "200";
			response.message = "Modification reussie";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.code = "400";
			response.message = e.getMessage();
		}
		return response;
	}
	
	@GetMapping("tarif/{id}")
	public Response getById(@PathVariable String id) {
		Response response = new Response();
		try {
			response.data = Tarif.getById(id, null);
			response.code = "200";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.code = "400";
			response.message = e.getMessage();
		}
		return response;
	}
}

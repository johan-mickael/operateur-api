package mg.s5.operateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import function.Response;

@RestController
@CrossOrigin
public class OffreController {
	@Autowired
	public mg.s5.service.OffreRepository offre;
	
	@GetMapping("offres")
	public Response getData(){
		Response response = new Response();
		try {
			response.data = offre.findAll();
			response.code = "200";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.code = "400";
			response.message = e.getMessage();
		}
		return response;
	}
	
	@GetMapping("offres/{id}")
	public Response getById(@PathVariable String id) {
		Response response = new Response();
		try {
			response.data = offre.findById(id);
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

package mg.s5.operateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import function.Response;

@RestController
//@CrossOrigin
//@RequestMapping("acheter")
public class AchatController {
	@Autowired
	public mg.s5.service.OffreRepository offre;
	
	public Response find() {
//		System.out.println(offre.findAll().size());
		return new Response(null, null, offre.findAll());
	}
}

package mg.s5.operateur;

import java.util.ArrayList;

import mg.s5.service.AppelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.Client;
import base.SimulationAppel;
import base.mongo.Appel;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("appel")
public class AppelController {
	@Autowired
	public AppelRepository repository;
	
	@PostMapping("")
	public Response appeler(@RequestHeader("Authorization") String token, @RequestBody Appel appel) {
		Response response = null;
		try {
			response = SimulationAppel.simuler(token, appel.getDestinataire(), appel.getMinute(), appel.getSeconde());
			String message = "";
			if(response.code == null) {
				@SuppressWarnings("unchecked")
				ArrayList<Object> array = (ArrayList<Object>) response.data;
				if((int)array.get(1) > 0) {
					int reste = (int)response.data;
					appel.setMinute(reste/60 + "");
					appel.setSeconde(reste - reste/60 + "");
					message = "Credit insuffisant";
				} else message = "Appel effectue";
				Client client = (Client) array.get(0);
				appel.setDestinateur(client.getNumero());
				appel.setDate();
				repository.insert(appel);
				
				response = new Response("200", message, appel);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		}
		return response;
	}
}

package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.Client;
import base.MobileMoney;
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

}

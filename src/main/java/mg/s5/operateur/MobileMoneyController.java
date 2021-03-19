package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.vue.DepotNonValide;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("mobilemoney")
public class MobileMoneyController {
	@GetMapping("")
	public Response get() {
		return DepotNonValide .get();
	}
	
	@PutMapping("update")
	public Response put(@RequestBody DepotNonValide input) {
		return input.update();
	}
}

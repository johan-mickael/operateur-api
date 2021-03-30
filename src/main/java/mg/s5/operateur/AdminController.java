package mg.s5.operateur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.Admin;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("admin")
public class AdminController {
	@PostMapping("login")
	public Response login(@RequestBody Admin admin) {
		return admin.login();
	}
}

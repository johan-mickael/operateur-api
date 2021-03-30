package mg.s5.operateur;

import java.sql.Date;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.Statistiques;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("stat")
public class StatController {
	@GetMapping("{date}")
	public Response getStat(@RequestHeader("Authorization") String token, @PathVariable String date) {
		return Statistiques.getData(date, token);
	}
}

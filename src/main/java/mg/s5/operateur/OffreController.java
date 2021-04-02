package mg.s5.operateur;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import base.Login;
import base.Offre_client;
import base.mongo.Offres;
import function.Function;
import function.Response;

@RestController
@CrossOrigin
@RequestMapping("offres")
public class OffreController {
	@Autowired
	public mg.s5.service.OffreRepository offre;
	

	@GetMapping("")
	public Response getData(@RequestHeader("Authorization") String token){
		Connection co = null;
		Response response = null;
		try {
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				response = new Response("200", "Get Offre OK", offre.findAll());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException e) {
				e.printStackTrace();
				response = new Response("500", e.toString());
			}
		}
		return response;
	}
	
	@GetMapping("get")
	public Response getData(){
		Response response = null;
		try {
			response = new Response("200", "Get Offre OK", offre.findAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		}
		return response;
	}
	

	@GetMapping("{id}")
	public Response getById(@RequestHeader("Authorization") String token, @PathVariable String id) {
		Connection co = null;
		Response response = null;
		try {
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				response = new Response("200", "Get By id Offre OK", offre.findById(id));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException e) {
				e.printStackTrace();
				response = new Response("500", e.toString());
			}
		}
		return response;
	}
	

	@PutMapping("modifier") 
	public Response update(@RequestHeader("Authorization") String token, @RequestBody Offres off) {
		Connection co = null;
		Response response = null;
		try {
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				offre.save(off);
				response = new Response("200", "Get Offre OK", off);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException e) {
				e.printStackTrace();
				response = new Response("500", e.toString());
			}
		}
		return response;
	}
	

	@PostMapping("creer")
	public Response create(@RequestHeader("Authorization") String token, @RequestBody Offres off) {
		Connection co = null;
		Response response = null;
		try {
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				off.setIdOffre(Function.nextVal(co) + "");
				offre.insert(off);
				response = new Response("200", "Get Offre OK", off);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException e) {
				e.printStackTrace();
				response = new Response("500", e.toString());
			}
		}
		return response;
	}
	

	@DeleteMapping("supprimer/{id}")
	public Response delete(@RequestHeader("Authorization") String token, @PathVariable String id) {
		Connection co = null;
		Response response = null;
		try {
			co = Function.getConnect();
			response = Login.tokenRequired(co, token, Login.table1);
			if(response == null) {
				offre.deleteById(id);
				response = new Response("200", "Get Offre OK", offre.findAll());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} finally {
			try {
				if(co != null) co.close();
			} catch(SQLException e) {
				e.printStackTrace();
				response = new Response("500", e.toString());
			}
		}
		return response;
	}
	
	@GetMapping("acheter/{idOffre}/{methode}/{date}")
	public Response buy(@RequestHeader("Authorization") String token, @PathVariable String idOffre, @PathVariable String methode, @PathVariable String date) {
		Response response = null;
		try {
			response = Offre_client.achat(token, idOffre, date, methode);
			if(response == null) response = new Response("200", "Achat reussi", offre.findAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = new Response("400", e.toString());
		} 
		return response;
	}
}

package function;

import org.springframework.beans.factory.annotation.Autowired;
import base.mongo.Offres;


public class Main {
	@Autowired
	public static mg.s5.service.OffreRepository offre;
	public static void main(String[] args) throws Exception {
//		Response res = Client.get();
//		System.out.println(res);
		
		Offres offre = Mongo.getById("2");
		System.out.println(offre.getForfaits().get(1).getUtilisation().getOperateur());
	}

}

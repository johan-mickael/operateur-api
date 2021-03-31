package function;

import org.springframework.beans.factory.annotation.Autowired;
import base.mongo.Offres;
import helper.NumeroHelper;


public class Main {
	@Autowired
	public static mg.s5.service.OffreRepository offre;
	public static void main(String[] args) throws Exception {
//		Response res = Client.get();
//		System.out.println(res);
		String n = NumeroHelper.genererNumero();
		System.out.println(n);
	}

}

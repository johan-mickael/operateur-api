package function;

import java.sql.Connection;
import java.util.List;

import mg.s5.operateur.OffreController;

import org.springframework.beans.factory.annotation.Autowired;

import base.Client;
import base.mongo.Offres;



public class Main {
	
	public static void main(String[] args) throws Exception {
		Response res = Client.get();
		System.out.println(res);
	}

}

package johan.test;

import java.sql.Connection;
import java.util.ArrayList;

import base.Tarif;
import function.Function;
import function.Response;
import mg.s5.operateur.TarifController;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Connection co = Function.getConnect();
		TarifController tc = new TarifController();
		Response r = tc.getData();
		System.out.println(r);
	}

}

package johan.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import base.Admin;
import base.Login;
import base.Tarif;
import function.Function;
import function.Response;
import helper.Helper;
import helper.NumeroHelper;
import mg.s5.operateur.AdminController;
import mg.s5.operateur.MobileMoneyController;
import mg.s5.operateur.TarifController;

public class Main {

	public static void main(String[] args) throws Exception {
		NumeroHelper num = new NumeroHelper("03415486789");
		String pref = num.getOperateur();
		System.out.println(pref);
	}

}

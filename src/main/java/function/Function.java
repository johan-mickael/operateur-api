package function;

import java.sql.Connection;
import java.sql.DriverManager;

public class Function {
	public static Connection getConnect() throws Exception {
		Class.forName("org.postgresql.Driver");
        Connection co = DriverManager
           .getConnection("jdbc:postgresql://ec2-54-211-176-156.compute-1.amazonaws.com:5432/dfipgrpro24pi4",
           "kmfflyzndyujjm", "4964e91fd56ca6b8c4f050f543c244e78def5114ba39b77373ea2648516d3be3");
		return co;
	}	
}

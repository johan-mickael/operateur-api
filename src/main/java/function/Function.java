package function;

import java.sql.Connection;
import java.sql.DriverManager;

public class Function {
	public static Connection getConnect() throws Exception {
		Class.forName("org.postgresql.Driver");
        Connection co = DriverManager
           .getConnection("jdbc:postgresql://ec2-52-7-115-250.compute-1.amazonaws.com:5432/d6lnt892j86noc",
           "vbjpgprvmqbunt", "1818d5a0403ee2e4813cdf8ff5fe7a3030891fed0a86b726a08a1a42be692899");
		return co;
	}	
}

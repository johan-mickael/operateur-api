package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Helper {
	public static Integer getMaxId(Connection co, String table) throws Exception {
		Integer ret = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try { 
			String sql = "select max(id) id from "+table;
			st = co.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()) {
				ret = rs.getInt("id");
			}
		} catch(Exception ex) {
			throw ex;
		}
		return ret;
	}
}
/**
 * 
 */
package com.querybyexample.functionality;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author spectral369
 *
 */
public class ConnectionOracle extends ConnectionDB {

	public ConnectionOracle(String userName, char[] password, String server,
			int port, String SID) {
		super(userName, password, server, port, SID);
	}

	@SuppressWarnings("unused")
	private Connection setConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + SID;

			connection = DriverManager.getConnection(url, userName,
					String.copyValueOf(password));

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	protected void CheckConnectionOracle() {

		try {

			statement = connection.createStatement();
			resultSet = statement.executeQuery("select  'Connected' from dual");
			if (resultSet.next()) {
				workingState = true;
				System.out.println(resultSet.getString(1));
			}

			// end
			resultSet.close();
			statement.close();
			log.log(Level.FINE, "Conn SUCCess", connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}

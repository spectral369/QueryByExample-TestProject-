/**
 * 
 */
package com.querybyexample.functionality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author spectral369
 *
 */
public abstract class ConnectionDB {
	// Connection requirements
	protected String userName = null;
	protected char[] password = null;
	protected String server = null;
	protected int port = 0;
	/* oracle specific */
	protected String SID = null;
	protected String[] schemas = null;
	/* Oracle Specific */
	Logger log;
	protected boolean workingState = false;
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	protected Vector<Object> QBECols;
	protected Vector<Object> data;


	// sql constructor
	public ConnectionDB(String userName, char[] password, String server,
			int port) {
		this.userName = userName;
		this.password = password;
		this.server = server;
		this.port = port;
		try {
			log = UtilitiesQBE.getLogger(ConnectionDB.class);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// oracle Contructor
	public ConnectionDB(String userName, char[] password, String server,
			int port, String SID) {
		this.userName = userName;
		this.password = password;
		this.server = server;
		this.port = port;
		this.SID = SID;
		try {
			log = UtilitiesQBE.getLogger(ConnectionDB.class);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

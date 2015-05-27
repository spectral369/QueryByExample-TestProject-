package com.querybyexample.functionality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChoseConnection {
	private Logger logChose;
	protected ConnectionMysql sql = null;
	protected static ConnectionOracle oracle = null;
	protected ConnectionDB connDB = null;
	protected Object ins = null;

	protected String userName = null;
	protected char[] password = null;
	protected String server = null;
	protected int port;
	protected String SID = null;
	protected String database = null;

	private void SetLog() {
		try {
			logChose = UtilitiesQBE.getLogger(ConnectionMysql.class);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ChoseConnection() {
		SetLog();
	}

	protected void setInfo(String userName, String password, String server,
			int port, String SID, String database) {
		this.userName = userName;
		this.password = password.toCharArray();
		this.server = server;
		this.port = port;
		this.SID = SID;
		this.database = database;

	}

	protected void setInfo(String userName, String password, String server,
			int port) {
		this.userName = userName;
		this.password = password.toCharArray();
		this.server = server;
		this.port = port;

	}

	public Connection getCon() {
		// return connDB.connection;
		if (sql != null)
			return sql.connection;
		if (oracle != null)
			return oracle.connection;
		else
			return null;

	}

	public void ChoseConn(int i) {
		switch (i) {
		case 1:
			logChose.log(Level.INFO, "SQL Server Chosen");
			sql = new ConnectionMysql(userName, password, server, port);
			break;
		case 2:
			logChose.log(Level.INFO, "Oracle Server Chosens");
			if (oracle == null)
				oracle = new ConnectionOracle(userName, password, server, port,
						SID);
			break;

		default:
			logChose.log(Level.WARNING, "bad Choise");
			break;
		}
	}

	public void changeDatabase(String database) throws SQLException {
		sql.changedb(database);
	}

}

/**
 * 
 */
package com.querybyexample.functionality;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * @author spectral369
 *
 */
public class ConnectionMysql extends ConnectionDB {
	private MysqlDataSource dataSource = null;
	private QueryData qdata = null;

	protected void mysqlSetDataSource() {
		dataSource.setUser(userName);
		dataSource.setPassword(String.valueOf(password));
		dataSource.setPort(port);
		dataSource.setServerName(server);
	}

	public ConnectionMysql(String userName, char[] password, String server,
			int port) {
		super(server, password, server, port);
		this.userName = userName;
		this.password = password;
		this.server = server;
		this.port = port;

		try {
			dataSource = new MysqlDataSource();
			mysqlSetDataSource();
			
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}

	public void setDatabase(String database) {
		if(UtilitiesQBE.isLogAcctive)
		log.log(Level.INFO, "Dtabase Chosen = " + database);
		selectDatabase(database);
	}

	private void selectDatabase(String database2) {
		// TODO Auto-generated method stub
		try {
			connection.createStatement().executeQuery("use " + database2 + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.getMessage();
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.WARNING, "failed to select DB", e);
		}
	}

	public boolean checkConnection() {

		// try {

		/*
		 * statement = connection.createStatement(); ResultSet rst =
		 * statement.executeQuery("Select @@hostname;"); if(rst.next())
		 * System.out.println(rst.getString(1)+"\n");
		 */
		try {
			if (connection.isValid(5000)) {
				if(UtilitiesQBE.isLogAcctive)
				log.log(Level.WARNING, "Success", connection);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.SEVERE, "Connection failed", connection);
			return false;
		}
		/*
		 * } catch (SQLException | SecurityException e) { // TODO Auto-generated
		 * catch block log.log(Level.SEVERE, e.getMessage(), connection); }
		 */
		return false;

	}

	String[] databases = null;

	protected String[] getDatabases() {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("show databases;");
			int rowCount = 0;
			int size = 0;
			if (resultSet != null) {
				resultSet.beforeFirst();
				resultSet.last();
				size = resultSet.getRow();

			}
			databases = new String[size];
			resultSet.beforeFirst();
			while (resultSet.next()) {
				databases[rowCount] = resultSet.getString(1);
				rowCount++;

			}
			// end

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.SEVERE, e.getMessage(), connection);
		}

		return databases;
	}

	String[] tables = null;

	protected String[] getTables() {
		try {
			statement = connection.createStatement();

			resultSet = statement.executeQuery("show tables;");
			int rowCount = 0;
			int size = 0;
			if (resultSet != null) {
				resultSet.beforeFirst();
				resultSet.last();
				size = resultSet.getRow();

			}
			tables = new String[size];
			resultSet.beforeFirst();
			while (resultSet.next()) {
				tables[rowCount] = resultSet.getString(1);
				// System.out.println(resultSet.getString(1));
				rowCount++;

			}
			// end

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.SEVERE, e.getMessage(), connection);
		}

		return tables;
	}

	public void printTesting() {
		for (int i = 0; i < databases.length; i++) {
			System.out.println(databases[i]);
		}
		System.out.println("---------------------------");
		for (int i = 0; i < tables.length; i++) {
			System.out.println(tables[i]);
		}
	}

	protected void changedb(String db) throws SQLException {
		try {
			connection.createStatement().executeQuery("use " + db + ";");
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.INFO, "database changed: " + db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException("database not available");
		}
	}

	protected ArrayList<Object> getDBTAbles() {
		ArrayList<Object> tables = new ArrayList<>();

		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("show tables;");
			while (resultSet.next()) {
				tables.add(resultSet.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tables;

	}

	protected ArrayList<Object> getColumns(String table) {
		ArrayList<Object> columns = new ArrayList<Object>();

		try {
			statement = connection.createStatement();

			resultSet = statement.executeQuery("SHOW COLUMNS FROM " + table
					+ ";");
			while (resultSet.next()) {
				columns.add(resultSet.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columns;

	}

	protected QueryData QBEQuerySQL(String sqlDatabase, String Table,
			String QueryString, String[] collsSelected) {
		boolean isColSelected = false;
		if (collsSelected != null)
			isColSelected = true;
		qdata = new QueryData();
		try {
			statement = connection.createStatement();
			statement.executeQuery("use " + sqlDatabase + ";");
			statement = connection.createStatement();
			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			if (isColSelected == true) {
				

				for (String s : collsSelected) {
					sb.append(s + ", ");
					sb2.append(s + " LIKE '%" + QueryString + "%' " + "OR ");
				}
				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, " ");
				sb2.replace(sb2.lastIndexOf("OR"), sb2.lastIndexOf("OR") + 2,
						" ");
				
				String[]tokens = QueryString.split("\\s+|[,:-]");
				if(QueryString.contains(":")&&((tokens.length%2)==0)){
				
				StringBuilder ad =  new StringBuilder();
				
					
				for(int h=0;h<tokens.length;h=h+2){
				ad.append(tokens[h]);
				ad.append("='");
				ad.append(tokens[h+1]);
				ad.append("' ");
				ad.append("OR ");
				}
				
				ad.replace(ad.lastIndexOf("OR"), ad.lastIndexOf("OR") + 2,"");
				sb2=ad;
				}
				
				
				
			} else if (isColSelected == false) {
				//StringBuilder sb = new StringBuilder();
			//	StringBuilder sb2 = new StringBuilder();
				ArrayList<Object> tabl = new ArrayList<Object>(
						getColumns(Table));

				for (Object s : getColumns(Table).toArray())
					sb.append(s + ", ");
				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, " ");

				for (int i = 0; i < tabl.size(); i++)
					sb2.append(tabl.get(i).toString() + " LIKE '%"
							+ QueryString + "%' " + " OR ");
				sb2.replace(sb2.lastIndexOf("OR"), sb2.lastIndexOf("OR") + 2,
						" ");
				String[]tokens = QueryString.split("\\s+|[,:-]");
				
				if(QueryString.contains(":")&&((tokens.length%2)==0)){
					
					StringBuilder ad =  new StringBuilder();
					
					for(int h=0;h<tokens.length;h=h+2){
					ad.append(tokens[h]);
					ad.append("='");
					ad.append(tokens[h+1]);
					ad.append("' ");
					ad.append("OR ");
					}
					
					ad.replace(ad.lastIndexOf("OR"), ad.lastIndexOf("OR") + 2,"");
					sb2=ad;
					}
				
				
				

			}
			String query = "SELECT " + sb + " FROM " + sqlDatabase + "."
					+ Table + " where " + sb2 + ";";
			if(UtilitiesQBE.isLogAcctive)
			log.log(Level.INFO, "QueryString: " + query);
			try{
			resultSet = statement.executeQuery(query);
			}catch(Exception ex){
				resultSet = statement.executeQuery("SHOW columns FROM " + sqlDatabase + "."
						+ Table + ";");
				
			}
			int length = resultSet.getMetaData().getColumnCount();
			qdata.setLength(length);
			// add col names
			QBECols = new Vector<Object>(length);
			for (int i = 1; i <= length; i++)
				QBECols.add(resultSet.getMetaData().getColumnName(i));
			// add qbe data
			data = new Vector<Object>();
			while (resultSet.next()) {
				Vector<Object> row = new Vector<Object>(length);
				for (int i = 1; i <= length; i++) {
					row.add(resultSet.getString(i));
				}
				data.add(row);
			}
			qdata.setQBECols(QBECols);
			qdata.setdata(data);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qdata;

	}
	
	//stackoverflow solution !
	public static int ordinalIndexOf(String str, String s, int n) {
	    int pos = str.indexOf(s, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(s, pos+1);
	    return pos;
	}


	protected QueryData SimpleQuery(String QueryString) {
		qdata = new QueryData();
		int length = 0;

		try {
			statement = connection.createStatement();

			resultSet = statement.executeQuery(QueryString);
			length = resultSet.getMetaData().getColumnCount();
			qdata.setLength(length);
			// add col names
			QBECols = new Vector<Object>(length);
			for (int i = 1; i <= length; i++)
				QBECols.add(resultSet.getMetaData().getColumnName(i));
			// add qbe data
			data = new Vector<Object>();
			while (resultSet.next()) {
				Vector<Object> row = new Vector<Object>(length);
				for (int i = 0; i <= length; i++) {
					row.add(resultSet.getString(i));
				}
				data.add(row);
			}
			qdata.setQBECols(QBECols);
			qdata.setdata(data);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return qdata;
	}

}

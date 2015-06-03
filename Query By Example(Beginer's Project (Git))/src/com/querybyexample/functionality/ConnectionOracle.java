/**
 * 
 */
package com.querybyexample.functionality;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

/**
 * @author spectral369
 *
 */
public class ConnectionOracle extends ConnectionDB {
	private QueryData data = null;
	
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

	protected boolean CheckConnectionOracle() {

		try {

			statement = connection.createStatement();
			resultSet = statement.executeQuery("select  'Connected' from dual");
			if (resultSet.next()) {
				workingState = true;
				System.out.println(resultSet.getString(1));
				
			}
			
			log.log(Level.FINE, "Conn SUCCess", connection);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}
	
	

	protected String[] getOracleSchemas(){
		String[] schemas  = null;
		int size = 0;
		try {
			statement =  connection.createStatement();
		
		resultSet =  statement.executeQuery("select distinct"
				+ " owner from dba_objects");
		if (resultSet != null) {
			resultSet.beforeFirst();
			resultSet.last();
			size = resultSet.getRow();
		}
		schemas =  new String[size];
		int i =  0 ;
		resultSet.beforeFirst();
		while(resultSet.next()){
			schemas[i] = resultSet.getString(1);
			i++;
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			try{
				statement =  connection.createStatement();
				resultSet =  statement.executeQuery("select sys_context('userenv','session_user') from dual");
				if (resultSet != null) {
					resultSet.beforeFirst();
					resultSet.last();
					size = resultSet.getRow();
				}
				schemas =  new String[size];
				int i = 0;
				resultSet.beforeFirst();
				while(resultSet.next()){
					schemas[i] = resultSet.getString(1);
					i++;
				}
			}catch(SQLException  ex){
				
			}
		}
		return schemas;
	}
	
	protected ArrayList<String> getOracleTables(String schema){
		ArrayList<String> oracleTables =  new ArrayList<String>();
		
		try {
			statement =  connection.createStatement();
		
		resultSet =  statement.executeQuery("select table_name from all tables where owner = '"+schema+"'");
		while(resultSet.next()){
			oracleTables.add(resultSet.getString(1));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return oracleTables;
	}

	protected ArrayList<String> getOracleColumns(String table){
		ArrayList<String> oracleColumns =  new ArrayList<String>();
		
		try{
		statement =  connection.createStatement();
		resultSet =  statement.executeQuery("select column_name from all_tab_columns where table_name = '"+table+"'");
		while(resultSet.next())
			oracleColumns.add(resultSet.getString(1));
		}catch(SQLException e){
			
		}
		return oracleColumns;
	}
	
	protected QueryData oracleQBE(String schema,String table,String Query,String[] Columns){
		
		boolean isAnyColSelected = false;
		if(Columns != null)
			isAnyColSelected = true;
		
		
		try {
			statement =  connection.createStatement();
		
			if(isAnyColSelected ==true){
				StringBuilder sb =  new StringBuilder();
				StringBuilder sb1 =  new StringBuilder();
				for(String s: Columns){
					sb.append(s+", ");
					sb1.append(s+" LIKE '%"+Query+"%'"+" OR ");
				}
				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+1, " ");
				sb1.replace(sb1.lastIndexOf("OR"), sb.lastIndexOf("OR")+2, " ");
				String queryString =  "SELECT "+sb+" FROM "+schema+"."+table+" WHERE "+sb1;
				resultSet =  statement.executeQuery(queryString);
			}
			else if(isAnyColSelected==false){
				StringBuilder sb  = new StringBuilder();
				for(Object s:getOracleColumns(table).toArray()){
					sb.append(s.toString()+", ");
				}
					sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+1, " ");
					
					StringBuilder sb1 =  new StringBuilder();
					ArrayList<String> col =  new ArrayList<String>(getOracleColumns(table));
					for( int i = 0;i < col.size();i++){
						sb1.append(col.get(i).toString()+" LIKE '%"+Query+"%'"+ " OR ");
					}
					sb1.replace(sb1.lastIndexOf("OR"), sb1.lastIndexOf("OR")+2, " ");
					String queryString =  "SELECT "+sb+" FROM "+schema+"."+table+" WHERE "+sb1;
					
					resultSet =  statement.executeQuery(queryString);		
			}
			data =  new QueryData();
			int length =  resultSet.getMetaData().getColumnCount();
			data.setLength(length);
			Vector<Object> cols =  new Vector<Object>();
			for( int i = 1;i<=length;i++)
				cols.add(resultSet.getMetaData().getColumnName(i));
			data.setQBECols(cols);
			
			Vector<Object> QBEData =  new Vector<>();
			while(resultSet.next()){
				Vector<String> row =  new Vector<String>();
				for(int i=1;i<=length;i++){
					row.add(resultSet.getString(i));
				}
				QBEData.add(row);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	protected QueryData SimpleQuery(String query){
		
		try {
			statement =  connection.createStatement();
		resultSet =  statement.executeQuery(query);
		int length =  resultSet.getMetaData().getColumnCount();
		data =  new QueryData();
		data.setLength(length);
		Vector<Object> queryCol =  new Vector<>();
		for(int i = 1;i<=length;i++)
			queryCol.add(resultSet.getMetaData().getColumnName(i));
		data.setQBECols(queryCol);
		
		Vector<Object> queryData =  new Vector<Object>();
		while(resultSet.next()){
			Vector<String> row =  new Vector<String>();
			for(int i = 1;i<=length;i++){
				row.add(resultSet.getString(i));
			}
			queryData.add(row);
		}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return data;
	}
	
}

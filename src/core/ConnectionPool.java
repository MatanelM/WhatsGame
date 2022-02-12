package core;

import util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class is a singleton.
 * it is being used to pull a connection in classes like CompanyDBDAO, for creating a new data or updating
 * data in any table of the system.
 * 
 */
public class  ConnectionPool{
		

	Set <Connection> Conpool = new HashSet<>();
	
	public static ConnectionPool instance = null;
	/**
	 * this method return the instance of this class 
	 * so it able to pull a connection from it later
	 * @return
	 */
	public synchronized static ConnectionPool getInstance(){
		if(instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}
	/**
	 * this constructor creates a connection to the database and then creates 15 connection 
	 * after these are initiated the connections are stored inside this class.
	 */
	public ConnectionPool(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try
		{
			for(int i = 0 ; i<15 ; i++ )
			{
				Connection con = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/%s?user=%s&password=%s", Util.DATABASE_NAME, Util.USERNAME, Util.PASSWORD));
				Conpool.add(con);
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	/**
	 * returns a connection to the derby from the connection pool
	 * @return
	 */
	public synchronized Connection getConnection(){
		while(Conpool.isEmpty())
		{
			try{
			System.out.println("no connection, wait..");
			wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		
		Iterator <Connection> I = Conpool.iterator();
		Connection con = I.next();
		
		I.remove();
		return con;
		
	}
	/**
	 * store a given connection to the derby in the connection pool
	 * @param con
	 */
	public synchronized void returnConnection(Connection con){
		Conpool.add(con);
		if(Conpool.isEmpty())
		notifyAll();
	}

	/**
	 * close all the connections that are in the pool
	 */
	public void closeAllConnections(){
		
		for(Connection con : Conpool){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
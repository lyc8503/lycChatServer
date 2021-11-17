package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import gui.MainFrame;
import start.ServerStart;

public class DBUtil {//数据库连接
	
	public static Connection open(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection(ServerStart.databaseaddress,ServerStart.databasename,ServerStart.databasepasswd);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			ServerStart.stop();
			return null;
		}
	}
	
	public static void close(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ServerStart.stop();
		}
	}
	
	public static void checkdb(){
		try {
			System.out.println("Checking Database...");
			MainFrame.serverstatuslabel.setText("Checking Database...");
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection(ServerStart.databaseaddress,ServerStart.databasename,ServerStart.databasepasswd);
			Statement statement = conn.createStatement();
			statement.execute("create table IF NOT EXISTS UserTable(id int primary key auto_increment,name varchar(20),email varchar(50),password varchar(32));");
			conn.close();
			System.out.println("Success!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.frame,"数据库连接异常!" ,"Error!",JOptionPane.PLAIN_MESSAGE);
			ServerStart.stop();
		}
	}
}

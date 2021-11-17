package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Userdaoimple implements Userdao{//UserDAO实现类
	
	public boolean add(User u){//添加用户
		if(u.getEmail()==null|u.getName()==null|u.getPassword()==null){
			return false;
		}
		if(u.getName().equals("")|u.getEmail().equals("")|u.getPassword().equals("")){
			return false;
		}
		Connection conn=DBUtil.open();
		Userdao dao=new Userdaoimple();
		if(dao.query(u.getName())==null){
			try {
				PreparedStatement stat=conn.prepareStatement("insert into UserTable (name,email,password)values(?,?,?)");
				stat.setString(1,u.getName());
				stat.setString(2,u.getEmail());
				stat.setString(3,u.getPassword());
				stat.executeUpdate();
				DBUtil.close(conn);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				DBUtil.close(conn);
				return false;
			}
		}else{
		DBUtil.close(conn);
		return false;
		}
	}
	
	public boolean delete(User u) {//删除用户
		Connection conn=DBUtil.open();
		Userdao dao=new Userdaoimple();
		boolean ifsuccess=dao.setObjectid(u);
		if(ifsuccess){
			PreparedStatement stat;
			try {
				stat = conn.prepareStatement("delete from UserTable where id=?");
				stat.setInt(1,u.getId());
				stat.executeUpdate();
				DBUtil.close(conn);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				DBUtil.close(conn);
				return false;
			}
		}else{
			DBUtil.close(conn);
			return false;
		}
	}

	public User query(String name) {//以用户名查询用户
		try{
			if(name==null|name.equals("")){
				return null;
			}
		}catch (Exception e) {
			return null;
		}
		
		Connection conn=DBUtil.open();
		try {
			Statement stat=conn.createStatement();
			ResultSet rs=stat.executeQuery("select * from UserTable");
			while(rs.next()){
				if(rs.getString(2)==null){
				}else{
					if(rs.getString(2).equals(name)){
							User u=new User();
							u.setId(rs.getInt(1));
							u.setEmail(rs.getString(3));
							u.setName(rs.getString(2));
							u.setPassword(rs.getString(4));
							DBUtil.close(conn);
							return u;
						}
					}
				}
		}catch(Exception e){
			e.printStackTrace();
			DBUtil.close(conn);
			return null;
		}
		DBUtil.close(conn);
		return null;
	}

	public boolean check(User u) {//检查用户账号与密码
		Connection conn=DBUtil.open();
		Statement stat;
		try {
			stat = conn.createStatement();
			ResultSet rs=stat.executeQuery("select * from UserTable");
			while(rs.next()){
				String name=rs.getString(2);
				String passwd=rs.getString(4);
				if(name==null|passwd==null){
					//PASS
				}else {
					if(name.equals(u.getName()) & passwd.equals(u.getPassword())){
						DBUtil.close(conn);
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBUtil.close(conn);
			return false;
		}
		DBUtil.close(conn);
		return false;
	}

	public User query(int id) {//以ID查询用户
		Connection conn=DBUtil.open();
		Statement stat;
		try {
			stat = conn.createStatement();
			ResultSet rs=stat.executeQuery("select * from UserTable");
			while(rs.next()){
				if(rs.getInt(1)==id){
					User u=new User();
					u.setId(rs.getInt(1));
					u.setName(rs.getString(2));
					u.setEmail(rs.getString(3));
					u.setPassword(rs.getString(4));
					DBUtil.close(conn);
					return u;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBUtil.close(conn);
			return null;
		}
		return null;
	}

	public boolean setObjectid(User u) {//设置用户对象的ID
		Connection conn=DBUtil.open();
		try {
			Statement stat=conn.createStatement();
			ResultSet rs=stat.executeQuery("select * from UserTable");
			Userdao dao=new Userdaoimple();
			if(dao.check(u)){
				while(rs.next()){
					if(u.getName().equals(rs.getString(2))){
						u.setId(rs.getInt(1));
					}
				}
			}else{
				DBUtil.close(conn);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBUtil.close(conn);
			return false;
		}
		DBUtil.close(conn);
		return true;
	}

	public boolean changePassword(User u, String newpassword) {//更改密码
		Connection conn=DBUtil.open();
		PreparedStatement pstat;
		try {
			setObjectid(u);
			pstat=conn.prepareStatement("update UserTable set password=? where id=?");
			pstat.setString(1,newpassword);
			pstat.setInt(2,u.getId());
			pstat.executeUpdate();
			DBUtil.close(conn);
			return true;
		} catch (Exception e) {
			DBUtil.close(conn);
			return false;
		}
	}

	public int queryregisteredusernum() {//查询用户数量
		Connection conn=DBUtil.open();
		int i = 0;
		Statement stat;
		ResultSet rs;
		try {
			stat = conn.createStatement();
			rs=stat.executeQuery("select * from UserTable");
			while(rs.next()){
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtil.close(conn);
			return -1;
		}
		DBUtil.close(conn);
		return i;
	}

	public Vector<User> getallusers() {//获取所有注册的用户
		Connection conn=DBUtil.open();
		Vector<User> user=new Vector<User>();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs=stat.executeQuery("select * from UserTable");
			while(rs.next()){
				User u=new User();
				u.setId(rs.getInt(1));
				u.setName(rs.getString(2));
				u.setEmail(rs.getString(3));
				u.setPassword(rs.getString(4));
				user.addElement(u);
			}
		} catch (SQLException e) {
			DBUtil.close(conn);
			e.printStackTrace();
			return null;
		}
		DBUtil.close(conn);
		return user;
	}
}

package database;

import java.util.Vector;

public interface Userdao {//UserDAO½Ó¿Ú
	public int queryregisteredusernum();
	public boolean add(User u);
	public boolean delete(User u);
	public User query(String name);
	public User query(int id);
	public boolean check(User u);
	public boolean setObjectid(User u);
	public boolean changePassword(User u,String newpassword);
	public Vector<?> getallusers();
}

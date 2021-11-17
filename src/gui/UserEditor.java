package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import database.User;
import database.Userdao;
import database.Userdaoimple;
import server.Mydata;
import server.ReadData;
import server.SendData;
import server.ServerMain;

public class UserEditor {//用户管理界面
	public static UserEditor userEditor;
	JFrame frame;
	GridBagConstraints constraints;
	GridBagLayout layout;
	JList<Object> registereduser;
	DefaultListModel<Object> listmodel;
	JButton kick;
	JButton info;
	JButton kicknotlogin;
	JButton queryid;
	JButton adduser;
	JButton deleteuser;
	JButton changepassword;
	public UserEditor() {
		frame=new JFrame("Edit User");
		constraints=new GridBagConstraints();
		layout=new GridBagLayout();
		listmodel=new DefaultListModel<Object>();
		registereduser=new JList<Object>();
		registereduser.setModel(listmodel);
		frame.setLayout(layout);
		
		constraints.fill=GridBagConstraints.BOTH;
		constraints.gridx=1;
		constraints.gridy=1;
		constraints.gridheight=7;
		constraints.ipadx=200;
		constraints.ipady=150;
		JScrollPane scrollPane=new JScrollPane(registereduser);
		layout.setConstraints(scrollPane, constraints);
		frame.getContentPane().add(scrollPane);
		
		info=new JButton("发布公告");
		info.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.gridheight=1;
		constraints.ipadx=0;
		constraints.ipady=0;
		layout.setConstraints(info, constraints);
		frame.getContentPane().add(info);
		
		kick=new JButton("踢出用户");
		kick.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=2;
		layout.setConstraints(kick, constraints);
		frame.getContentPane().add(kick);
		
		kicknotlogin=new JButton("踢出未登录用户");
		kicknotlogin.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=3;
		layout.setConstraints(kicknotlogin, constraints);
		frame.getContentPane().add(kicknotlogin);
		
		queryid=new JButton("查询用户");
		queryid.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=4;
		layout.setConstraints(queryid, constraints);
		frame.getContentPane().add(queryid);
		
		adduser=new JButton("添加用户");
		adduser.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=5;
		layout.setConstraints(adduser, constraints);
		frame.getContentPane().add(adduser);
		
		deleteuser=new JButton("删除用户");
		deleteuser.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=6;
		layout.setConstraints(deleteuser, constraints);
		frame.getContentPane().add(deleteuser);
		
		changepassword=new JButton("更改密码");
		changepassword.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=7;
		layout.setConstraints(changepassword, constraints);
		frame.getContentPane().add(changepassword);
	}
	
	public void showframe(){
		Vector<User> alluser=new Userdaoimple().getallusers();
		listmodel.removeAllElements();
		for(int i=0;i<alluser.size();i++){
			User user=(User) alluser.get(i);
			listmodel.addElement(new String("ID:"+user.getId()+"---Name:"+user.getName()+"---Email:"+user.getEmail()+"---Password:"+user.getPassword()));
		}
		frame.setLocation(0, 0);
		frame.setSize(315,230);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}

class MyActionListener implements ActionListener{
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "踢出用户":
			String kick = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入用户名", "踢出用户",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			ReadData readData = null;
			outer:for (int i = 0; i < ServerMain.readdataVector.size(); i++) {
				try {
					if (ServerMain.readdataVector.get(i).curuser.getName().equals(kick)) {
						readData=ServerMain.readdataVector.get(i);
						break outer;
					}
				} catch (Exception e2) {
					//Ignore
				}
			}
			if(readData==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"找不到名为 "+kick+" 的在线用户!" ,"Warning",JOptionPane.PLAIN_MESSAGE);
			}else {
				int n = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "确认踢出"+kick+"?", "Warning", JOptionPane.YES_NO_OPTION);
				if (n==JOptionPane.YES_OPTION) {
					String reason = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
					        "踢出理由", "踢出用户",
					        JOptionPane.QUESTION_MESSAGE,null, null, "管理员后台操作");
					String message[]=new String[2];
					message[0]="lycChatMessage";
					message[1]="你被服务器踢出\n"+reason;
					Mydata mydata=new Mydata(message,readData);
					SendData.senddata(mydata);
					SendData.waitsend(mydata);//等待信息发出
					try {
						ServerMain.readdataVector.remove(readData);
						readData.t.stop();//结束进程
						readData.outputstream.close();
						readData.inputstream.close();
						System.out.println("Kicked:"+kick);
						JOptionPane.showMessageDialog(UserEditor.userEditor.frame,kick+" 已踢出!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			break;
		case"踢出未登录用户":
			int n = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "确认踢出未登陆用户?", "Warning", JOptionPane.YES_NO_OPTION);
			if(!(n==JOptionPane.YES_OPTION)){
				break;
			}
			Vector<ReadData> kickvector =new Vector<ReadData>();
			for (int i = 0; i < ServerMain.readdataVector.size(); i++) {
				if (ServerMain.readdataVector.get(i).curuser==null) {
					readData=ServerMain.readdataVector.get(i);
					kickvector.addElement(readData);
				}
			}
			for(int i1=0;i1<kickvector.size();i1++){
				readData=kickvector.elementAt(i1);
				String message1[]=new String[2];
				message1[0]="lycChatMessage";
				message1[1]="你被服务器踢出\n"+"未登陆!";
				Mydata mydata=new Mydata(message1,readData);
				SendData.senddata(mydata);
				SendData.waitsend(mydata);
				try {
					ServerMain.readdataVector.remove(readData);
					readData.t.stop();//结束进程
					readData.outputstream.close();
					readData.inputstream.close();
					System.out.println("Kicked:"+readData.conn.socket.getInetAddress().toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"已踢出所有未登陆用户!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			break;
		case"发布公告":
			String info = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入公告内容", "发布公告",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			int n1 = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "确认发布公告?", "Warning", JOptionPane.YES_NO_OPTION);
			if(n1==JOptionPane.YES_OPTION){
				String message1[]=new String[2];
				message1[0]="lycChatMessage";
				message1[1]="服务器公告\n"+info;
				SendData.senddata(new Mydata(message1,null));
				System.out.println("Info Sent!");
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"公告已发布!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"查询用户":
			String name = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入用户名", "查询用户",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			User user=new Userdaoimple().query(name);
			if (user==null) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户未找到!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户名:"+user.getName()+"\nID:"+user.getId()+"\nEmail:"+user.getEmail()+"\n密码:"+user.getPassword() ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"添加用户":
			User user2=new User();
			String username=(String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入用户名", "添加用户",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			user2.setName(username);
			user2.setEmail((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入Email", "添加用户",
			        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			user2.setPassword((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入密码", "添加用户",
			        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			Userdao dao=new Userdaoimple();
			try{
				if(username==null|username.equals("")|user2.getEmail()==null|user2.getEmail().equals("")|user2.getPassword()==null|user2.getPassword().equals("")){
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户名/Email/密码为空,添加失败!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
					break;
				}
			}catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户名/Email/密码为空,添加失败!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			boolean ifsuccess=dao.add(user2);
			if(ifsuccess){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户添加成功!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户添加失败!用户是否已经存在?" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"删除用户":
			int id=-1;
			try{
				id=Integer.parseInt((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
				        "输入需要删除的用户ID", "删除用户",
				        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			}catch (Exception e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"输入有误!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			User user3=new Userdaoimple().query(id);
			if(user3==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户不存在!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else{
				new Userdaoimple().delete(user3);
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户已成功删除!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"更改密码":
			int id1=-1;
			try{
				id1=Integer.parseInt((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
				        "输入需要更改密码的用户ID", "更改密码",
				        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			}catch (Exception e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"输入有误!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			String newpassword=(String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "输入需要新密码", "更改密码",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			if(newpassword==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"密码不能为空!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			if(newpassword.equals("")){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"密码不能为空!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			User user4=new Userdaoimple().query(id1);
			if(user4==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户不存在!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else{
				boolean ifsuccess1=new Userdaoimple().changePassword(user4,newpassword);
				if(ifsuccess1){
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户密码已更改!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"用户密码更改失败!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				}
			}
			break;
		}
	}
}
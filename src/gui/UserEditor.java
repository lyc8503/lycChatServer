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

public class UserEditor {//�û��������
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
		
		info=new JButton("��������");
		info.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.gridheight=1;
		constraints.ipadx=0;
		constraints.ipady=0;
		layout.setConstraints(info, constraints);
		frame.getContentPane().add(info);
		
		kick=new JButton("�߳��û�");
		kick.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=2;
		layout.setConstraints(kick, constraints);
		frame.getContentPane().add(kick);
		
		kicknotlogin=new JButton("�߳�δ��¼�û�");
		kicknotlogin.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=3;
		layout.setConstraints(kicknotlogin, constraints);
		frame.getContentPane().add(kicknotlogin);
		
		queryid=new JButton("��ѯ�û�");
		queryid.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=4;
		layout.setConstraints(queryid, constraints);
		frame.getContentPane().add(queryid);
		
		adduser=new JButton("����û�");
		adduser.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=5;
		layout.setConstraints(adduser, constraints);
		frame.getContentPane().add(adduser);
		
		deleteuser=new JButton("ɾ���û�");
		deleteuser.addActionListener(new MyActionListener());
		constraints.gridx=2;
		constraints.gridy=6;
		layout.setConstraints(deleteuser, constraints);
		frame.getContentPane().add(deleteuser);
		
		changepassword=new JButton("��������");
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
		case "�߳��û�":
			String kick = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "�����û���", "�߳��û�",
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
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�Ҳ�����Ϊ "+kick+" �������û�!" ,"Warning",JOptionPane.PLAIN_MESSAGE);
			}else {
				int n = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "ȷ���߳�"+kick+"?", "Warning", JOptionPane.YES_NO_OPTION);
				if (n==JOptionPane.YES_OPTION) {
					String reason = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
					        "�߳�����", "�߳��û�",
					        JOptionPane.QUESTION_MESSAGE,null, null, "����Ա��̨����");
					String message[]=new String[2];
					message[0]="lycChatMessage";
					message[1]="�㱻�������߳�\n"+reason;
					Mydata mydata=new Mydata(message,readData);
					SendData.senddata(mydata);
					SendData.waitsend(mydata);//�ȴ���Ϣ����
					try {
						ServerMain.readdataVector.remove(readData);
						readData.t.stop();//��������
						readData.outputstream.close();
						readData.inputstream.close();
						System.out.println("Kicked:"+kick);
						JOptionPane.showMessageDialog(UserEditor.userEditor.frame,kick+" ���߳�!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			break;
		case"�߳�δ��¼�û�":
			int n = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "ȷ���߳�δ��½�û�?", "Warning", JOptionPane.YES_NO_OPTION);
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
				message1[1]="�㱻�������߳�\n"+"δ��½!";
				Mydata mydata=new Mydata(message1,readData);
				SendData.senddata(mydata);
				SendData.waitsend(mydata);
				try {
					ServerMain.readdataVector.remove(readData);
					readData.t.stop();//��������
					readData.outputstream.close();
					readData.inputstream.close();
					System.out.println("Kicked:"+readData.conn.socket.getInetAddress().toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"���߳�����δ��½�û�!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			break;
		case"��������":
			String info = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "���빫������", "��������",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			int n1 = JOptionPane.showConfirmDialog(UserEditor.userEditor.frame, "ȷ�Ϸ�������?", "Warning", JOptionPane.YES_NO_OPTION);
			if(n1==JOptionPane.YES_OPTION){
				String message1[]=new String[2];
				message1[0]="lycChatMessage";
				message1[1]="����������\n"+info;
				SendData.senddata(new Mydata(message1,null));
				System.out.println("Info Sent!");
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�����ѷ���!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"��ѯ�û�":
			String name = (String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "�����û���", "��ѯ�û�",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			User user=new Userdaoimple().query(name);
			if (user==null) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û�δ�ҵ�!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û���:"+user.getName()+"\nID:"+user.getId()+"\nEmail:"+user.getEmail()+"\n����:"+user.getPassword() ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"����û�":
			User user2=new User();
			String username=(String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "�����û���", "����û�",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			user2.setName(username);
			user2.setEmail((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "����Email", "����û�",
			        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			user2.setPassword((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "��������", "����û�",
			        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			Userdao dao=new Userdaoimple();
			try{
				if(username==null|username.equals("")|user2.getEmail()==null|user2.getEmail().equals("")|user2.getPassword()==null|user2.getPassword().equals("")){
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û���/Email/����Ϊ��,���ʧ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
					break;
				}
			}catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û���/Email/����Ϊ��,���ʧ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			boolean ifsuccess=dao.add(user2);
			if(ifsuccess){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û���ӳɹ�!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û����ʧ��!�û��Ƿ��Ѿ�����?" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"ɾ���û�":
			int id=-1;
			try{
				id=Integer.parseInt((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
				        "������Ҫɾ�����û�ID", "ɾ���û�",
				        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			}catch (Exception e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"��������!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			User user3=new Userdaoimple().query(id);
			if(user3==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û�������!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else{
				new Userdaoimple().delete(user3);
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û��ѳɹ�ɾ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}
			break;
		case"��������":
			int id1=-1;
			try{
				id1=Integer.parseInt((String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
				        "������Ҫ����������û�ID", "��������",
				        JOptionPane.QUESTION_MESSAGE,null, null, ""));
			}catch (Exception e2) {
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"��������!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			String newpassword=(String)JOptionPane.showInputDialog(UserEditor.userEditor.frame,
			        "������Ҫ������", "��������",
			        JOptionPane.QUESTION_MESSAGE,null, null, "");
			if(newpassword==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"���벻��Ϊ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			if(newpassword.equals("")){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"���벻��Ϊ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				break;
			}
			User user4=new Userdaoimple().query(id1);
			if(user4==null){
				JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û�������!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
			}else{
				boolean ifsuccess1=new Userdaoimple().changePassword(user4,newpassword);
				if(ifsuccess1){
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û������Ѹ���!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(UserEditor.userEditor.frame,"�û��������ʧ��!" ,"Hint",JOptionPane.PLAIN_MESSAGE);
				}
			}
			break;
		}
	}
}
package gui;

import java.util.Vector;

import javax.swing.SwingUtilities;

import gui.ListOfUser;
import server.ReadData;
import server.ServerMain;

public class GUIManager implements Runnable{//����ˢ���û��б�
	static Runnable command;
	static Thread t;
	static{
		command=new Runnable() {
			public void run() {
				try {
					Vector<ListOfUser> ListOfUserVector=new Vector<ListOfUser>();
					for (int i = 0; i < ServerMain.readdataVector.size(); i++) {
						ReadData readData=ServerMain.readdataVector.get(i);
						ListOfUser user=new ListOfUser();//��ReadData�����ݶ�ȡ��ListOfUser����
						user.setIpaddress(readData.conn.socket.getInetAddress().getHostAddress());
						if(!(readData.curuser==null)){
							user.setUsername(readData.curuser.getName());
						}else{
							user.setUsername("Not Login!");
						}
						ListOfUserVector.addElement(user);
					}
					MainFrame.listmodel.removeAllElements();
					for (int a = 0; a < ListOfUserVector.size(); a++) {//����Vector
						ListOfUser user=ListOfUserVector.get(a);
						MainFrame.listmodel.addElement(user.getIpaddress()+" - "+user.getUsername());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
	}
	public GUIManager() {
		t=new Thread(this);
		t.start();
	}

	public void run() {//�����û��б�
		while(true){
			try {
				Thread.sleep(3000);
				SwingUtilities.invokeLater(command);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

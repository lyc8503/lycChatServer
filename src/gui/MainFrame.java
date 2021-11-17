package gui;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import database.Userdaoimple;
import server.ReadData;
import server.ServerMain;
import start.ServerStart;

public class MainFrame {
	public static JFrame frame;
	public static JList<Object> userlist;
	public static JLabel serverstatuslabel;
	public static JMenu menu;
	public static GridBagConstraints Constraints;
	public static GridBagLayout layout;
	public static JScrollPane userscrollpane;
	public static DefaultListModel<Object> listmodel;//���ڶ�̬�����û��б�
	//����listmodel.addElement(new String("123"))����
	public static JButton serverconfig;//�鿴������״̬��ť
	public static JButton serverstatus;
	public static JButton edituser;
	public static JButton chatmessage;
	
	public static JMenuBar menubar;//�˵�
	public static JMenu helpmenu;//�����˵�
	public static JMenuItem about;//�������
	public static JMenu servermenu;//�������˵�
	public static JMenuItem shutdown;//�رշ�����
	public static JMenuItem restart;//����������
	
	public MainFrame(){
		//��ʼˢ���û��˵�
		new GUIManager();
		new ChatMessageFrame();
		
		//��ʼ���༭�û�����
		UserEditor.userEditor=new UserEditor();
		
		frame=new JFrame("lycChatServer");//����������
		frame.addWindowListener(new MyWindowsActionLisntener());
		frame.setSize(500,300);
		frame.setResizable(false);
		layout =new GridBagLayout();
		frame.setLayout(layout);
		Constraints =new GridBagConstraints();
		
		//���ò˵�
		menubar=new JMenuBar();
		frame.setJMenuBar(menubar);
		helpmenu=new JMenu("����");
		about=new JMenuItem("����lycChat");
		helpmenu.add(about);
		
		servermenu=new JMenu("������");
		shutdown=new JMenuItem("�رշ�����");
		restart=new JMenuItem("����������");
		servermenu.add(shutdown);
		servermenu.add(restart);

		menubar.add(servermenu);
		menubar.add(helpmenu);
		restart.addActionListener(new MenuActionListener());
		shutdown.addActionListener(new MenuActionListener());
		about.addActionListener(new MenuActionListener());
		
		//��ʼ������
		userlist=new JList<Object>();//�û��б�
		listmodel=new DefaultListModel<Object>();
		userlist.setModel(listmodel);
		userscrollpane=new JScrollPane(userlist);
		Constraints.anchor=GridBagConstraints.WEST;
		Constraints.fill=GridBagConstraints.BOTH;
		Constraints.gridx=1;
		Constraints.gridy=2;
		Constraints.gridheight=4;
		Constraints.gridwidth=1;
		layout.setConstraints(userscrollpane,Constraints);
		frame.getContentPane().add(userscrollpane);
		serverconfig=new JButton("����������");
		
		serverstatuslabel=new JLabel("Unknown");//������״̬
		Constraints.gridx=1;
		Constraints.gridy=1;
		Constraints.gridheight=1;
		Constraints.gridwidth=1;
		layout.setConstraints(serverstatuslabel,Constraints);
		frame.getContentPane().add(serverstatuslabel);
		
		serverconfig=new JButton("����������");//����������
		Constraints.gridx=2;
		Constraints.gridy=1;
		Constraints.gridheight=1;
		Constraints.gridwidth=1;
		Constraints.insets.left=20;
		layout.setConstraints(serverconfig,Constraints);
		frame.getContentPane().add(serverconfig);
		serverconfig.addActionListener(new MenuActionListener());
		
		serverstatus=new JButton("������״̬");
		Constraints.gridx=2;
		Constraints.gridy=2;
		Constraints.gridheight=1;
		Constraints.gridwidth=1;
		Constraints.insets.left=20;
		Constraints.insets.top=10;
		layout.setConstraints(serverstatus,Constraints);
		frame.getContentPane().add(serverstatus);
		serverstatus.addActionListener(new MenuActionListener());
		
		edituser=new JButton("�û�����");
		Constraints.gridx=2;
		Constraints.gridy=3;
		Constraints.gridheight=1;
		Constraints.gridwidth=1;
		Constraints.insets.left=20;
		Constraints.insets.top=10;
		layout.setConstraints(edituser,Constraints);
		frame.getContentPane().add(edituser);
		edituser.addActionListener(new MenuActionListener());
		
		chatmessage=new JButton("��������");
		Constraints.gridx=2;
		Constraints.gridy=4;
		Constraints.gridheight=1;
		Constraints.gridwidth=1;
		Constraints.insets.left=20;
		Constraints.insets.top=10;
		layout.setConstraints(chatmessage,Constraints);
		frame.getContentPane().add(chatmessage);
		chatmessage.addActionListener(new MenuActionListener());
		
		frame.setVisible(true);
	}
}

class MyWindowsActionLisntener implements WindowListener{//�������ڹر�
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) {
		System.out.println("Windows Closed!");
		ServerStart.stop();
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}

class MenuActionListener implements ActionListener{//�����˵�����Ϊ
	public static StatusUpdater statusUpdater;
	public static JDialog serverstatusdialog;
	public static JDialog dialog;
	public static JTextField port;
	public static JTextField mysqladdress;
	public static JTextField mysqlname;
	public static JTextField mysqlpassword;
	public static JTextField welcomemessage;
	public static JTextField allowpasswordreset;
	public static JTextField smtpserver;
	public static JTextField smtpauth;
	public static JTextField smtpusername;
	public static JTextField smtppassword;
	public static JTextField smtpfrom;
	public static JTextField maxmessagelength;
	public static JTextField allowprivatechat;
	public static JTextField allowregister;
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()){
		case"����lycChat":
			 JOptionPane.showMessageDialog(MainFrame.frame,"����:lyc8503"+"\n"+"Email:lyc8503@gmail.com"+"\n"+"lycChat�汾:Beta 1.0" ,"lycChat",JOptionPane.PLAIN_MESSAGE);
			 break;
		case"�رշ�����":
			int n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ�رշ�������?", "Warning", JOptionPane.YES_NO_OPTION);   
			if (n == JOptionPane.YES_OPTION) {   
			    ServerStart.stop();
			}
			break;
		case"����������":
			int n1 = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ������������?", "Warning", JOptionPane.YES_NO_OPTION);   
			if (n1 == JOptionPane.YES_OPTION) {  
				System.out.println("Exiting...");
				MainFrame.serverstatuslabel.setText("Restarting...");
				MainFrame.frame.update(MainFrame.frame.getGraphics());
				try {
					ServerMain.serversocket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				for(int i=0;i<ServerMain.readdataVector.size();i++){
					ReadData readData=ServerMain.readdataVector.get(i);
					try {
						readData.inputstream.close();
						readData.outputstream.close();
						readData.conn.socket.getInputStream().close();
						} catch (IOException e) {
						e.printStackTrace();
					}
					readData.conn.t.stop();
					readData.t.stop();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Restarting...");
				try {
					Runtime.getRuntime().exec("java -jar "+System.getProperty("java.class.path"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			    System.exit(0);	
			}
			break;
		case"����������":
			if(!(dialog==null)){
				break;
			}
			System.out.println("Config Dialog Opened!");
			dialog=new JDialog(MainFrame.frame,"Server Config", false);//���������öԻ���
			dialog.getContentPane().setLayout(new GridLayout(15,1))
			;
			dialog.getContentPane().add(new JLabel("Port:"));
			port=new JTextField();
			port.setText(String.valueOf(ServerStart.serverport));
			dialog.getContentPane().add(port);
			
			dialog.getContentPane().add(new JLabel("MySQLAddress:"));
			mysqladdress=new JTextField();
			mysqladdress.setText(ServerStart.databaseaddress);
			dialog.getContentPane().add(mysqladdress);
			
			dialog.getContentPane().add(new JLabel("MySQLName:"));
			mysqlname=new JTextField();
			mysqlname.setText(ServerStart.databasename);
			dialog.getContentPane().add(mysqlname);
			
			dialog.getContentPane().add(new JLabel("MySQLPassword:"));
			mysqlpassword=new JTextField();
			mysqlpassword.setText(ServerStart.databasepasswd);
			dialog.getContentPane().add(mysqlpassword);
			
			dialog.getContentPane().add(new JLabel("WelcomeMessage:"));
			welcomemessage=new JTextField();
			welcomemessage.setText(ServerStart.welcomemessage);
			dialog.getContentPane().add(welcomemessage);
			
			dialog.getContentPane().add(new JLabel("AllowPasswordReset:"));
			allowpasswordreset=new JTextField();
			allowpasswordreset.setText(String.valueOf(ServerStart.allowresetpassword));
			dialog.getContentPane().add(allowpasswordreset);
			
			dialog.getContentPane().add(new JLabel("SMTPServer:"));
			smtpserver=new JTextField();
			smtpserver.setText(ServerStart.smtpserver);
			dialog.getContentPane().add(smtpserver);
			
			dialog.getContentPane().add(new JLabel("SMTPAuth"));
			smtpauth=new JTextField();
			smtpauth.setText(String.valueOf(ServerStart.smtpauth));
			dialog.getContentPane().add(smtpauth);
			
			dialog.getContentPane().add(new JLabel("SMTPUsername"));
			smtpusername =new JTextField();
			smtpusername.setText(ServerStart.smtpusername);
			dialog.getContentPane().add(smtpusername);
			
			dialog.getContentPane().add(new JLabel("SMTPPassword:"));
			smtppassword=new JTextField();
			smtppassword.setText(ServerStart.smtppassword);
			dialog.getContentPane().add(smtppassword);
			
			dialog.getContentPane().add(new JLabel("EmailFrom:"));
			smtpfrom=new JTextField();
			smtpfrom.setText(ServerStart.smtpfrom);
			dialog.getContentPane().add(smtpfrom);
			
			dialog.getContentPane().add(new JLabel("MaxmessageLength:"));
			maxmessagelength=new JTextField();
			maxmessagelength.setText(String.valueOf(ServerStart.maxmessagelength));
			dialog.getContentPane().add(maxmessagelength);
			
			dialog.getContentPane().add(new JLabel("AllowPrivateChat:"));
			allowprivatechat=new JTextField();
			allowprivatechat.setText(String.valueOf(ServerStart.allowprivatechat));
			dialog.getContentPane().add(allowprivatechat);
			
			dialog.getContentPane().add(new JLabel("AllowRegister:"));
			allowregister=new JTextField();
			allowregister.setText(String.valueOf(ServerStart.allowregister));
			dialog.getContentPane().add(allowregister);
			
			JButton cancelbutton=new JButton("Cancel");
			JButton submitbutton=new JButton("Save");
			cancelbutton.addActionListener(new ConfigDialogActionListener());
			submitbutton.addActionListener(new ConfigDialogActionListener());
			dialog.getContentPane().add(cancelbutton);
			dialog.getContentPane().add(submitbutton);
			dialog.addWindowListener(new ConfigDialogWindowsActionListener());
			dialog.setResizable(false);
			dialog.setSize(400,300);
			dialog.setModal(false);
			dialog.setVisible(true);
			break;
		case"������״̬":
			if(!(serverstatusdialog==null)){
				break;
			}
			serverstatusdialog=new JDialog(MainFrame.frame,"Server Status",false);
			serverstatusdialog.getContentPane().add(new StatusPane());
			serverstatusdialog.setSize(250,160);
			serverstatusdialog.setResizable(false);
			serverstatusdialog.setVisible(true);
			serverstatusdialog.addWindowListener(new StatusDialogWindowsActionListener());
			try {
				StatusUpdater.thread.stop();
			} catch (Exception e) {
				//Ignore
			}
			statusUpdater=new StatusUpdater();
			System.out.println("Server Status");
			break;
		case"�û�����":
			UserEditor.userEditor.showframe();
			break;
		case"��������":
			ChatMessageFrame.showframe();
			break;
		}
	}
}

@SuppressWarnings("serial")
class StatusPane extends JPanel{//������״̬�Ի���
	public void paint(Graphics g){
		g.drawString("Server Status:",20,20);
		g.drawString("Running...",20,40);
		g.drawString("Online User: "+ServerMain.readdataVector.size(), 20, 60);
		g.drawString("Registered User: "+new Userdaoimple().queryregisteredusernum(), 20, 80);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		g.drawString("Server Time: "+df.format(new Date()), 20, 100);
		g.drawString("Running Time: "+(System.currentTimeMillis()-ServerStart.starttime)/1000+"s", 20, 120);
	}
}

class ConfigDialogActionListener implements ActionListener{//���ڷ�������������ActionListener
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Cancel":
			int n = JOptionPane.showConfirmDialog(null, "û�б�������,ȷ��Ҫ�˳���?", "Warning", JOptionPane.YES_NO_OPTION);
			if(n==JOptionPane.YES_OPTION){
				System.out.println("Cancelled!");
				MenuActionListener.dialog.setVisible(false);
				MenuActionListener.dialog=null;
				break;
			}
		case"Save":
			int n1 = JOptionPane.showConfirmDialog(null, "��������������������,�Ƿ����", "Warning", JOptionPane.YES_NO_OPTION);
			if(n1==JOptionPane.YES_OPTION){
				MenuActionListener.dialog.setVisible(false);
				MenuActionListener.dialog=null;
				MainFrame.serverstatuslabel.setText("Restarting...");
				MainFrame.frame.update(MainFrame.frame.getGraphics());
				System.out.println("Deleting Old Config...");
				File config=new File("lycChatConfig.properties");
				config.delete();
				System.out.println("Saving...");//��������
				try {
					config.createNewFile();
					FileWriter fileWriter=new FileWriter(config);
					fileWriter.write("port="+MenuActionListener.port.getText()+
							"\nmysqladdress="+MenuActionListener.mysqladdress.getText()+
							"\nmysqlname="+MenuActionListener.mysqlname.getText()+
							"\nmysqlpasswd="+MenuActionListener.mysqlpassword.getText()+
							"\nwelcomemessage="+MenuActionListener.welcomemessage.getText()+
							"\nallowpasswordreset="+MenuActionListener.allowpasswordreset.getText()+
							"\nsmtpserver="+MenuActionListener.smtpserver.getText()+
							"\nsmtpauth="+MenuActionListener.smtpauth.getText()+
							"\nsmtpusername="+MenuActionListener.smtpusername.getText()+
							"\nsmtppassword="+MenuActionListener.smtppassword.getText()+
							"\nsmtpfrom="+MenuActionListener.smtpfrom.getText()+
							"\nmaxmessagelength="+MenuActionListener.maxmessagelength.getText()+
							"\nallowprivatechat="+MenuActionListener.allowprivatechat.getText()+
							"\nallowregister="+MenuActionListener.allowregister.getText());
					fileWriter.close();
					System.out.println("port="+MenuActionListener.port.getText()+
							"\nmysqladdress="+MenuActionListener.mysqladdress.getText()+
							"\nmysqlname="+MenuActionListener.mysqlname.getText()+
							"\nmysqlpasswd="+MenuActionListener.mysqlpassword.getText()+
							"\nwelcomemessage="+MenuActionListener.welcomemessage.getText()+
							"\nallowpasswordreset="+MenuActionListener.allowpasswordreset.getText()+
							"\nsmtpserver="+MenuActionListener.smtpserver.getText()+
							"\nsmtpauth="+MenuActionListener.smtpauth.getText()+
							"\nsmtpusername="+MenuActionListener.smtpusername.getText()+
							"\nsmtppassword="+MenuActionListener.smtppassword.getText()+
							"\nsmtpfrom="+MenuActionListener.smtpfrom.getText()+
							"\nmaxmessagelength="+MenuActionListener.maxmessagelength.getText()+
							"\nallowprivatechat="+MenuActionListener.allowprivatechat.getText()+
							"\nallowregister="+MenuActionListener.allowregister.getText());
					System.out.println("Restarting...");
					try {
						ServerMain.serversocket.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					for(int i=0;i<ServerMain.readdataVector.size();i++){
						ReadData readData=ServerMain.readdataVector.get(i);
						try {
							readData.inputstream.close();
							readData.outputstream.close();
							readData.conn.socket.getInputStream().close();
							} catch (IOException e2) {
							e2.printStackTrace();
						}
						readData.conn.t.stop();
						readData.t.stop();
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					Runtime.getRuntime().exec("java -jar "+System.getProperty("java.class.path"));
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
	}
}

class ConfigDialogWindowsActionListener implements WindowListener{//���ڷ�������������WindowsListener
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		System.out.println("Cancelled!");
		MenuActionListener.dialog.setVisible(false);
		MenuActionListener.dialog=null;
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}

class StatusDialogWindowsActionListener implements WindowListener{//���ڷ�����״̬����WindowsListener
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	@SuppressWarnings("deprecation")
	public void windowClosing(WindowEvent e) {
		MenuActionListener.serverstatusdialog.setVisible(false);
		MenuActionListener.serverstatusdialog=null;
		StatusUpdater.thread.stop();
		MenuActionListener.statusUpdater=null;
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}

class StatusUpdater implements Runnable{//���ڷ�����״̬����״̬����
	public static Thread thread;
	public StatusUpdater() {
		thread=new Thread(this);
		thread.start();
	}
	public void run(){
		while(true){
			MenuActionListener.serverstatusdialog.update(MenuActionListener.serverstatusdialog.getGraphics());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
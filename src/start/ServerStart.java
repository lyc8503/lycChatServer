package start;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

import database.DBUtil;
import gui.MainFrame;
import server.ReadData;
import server.ServerMain;

public class ServerStart {
	public static String databaseaddress;//配置文件内容
	public static String databasename;
	public static String databasepasswd;
	public static String welcomemessage;
	public static boolean allowresetpassword;
	public static boolean smtpauth;
	public static String smtpusername;
	public static String smtppassword;
	public static String smtpfrom;
	public static int serverport;
	public static String smtpserver;
	public static long starttime;
	public static int maxmessagelength;
	public static boolean allowprivatechat;
	public static boolean allowregister;
	
	public static ServerMain servermain;
	public static void main(String args[]){//程序开始
		starttime=System.currentTimeMillis();
		System.out.println("欢迎使用lycChatServer,本软件与lycChatClient共同使用");
		System.out.println("首次使用请填写生成的Properties文件");
		System.out.println("并安装好MySql Server后运行");
		System.out.println("如需启用找回密码功能,请正确填写SMTP服务器相关信息");
		System.out.println("警告:本软件连接没有加密,请确保在私人网络中使用");
		new MainFrame();//创建主窗口
		System.out.println("Starting...");
		MainFrame.serverstatuslabel.setText("Starting...");
		File conffile=new File("lycChatConfig.properties");
		if(!conffile.exists()){//如配置文件不存在,生成默认文件
			System.out.println("Properties Not Found,Creating...");
			try {
				conffile.createNewFile();
				FileWriter configoutput=new FileWriter(conffile);
				configoutput.write("port="+"\n"+"mysqladdress=jdbc:mysql://localhost:9999/UserDatabase"+"\n"+"mysqlname="+"\n"+"mysqlpasswd="+"\n"+"welcomemessage="+"Welcome!"+"\n"+"allowpasswordreset=false\n"+"smtpserver=localhost\n"+"smtpauth=true\n"+"smtpusername=username\n"+"smtppassword=passwd\n"+"smtpfrom=test@test.com\n"+"maxmessagelength=1000\n"+"allowprivatechat=true\n"+"allowregister=true");
				configoutput.close();
				JOptionPane.showMessageDialog(MainFrame.frame, "没有检测到配置文件,使用默认\n请正确填写lycChatConfig.properties","Hint", JOptionPane.PLAIN_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				ServerStart.stop();
			}
		}
		Properties prop=new Properties();//读取配置文件
		try {
			MainFrame.serverstatuslabel.setText("Reading Properties...");
			prop.load(new FileInputStream(conffile));
			serverport=Integer.parseInt((String) prop.get("port"));
			databaseaddress=(String) prop.get("mysqladdress");
			databasename=(String) prop.get("mysqlname");
			databasepasswd=(String) prop.get("mysqlpasswd");
			welcomemessage=(String)prop.get("welcomemessage");
			allowresetpassword=Boolean.parseBoolean((String) prop.get("allowpasswordreset"));
			smtpauth=Boolean.parseBoolean((String)prop.get("smtpauth"));
			smtpusername=(String)prop.get("smtpusername");
			smtppassword=(String)prop.get("smtppassword");
			smtpfrom=(String)prop.get("smtpfrom");
			smtpserver=(String)prop.get("smtpserver");
			maxmessagelength=Integer.parseInt((String)prop.get("maxmessagelength"));
			allowprivatechat=Boolean.parseBoolean((String)prop.get("allowprivatechat"));
			allowregister=Boolean.parseBoolean((String)prop.get("allowregister"));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.frame, "请正确填写lycChatConfig.properties","Hint", JOptionPane.PLAIN_MESSAGE);
			ServerStart.stop();
		}

		DBUtil.checkdb();
		servermain=new ServerMain();
	}
	@SuppressWarnings("deprecation")
	public static void stop(){//结束程序
		try {
			MainFrame.serverstatuslabel.setText("Exiting...");
			MainFrame.frame.update(MainFrame.frame.getGraphics());
			System.out.println("Exiting...");
			ServerMain.serversocket.close();
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
			System.out.println("Stopped!");
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}
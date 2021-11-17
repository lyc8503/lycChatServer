package server;

import java.net.ServerSocket;
import java.util.Vector;

import gui.MainFrame;
import start.ServerStart;

public class ServerMain {//开始网络服务
	public static Vector<ReadData> readdataVector;
	public static ServerSocket serversocket;
	public ServerMain(){
		readdataVector=new Vector<ReadData>();
		MainFrame.serverstatuslabel.setText("Running...");
		try {
			serversocket=new ServerSocket(ServerStart.serverport);
		} catch (Exception e) {
			e.printStackTrace();
			ServerStart.stop();
		}
		new ListenforConnection();//开始监听端口
		new SendData();//开始发送数据进程
		System.out.println("Listening for Connection...");
	}
}
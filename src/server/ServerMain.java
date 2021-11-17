package server;

import java.net.ServerSocket;
import java.util.Vector;

import gui.MainFrame;
import start.ServerStart;

public class ServerMain {//��ʼ�������
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
		new ListenforConnection();//��ʼ�����˿�
		new SendData();//��ʼ�������ݽ���
		System.out.println("Listening for Connection...");
	}
}
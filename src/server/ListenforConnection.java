package server;

import java.net.Socket;

public class ListenforConnection implements Runnable{
	public Thread t;
	public Socket socket;
	public ListenforConnection(){//���̲߳���
		t=new Thread(this);
		t.start();
	}
	public void run() {
		try {
			socket=ServerMain.serversocket.accept();//��ȡ���Ӻ����Vector,ѭ��
			System.out.println(socket.getInetAddress().getHostAddress()+"has connected!");
			ServerMain.readdataVector.add(new ReadData(this));
		} catch (Exception e) {
			//Ignore
		}
		new ListenforConnection();
	}
}

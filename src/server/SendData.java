package server;

import java.io.IOException;
import java.util.Vector;

public class SendData implements Runnable{//发送数据进程
	public static Vector<Mydata> mydatalist;
	public static Thread t;
	public SendData() {
		System.out.println("SendData Start!");
		mydatalist=new Vector<Mydata>();
		t=new Thread(this);
		t.setPriority(6);
		t.start();
	}
	public static void senddata(Mydata mydata){
		mydatalist.addElement(mydata);//加入待处理线程
	}
	public static void waitsend(Mydata mydata){
		while (mydatalist.contains(mydata)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void run() {
		while (true) {
			if(!mydatalist.isEmpty()){
				Mydata mydata=mydatalist.elementAt(0);
				if(mydata.getData()==null){//判断数据是否为空
					mydatalist.removeElement(mydata);
				}else{
					mydatalist.removeElement(mydata);
					if(mydata.getReadData()==null){//发送给群体
						for(ReadData readData:ServerMain.readdataVector){//foreach循环
							String datas[]=mydata.getData();
							for(String data:datas){
								try {
									readData.outputstream.writeUTF(data);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}else{
						ReadData readData=mydata.getReadData();
						for(String data:mydata.getData()){
							try {
								readData.outputstream.writeUTF(data);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
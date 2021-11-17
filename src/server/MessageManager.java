package server;

import java.text.SimpleDateFormat;
import java.util.Date;

import database.User;

public class MessageManager {
	public static void sendprivatemessage(String toname,String message,String byname){
		String message3[]=new String[3];
		message3[0]="privateMessage";
		message3[1]=byname;
		message3[2]=message;
		ReadData readData=null;
		for(ReadData readData2:ServerMain.readdataVector){
			if(readData2.curuser!=null){
				if(readData2.curuser.getName().equals(toname)){
					readData=readData2;
				}
			}
		}
		if(readData!=null){
			SendData.senddata(new Mydata(message3,readData));
		}
	}
	public static void newprivatechat(User by,ReadData to){
		System.out.println("NewPrivateChat From "+by.getName()+" to "+to.curuser.getName());
		String message2[]=new String[2];
		message2[0]="newpricatechat";
		message2[1]=by.getName();
		SendData.senddata(new Mydata(message2,to));
	}
	public static void sendpublicmessage(User by,String message){
			String message1[]=new String[5];
			message1[0]="publicMessage";
			message1[1]=by.getName();
			message1[2]=message;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=df.format(new Date());
			message1[3]=date;
			message1[4]=by.getEmail();
			SendData.senddata(new Mydata(message1,null));
	}
}

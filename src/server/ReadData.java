package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import database.User;
import database.Userdao;
import database.Userdaoimple;
import gui.ChatMessageFrame;
import start.ServerStart;

public class ReadData implements Runnable{//从客户端读取信息
	public ListenforConnection conn;
	public static Userdao dao;
	static{
		dao=new Userdaoimple();
	}
	public Thread t;
	public User curuser;
	public DataInputStream inputstream;
	public DataOutputStream outputstream;
	ReadData(ListenforConnection conn){
		this.conn=conn;
		t=new Thread(this);
		t.start();
		dao=new Userdaoimple();
	}
	@SuppressWarnings("deprecation")
	public void run() {
		Socket socket=conn.socket;
		try {
			inputstream=new DataInputStream(socket.getInputStream());
			outputstream=new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connect failed!Connection Removed");
			ServerMain.readdataVector.remove(this);
			try {
				outputstream.close();
				inputstream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			t.stop();
		}
		while(true){//开始从客户端读取数据
			try {
				switch(inputstream.readUTF()){
				case"lycChat":
					switch(inputstream.readUTF()){
					case"privatemessage":
						String toname=inputstream.readUTF();
						String message=inputstream.readUTF();
						if(curuser==null){
							System.out.println("Forbidden Request!");
							ServerMain.readdataVector.remove(this);
							t.stop();
						}else{
							if(ServerStart.maxmessagelength<message.length()){
								String message19[]=new String[2];
								message19[0]="lycChatMessage";
								message19[1]="发送的消息长度超过上限!";
								SendData.senddata(new Mydata(message19,this));
							}else {
								System.out.println("Private Message From "+curuser.name+" to "+toname+": "+message);
								ChatMessageFrame.messages.addElement("Private Message From "+curuser.name+" to "+toname+": "+message);
								ChatMessageFrame.flush();
								MessageManager.sendprivatemessage(toname,message,curuser.getName());//发送私聊消息
							}
						}
						break;
					case"newprivatemessage":
						if(curuser==null){
							System.out.println("Forbidden Request!");
							ServerMain.readdataVector.remove(this);
							t.stop();
						}else{
							String to=inputstream.readUTF();
							boolean online=false;
							ReadData readData=null;
							for(ReadData readData1:ServerMain.readdataVector){
								if(!(readData1.curuser==null)){
									if(readData1.curuser.getName().equals(to)){
										online=true;
										readData=readData1;
									}
								}
							}
							if(online){
								if(this.curuser.getName().equals(to)){
									String message19[]=new String[2];
									message19[0]="lycChatMessage";
									message19[1]="不能向自己发送私聊请求!";
									SendData.senddata(new Mydata(message19,this));
								}else{
									MessageManager.newprivatechat(this.curuser,readData);
									String message18[]=new String[3];
									message18[0]="lycChatSystemInfo";
									message18[1]="privatechatsuccess";
									message18[2]=readData.curuser.getName();
									SendData.senddata(new Mydata(message18,this));
								}
							}else{
								String message17[]=new String[2];
								message17[0]="lycChatMessage";
								message17[1]="对方不在线!";
								SendData.senddata(new Mydata(message17,this));
							}
						}
						break;
					case"message":
						if(curuser==null){
							System.out.println("Forbidden Request!");
							ServerMain.readdataVector.remove(this);
							t.stop();
						}else{
							String message99=inputstream.readUTF();
							if(ServerStart.maxmessagelength<message99.length()){
								String message3[]=new String[2];
								message3[0]="lycChatMessage";
								message3[1]="发送的消息长度超过上限!";
								SendData.senddata(new Mydata(message3,this));
							}else {
								System.out.println("Public Message:"+message99);
								ChatMessageFrame.messages.addElement("Public Message : "+message99);
								ChatMessageFrame.flush();
								MessageManager.sendpublicmessage(curuser,message99);//发送公开消息
							}
						}
						break;
					case"exit":
						System.out.println(socket.getInetAddress().getHostAddress()+"Log out!");
						ServerMain.readdataVector.remove(this);
						t.stop();
						break;
					case"login":
						String username=inputstream.readUTF();
						String password=inputstream.readUTF();
						User u=dao.query(username);//查询正在登陆的用户
						if(u==null){
							String message4[]=new String[2];
							message4[0]="lycChatMessage";
							message4[1]="用户不存在!";
							SendData.senddata(new Mydata(message4,this));
						}else{
							if(!(u.getPassword().equals(password))){
								String message5[]=new String[2];
								message5[0]="lycChatMessage";
								message5[1]="用户密码错误!";
								SendData.senddata(new Mydata(message5,this));
							}else{
								boolean online=false;
								for(ReadData readData:ServerMain.readdataVector){
									if(!(readData.curuser==null)){
										if(readData.curuser.getName().equals(u.getName())){
											online=true;
										}
									}
								}
								if(online){
									String message15[]=new String[2];
									message15[0]="lycChatMessage";
									message15[1]="请不要重复登陆!";
									SendData.senddata(new Mydata(message15,this));
								}else{
									String message20[]=new String[2];
									message20[0]="lycChatSystemInfo";//告知登陆成功
									message20[1]="successlogin";
									SendData.senddata(new Mydata(message20,this));
									String message1[]=new String[2];
									message1[0]="lycChatMessage";
									message1[1]="登陆成功!";
									SendData.senddata(new Mydata(message1,this));
									System.out.println("Login :"+u.getName());
									curuser=u;//将用户设置为当前用户
									String message2[]=new String[2];
									message2[0]="lycChatMessage";
									message2[1]=start.ServerStart.welcomemessage;
									SendData.senddata(new Mydata(message2,this));
								}
							}
						}
						break;
					case"reg":
						String regusername=inputstream.readUTF();//注册新账号
						String regemail=inputstream.readUTF();
						String regpassword=inputstream.readUTF();
						if(!ServerStart.allowregister){
							String message5[]=new String[2];
							message5[0]="lycChatMessage";
							message5[1]="服务器不允许新用户注册!";
							SendData.senddata(new Mydata(message5,this));
							break;
						}
						User regu=dao.query(regusername);//查询用用户
						if(!(regu==null)){
							String message6[]=new String[2];
							message6[0]="lycChatMessage";
							message6[1]="用户已经存在!";
							SendData.senddata(new Mydata(message6,this));
						}else{
							boolean ifregsuccess;
							User regrequestuser=new User();//客户端发送的用户
							regrequestuser.setName(regusername);
							regrequestuser.setEmail(regemail);
							regrequestuser.setPassword(regpassword);
							ifregsuccess=dao.add(regrequestuser);//将用户设置为当前用户
							if(ifregsuccess==true){
								String message7[]=new String[6];
								message7[0]="lycChatSystemInfo";//告知登陆成功
								message7[1]="successlogin";
								message7[2]="lycChatMessage";
								message7[3]="注册成功!";
								message7[4]="lycChatMessage";
								message7[5]=start.ServerStart.welcomemessage;
								SendData.senddata(new Mydata(message7,this));
								curuser=dao.query(regusername);
							}else{
								String message8[]=new String[2];
								message8[0]="lycChatMessage";
								message8[1]="服务器错误,请联系服务器管理员!";
								SendData.senddata(new Mydata(message8,this));
							}
						}
						break;
					case"forgetpassword":
						dao=new Userdaoimple();
						String forgetpasswordusername=inputstream.readUTF();
						if(ServerStart.allowresetpassword){
							User user=dao.query(forgetpasswordusername);
							if(!(user==null)){
								boolean ifsuccess=EmailUtil.sendEmail(user.getEmail(),user.getPassword());
								if(ifsuccess){
									String message21[]=new String[2];
									message21[0]="lycChatMessage";
									message21[1]="已将密码发送到你的邮箱,请查收!";
									SendData.senddata(new Mydata(message21,this));
								}else{
									String message9[]=new String[2];
									message9[0]="lycChatMessage";
									message9[1]="SMTP服务器连接失败!";
									SendData.senddata(new Mydata(message9,this));
								}
							}else{
								String message10[]=new String[2];
								message10[0]="lycChatMessage";
								message10[1]="用户未找到!";
								SendData.senddata(new Mydata(message10,this));
							}
						}else{
							String message11[]=new String[2];
							message11[0]="lycChatMessage";
							message11[1]="服务器未开启找回密码功能!";
							SendData.senddata(new Mydata(message11,this));
						}
						break;
					case"changepassword":
						try {
							String username2=inputstream.readUTF();
							String oldpassword2=inputstream.readUTF();
							String newpassword2=inputstream.readUTF();
							User user=new User();
							user.setName(username2);
							user.setPassword(oldpassword2);
							if(dao.check(user)){
								boolean ifsuccess=dao.changePassword(user,newpassword2);
								if(ifsuccess){
									String message12[]=new String[2];
									message12[0]="lycChatMessage";
									message12[1]="密码更改成功!";
									SendData.senddata(new Mydata(message12,this));
								}else{
									String message13[]=new String[2];
									message13[0]="lycChatMessage";
									message13[1]="更改密码失败!";
									SendData.senddata(new Mydata(message13,this));
								}
							}else{
								String message14[]=new String[2];
								message14[0]="lycChatMessage";
								message14[1]="原密码错误!";
								SendData.senddata(new Mydata(message14,this));
							}
						} catch (Exception e) {
							String message15[]=new String[2];
							message15[0]="lycChatMessage";
							message15[1]="服务器错误,请联系服务器管理员!";
							SendData.senddata(new Mydata(message15,this));
						}
						break;
					default:
						String message16[]=new String[2];
						message16[0]="lycChatMessage";
						message16[1]="Invaild Packet";
						SendData.senddata(new Mydata(message16,this));
						break;
					}
					break;
				default:
					String message17[]=new String[2];
					message17[0]="lycChatMessage";
					message17[1]="Invaild Packet";
					SendData.senddata(new Mydata(message17,this));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connect failed!Connection Removed");
				ServerMain.readdataVector.remove(this);
				try {
					outputstream.close();
					inputstream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				t.stop();
			}
		}
	}
}

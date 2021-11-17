package server;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import start.ServerStart;

public class EmailUtil {//发送邮件
	public static boolean sendEmail(String to,String password){
		Properties properties=System.getProperties();
		properties.setProperty("mail.smtp.host",ServerStart.smtpserver);
		Session session;
		if(ServerStart.smtpauth){//密码认证
			properties.put("mail.smtp.auth", "true");
			session=Session.getDefaultInstance(properties,new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication()
		        {
		         return new PasswordAuthentication(ServerStart.smtpusername, ServerStart.smtppassword); //发件人邮件用户名、密码
		        }
			});
		}else{
			session=Session.getDefaultInstance(properties);
		}
		MimeMessage message=new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(ServerStart.smtpfrom));//发送信息
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject("lycChat找回密码");
			message.setDescription("lycChat");
			message.setText("[lycChat]找回密码:你的密码为"+password+",可在软件中更改密码\n(若非本人操作,请忽略)");
			Transport.send(message);
			System.out.println("Success Send Email to:"+to);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}

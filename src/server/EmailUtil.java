package server;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import start.ServerStart;

public class EmailUtil {//�����ʼ�
	public static boolean sendEmail(String to,String password){
		Properties properties=System.getProperties();
		properties.setProperty("mail.smtp.host",ServerStart.smtpserver);
		Session session;
		if(ServerStart.smtpauth){//������֤
			properties.put("mail.smtp.auth", "true");
			session=Session.getDefaultInstance(properties,new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication()
		        {
		         return new PasswordAuthentication(ServerStart.smtpusername, ServerStart.smtppassword); //�������ʼ��û���������
		        }
			});
		}else{
			session=Session.getDefaultInstance(properties);
		}
		MimeMessage message=new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(ServerStart.smtpfrom));//������Ϣ
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject("lycChat�һ�����");
			message.setDescription("lycChat");
			message.setText("[lycChat]�һ�����:�������Ϊ"+password+",��������и�������\n(���Ǳ��˲���,�����)");
			Transport.send(message);
			System.out.println("Success Send Email to:"+to);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}

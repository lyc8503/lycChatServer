package gui;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatMessageFrame {
	public static Vector<String> messages=new Vector<String>();
	public static JFrame frame;
	public static JTextArea textArea;
	public ChatMessageFrame() {
		frame=new JFrame("Messages");
		textArea=new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane=new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane);
	}
	public static void flush(){
		StringBuilder builder=new StringBuilder();
		for(String s:messages){
			builder.append(s+"\n");
		}
		while(messages.size()>1000){
			messages.removeElementAt(0);
		}
		textArea.setText(builder.toString());
	}
	public static void showframe(){
		frame.setLocation(0,0);
		frame.setVisible(true);
		frame.setSize(500, 300);
	}
}
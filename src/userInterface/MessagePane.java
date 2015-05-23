package userInterface;

import java.awt.Font;
import java.awt.Color;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.BorderFactory;

import commandCenter.MinervaMessage;

/**用來顯示一些minerva要告訴使用者的訊息的視窗，show一些資訊而已，多個title area只是為了好看和美觀*/
public class MessagePane extends JScrollPane{
	private static MessagePane messagePane;
	private JTextPane messageArea;
	private JTextArea titleArea;
	private StyledDocument doc;
	
	/**5種字色變化的attribute*/
	private MutableAttributeSet stdout;		
	private MutableAttributeSet stdin;
	private MutableAttributeSet stderr;
	private MutableAttributeSet warning;
	private MutableAttributeSet success;
	
	/**導io用的pipe，用來導System.err*/
	private PipedInputStream pis;
	private PipedOutputStream pos;
	
	/**設定MessagePane和其columnHeaderView*/
	protected MessagePane(){
		messageArea=new JTextPane();
		messageArea.setFont(new Font("Courier New",Font.PLAIN,13));
		messageArea.setEditable(false);
		doc=messageArea.getStyledDocument();
		this.setTextAttribute();
		this.setViewportView(messageArea);
		
		/**設定title列*/
		titleArea=new JTextArea("Output");
		titleArea.setEditable(false);
		titleArea.setBackground(new Color(200,200,200));
		this.setColumnHeaderView(titleArea);
		
		/**設定外框及背景*/
		this.setViewportBorder(BorderFactory.createEtchedBorder());
		this.setBorder(BorderFactory.createCompoundBorder());
		this.setBackground(Color.LIGHT_GRAY);

		/**將System.err導入pipe中*/
		try {
			pis=new PipedInputStream();
			pos=new PipedOutputStream(pis);
			System.setErr(new PrintStream(pos));
			new StderrReader(pis).start();
		} catch (IOException e) {}
	}
	public static MessagePane getInstance(){
		if(messagePane==null)
			messagePane=new MessagePane();
		return messagePane;
	}
	
	/**由訊息的開頭來決定要用什麼顏色表達*/
	public void appendErrMessage(String message){
		if(message.startsWith(MinervaMessage.SUCCESS_TITLE))
			this.appendOutputMessage(message,success);
		else if(message.startsWith(MinervaMessage.ERROR_TITLE))
			this.appendOutputMessage(message,stderr);
		else if(message.startsWith(MinervaMessage.WARNING_TITLE))
			this.appendOutputMessage(message,warning);
		else
			this.appendOutputMessage(message, stdout);
	}
	
	private void appendOutputMessage(String message,MutableAttributeSet style){
		try {
			doc.insertString(doc.getLength(), message, style);
		} catch (BadLocationException e) {}
	}
	
	public void clearText(){
		messageArea.setText("");
	}
	/**設定5種字色初始*/
	private void setTextAttribute(){
		stdin=new SimpleAttributeSet();
		stdout=new SimpleAttributeSet();
		stderr=new SimpleAttributeSet();
		warning=new SimpleAttributeSet();
		success=new SimpleAttributeSet();
		StyleConstants.setForeground(stdin, Color.CYAN);
		StyleConstants.setForeground(stdout, Color.BLACK);
		StyleConstants.setForeground(stderr, Color.RED);
		StyleConstants.setForeground(warning, new Color(200,150,20));
		StyleConstants.setForeground(success, Color.GREEN);
	}
	
	/**讀取System.err的thread*/
	class StderrReader extends Thread{
		PipedInputStream pis;
		byte[] buffer;
		public StderrReader(PipedInputStream pis){
			this.pis=pis;
			this.buffer=new byte[1024];
		}
		
		public void run(){
			while(true){
				try {
					final int length=pis.read(buffer);
					if(length==-1)
						continue;
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							String str=new String(buffer,0,length);
							MessagePane.this.appendErrMessage(str);
						}
					});
				} catch (IOException e) {}
			}
		}
	}
}

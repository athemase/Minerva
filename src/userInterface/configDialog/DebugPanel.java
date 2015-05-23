package userInterface.configDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.Spring;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.util.HashMap;

import recordProcessor.RecordProcessor;

/**設定jdb和gdb位置用的視窗，具有一些簡單的gui元件。*/
public class DebugPanel extends ConfigPanel implements ActionListener{
	private JTextField valueField[];
	private JButton browseButton[];
	
	private HashMap settings;
	private String jdbPath;
	private String gdbPath;
	
	public DebugPanel(){
		this.setName("Debug Path");
		valueField=new JTextField[2];
		browseButton=new JButton[2];
		
		valueField[0]=new JTextField(30);
		valueField[1]=new JTextField(30);
		
		browseButton[0]=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg"));
		browseButton[1]=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg"));
		browseButton[0].addActionListener(this);
		browseButton[1].addActionListener(this);
		this.setUILayout();
		this.loadSetting();
	}
	/**使用spring layout來排板面*/
	private void setUILayout(){
		this.setLayout(new SpringLayout());
		SpringLayout.Constraints constraint;
		for(int i=0;i<valueField.length;i++){
			constraint=new SpringLayout.Constraints(Spring.constant(40),
					Spring.constant(i*70+50),Spring.constant(260),Spring.constant(25)); 
			this.add(valueField[i],constraint);
		}
		for(int i=0;i<browseButton.length;i++){
			constraint=new SpringLayout.Constraints(Spring.constant(310),
					Spring.constant(i*70+50),Spring.constant(40),Spring.constant(23)); 
			this.add(browseButton[i],constraint);
		}
		
	}
	/**讀取使用者的舊設定。*/
	private void loadSetting(){
		settings=RecordProcessor.getInstance().getUserSettingMap();
		if((jdbPath=(String)settings.get("JDB"))!=null)
			valueField[0].setText(jdbPath);
		if((gdbPath=(String)settings.get("GDB"))!=null)
			valueField[1].setText(gdbPath);
	}
	/**使用者取消時，讀回舊設定*/
	public void cancel() {
		this.loadSetting();
	}
	/**使用者確定時，將新設定寫入setting。*/
	public void confirm() {
		jdbPath=valueField[0].getText();
		gdbPath=valueField[1].getText();
		settings.put("JDB", jdbPath);
		settings.put("GDB", gdbPath);
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 60);
		g.drawRect(20, 100, 350, 60);
		g.setColor(Color.BLACK);
		g.drawString("JDB PATH", 30, 35);
		g.drawString("GDB PATH", 30, 105);
	}
	
	/**按下browse按鈕時，有個JFileChooser來讓使用者選取檔案，也就是選jdb和gdb path*/
	public void actionPerformed(ActionEvent event){
		JFileChooser jfc=new JFileChooser();
		int result=jfc.showOpenDialog(this);
		if(result==JFileChooser.CANCEL_OPTION)
			return;
		if(event.getSource()==browseButton[0])
			valueField[0].setText(jfc.getSelectedFile().toString());
		else if(event.getSource()==browseButton[1])
			valueField[1].setText(jfc.getSelectedFile().toString());
	}
}

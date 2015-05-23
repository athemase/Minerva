package userInterface.configDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import recordProcessor.RecordProcessor;
import userInterface.MainFrame;

/**設定button和視窗的style用的。在windows下 jdk1.6.0有4種style，我自已畫2種button style
 * 所以，可以有8種組合*/
public class StylePanel extends ConfigPanel{
	private JComboBox styleBox;
	private JComboBox buttonBox;
	private HashMap settings;
	
	private HashMap<String,String> styleMap;
	private final String buttonStyleName[]={"JavaStyle","WindowStyle"};
	
	/**讀入jdk預設的4種look and feel*/
	public StylePanel(){
		this.setName("GUI Style");
		styleMap=new HashMap<String,String>();
		
		LookAndFeelInfo lafi[]=UIManager.getInstalledLookAndFeels();
		for(int i=0;i<lafi.length;i++){
			String styleStr=lafi[i].toString();
			String styleName=styleStr.substring(styleStr.lastIndexOf("[")+1,styleStr.lastIndexOf(" "));
			String styleValue=styleStr.substring(styleStr.lastIndexOf(" ")+1,styleStr.lastIndexOf("]"));
			styleMap.put(styleName,styleValue);
		}
		
		styleBox=new JComboBox(styleMap.keySet().toArray());
		buttonBox=new JComboBox(buttonStyleName);
		
		this.setUILayout();
		this.loadSettings();
	}
	/**使用spring layout*/
	private void setUILayout(){
		this.setLayout(new SpringLayout());
		SpringLayout.Constraints constraint;
		
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(50),Spring.constant(300),Spring.constant(25)); 
		this.add(styleBox,constraint);
		
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(120),Spring.constant(300),Spring.constant(25)); 
		this.add(buttonBox,constraint);
	}
	
	/**讀取舊設定*/
	private void loadSettings(){
		settings=RecordProcessor.getInstance().getUserSettingMap();
		if(settings.get("Style")!=null){
			String currentStyle=(String)settings.get("Style");
			for(int i=0;i<styleBox.getItemCount();i++){
				if(currentStyle.equals(styleMap.get(styleBox.getItemAt(i).toString()))){
					styleBox.setSelectedIndex(i);
					break;
				}
			}
		}
		if(settings.get("ButtonStyle")!=null){
			String currentButtonStyle=(String)settings.get("ButtonStyle");
			for(int i=0;i<buttonBox.getItemCount();i++){
				if(currentButtonStyle.equals(buttonBox.getItemAt(i).toString())){
					buttonBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}
	
	/**使用者按下取消時，讀取舊設定*/
	public void cancel(){
		this.loadSettings();
	}
	/**使用者按下確定時，將新設定寫入setting*/
	public void confirm(){
		String styleName=styleBox.getSelectedItem().toString();
		settings.put("Style",styleMap.get(styleName));
		settings.put("ButtonStyle", buttonBox.getSelectedItem().toString());
		MainFrame.getInstance().updateUI();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 60);
		g.drawRect(20, 100, 350, 60);
		g.setColor(Color.BLACK);
		g.drawString("WINDOW STYLE", 30, 35);
		g.drawString("BUTTON STYLE", 30, 105);
	}
}

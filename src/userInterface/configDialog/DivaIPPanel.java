package userInterface.configDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import recordProcessor.RecordProcessor;

/**設定diva的ip和port位置用的panel。currentSelection是代表選的是local host還是
 * other host。*/
public class DivaIPPanel extends ConfigPanel implements ItemListener{
	private JRadioButton localHostButton;
	private JRadioButton otherHostButton;
	private ButtonGroup buttonGroup;
	private JTextField ipAddressField;
	private JTextField portField;
	
	private String divaAddress;
	private String divaPort;
	private String currentSelection;
	
	private HashMap<String,String> settings;
	
	public DivaIPPanel(){
		this.setName("Diva Address");
		
		localHostButton=new JRadioButton("LocalHost",false);
		otherHostButton=new JRadioButton("OtherHost",false);
		ipAddressField=new JTextField(15);
		ipAddressField.setEditable(false);
		portField=new JTextField(5);
		portField.setEditable(false);
		
		localHostButton.addItemListener(this);
		otherHostButton.addItemListener(this);
		buttonGroup=new ButtonGroup();
		buttonGroup.add(localHostButton);
		buttonGroup.add(otherHostButton);
		this.setUILayout();
		this.loadSetting();
	}
	/**使用spring layout來排板面*/
	private void setUILayout(){
		this.setLayout(new SpringLayout());
		SpringLayout.Constraints constraint=new SpringLayout.Constraints(
				Spring.constant(30),Spring.constant(50),Spring.constant(100),
				Spring.constant(20)); 
		this.add(localHostButton,constraint);
		constraint=new SpringLayout.Constraints(Spring.constant(30),
				Spring.constant(80),Spring.constant(100),Spring.constant(20)); 
		this.add(otherHostButton,constraint);
		constraint=new SpringLayout.Constraints(Spring.constant(60),
				Spring.constant(110),Spring.constant(200),Spring.constant(20)); 
		this.add(ipAddressField,constraint);
		constraint=new SpringLayout.Constraints(Spring.constant(270),
				Spring.constant(110),Spring.constant(40),Spring.constant(20)); 
		this.add(portField,constraint);
	}
	/**讀取舊設定*/
	private void loadSetting(){
		// Added by Yu-Jen Peng, 2012/05/07
		// null 會導致下面 NullPointerException，若為 null 則指定為"0"(即設 diva 在 localhost)
		if(currentSelection == null){
			currentSelection = "0";
		}
		
		settings=RecordProcessor.getInstance().getUserSettingMap();
		if(settings.get("IPAddress")!=null)
			divaAddress=(String)settings.get("IPAddress");
		if(settings.get("Port")!=null)
			divaPort=(String)settings.get("Port");
		if(settings.get("ServerSelect")!=null)
			currentSelection=(String)settings.get("ServerSelect");
		if(currentSelection.equals("0")){
			localHostButton.setSelected(true);
		}
		else if(currentSelection.equals("1")){
			otherHostButton.setSelected(true);
			ipAddressField.setText(divaAddress);
			portField.setText(divaPort);
		}
	}
	/**使用者按下確定時，將新設定寫入setting*/
	public void confirm(){
		if(currentSelection.equals("1")){
			divaAddress=ipAddressField.getText();
			divaPort=portField.getText();
		}
		settings.put("IPAddress", divaAddress);
		settings.put("Port",divaPort);
		settings.put("ServerSelect",currentSelection);
	}
	/**當使用者按下取消時，讀取舊設定*/
	public void cancel(){
		this.loadSetting();
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 130);
		g.setColor(Color.BLACK);
		g.drawString("DIVA ADDRESS", 30, 35);
	}
	/**radio button的select時發生的。*/
	public void itemStateChanged(ItemEvent event){
		if(event.getStateChange()==ItemEvent.SELECTED){
			if(event.getSource()==localHostButton){
				divaAddress="127.0.0.1";
				divaPort="2000";
				currentSelection="0";
				ipAddressField.setEditable(false);
				portField.setEditable(false);
			}
			else if(event.getSource()==otherHostButton){
				currentSelection="1";
				ipAddressField.setEditable(true);
				portField.setEditable(true);
			}
		}
	}
}

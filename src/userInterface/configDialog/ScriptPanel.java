package userInterface.configDialog;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import recordProcessor.BreakPointScript;


public class ScriptPanel extends JDialog implements ActionListener{
	private static ScriptPanel singleton = null;
	private JButton ok;
	private JButton cancel;
	private JLabel fieldName;
	private JLabel selectName;
	private JRadioButton option[];
	private JPanel panel;
	private JTextField scripts;
	private ButtonGroup radioGroup;
	private BreakPointScript myScript;
	private ScriptPanel(){
		this.myScript = new BreakPointScript("",false);		
		this.setTitle("Script");
		this.setResizable(true);
		this.setAlwaysOnTop(true);		
		this.setLocation(250,250);
		this.setSize(210,200);
		this.setVisible(true);		
		panelInit();		
		this.add(panel);
	}
	private void panelInit(){
		System.out.println("   script init.");
		
		this.panel = new JPanel(new GridBagLayout());
		this.panel.setBackground(Color.white);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.fieldName = new JLabel("Visualize :");
		this.panel.add(fieldName,c);
		this.scripts = new JTextField(20);
		this.scripts.setEditable(true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		this.panel.add(scripts,c);
		this.selectName = new JLabel("None Stop :");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		this.panel.add(selectName,c);
		this.option = new JRadioButton[2];		
		this.option[0] = new JRadioButton("Yes",false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		this.panel.add(option[0],c);
		this.option[1] = new JRadioButton("No",false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,5,0,5);
		c.gridx = 2;
		c.gridy = 1;
		this.panel.add(option[1],c);
		this.radioGroup = new ButtonGroup();
		this.radioGroup.add(option[0]);
		this.radioGroup.add(option[1]);	
		this.ok = new JButton("OK");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,5,0,0);
		c.gridx = 0;
		c.gridy = 2;
		this.panel.add(ok,c);
		this.ok.addActionListener(this);
		this.cancel = new JButton("CANCEL");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,5);
		c.gridx = 2;
		c.gridy = 2;
		this.panel.add(cancel,c);
		this.cancel.addActionListener(this);
		this.panel.setVisible(true);
	}	
	
	public static ScriptPanel getInstance(){
		if(singleton == null){
			singleton = new ScriptPanel();
		}
		else{
			singleton.scripts.setText("");
			//singleton.option[0].setEnabled(true);
			singleton.setVisible(true);
		}		
		return singleton;
	}
	private void confirm(){		
		if(option[0].isSelected()){
			/**通知observer要更新資料*/
			myScript.setScriptData(scripts.getText(),true);
		}
		else if(option[1].isSelected()){
			/**通知observer要更新資料*/
			myScript.setScriptData(scripts.getText(),false);
		}
	}
	private void cancel(){
		
	}
	public void actionPerformed(ActionEvent event) {
		this.setVisible(false);
		if(event.getSource() == ok){
			confirm();			
		}
		else if(event.getSource() == cancel){
			cancel();
		}		
	}
	public BreakPointScript getScript(){
		return this.myScript;
	}
}

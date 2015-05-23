package userInterface.configDialog;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JPanel;

import recordProcessor.RecordProcessor;
import userInterface.MainFrame;

/**minerva的設定視窗，為了設定diva的位置，jdb gdb的位置，和ui的style用的，具有3個設定用panel和一個
 * 裝有他們的JTabbedPane。和2個button(ok 和 cancel)*/
public class ConfigDialog extends JDialog implements ActionListener{
	private static ConfigDialog configFrame;
	private JTabbedPane configTabPane;
	private ConfigPanel divaIPPanel;
	private ConfigPanel debugPanel;
	private ConfigPanel stylePanel;
	private JButton OKButton,cancelButton;
	private JPanel buttonPanel;
	
	protected ConfigDialog(){
		this.setTitle("Config");
		this.setSize(400,300);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		
		configTabPane=new JTabbedPane();
		this.setConfigPanel();
		this.setButtonPanel();
		
		this.setLayout(new BorderLayout());
		this.add(configTabPane,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		this.setIconImage(MainFrame.logoImage);
		this.setLocation(300,150);
		this.setVisible(true);
	}
	public static ConfigDialog getInstance(){
		if(configFrame==null)
			configFrame=new ConfigDialog();
		else
			configFrame.setVisible(true);
		return configFrame;
	}
	private void setConfigPanel(){
		divaIPPanel=new DivaIPPanel();
		configTabPane.add(divaIPPanel);
		debugPanel=new DebugPanel();
		configTabPane.add(debugPanel);
		stylePanel=new StylePanel();
		configTabPane.add(stylePanel);
	}
	private void setButtonPanel(){
		buttonPanel=new JPanel();
		OKButton=new JButton("  OK  ");
		cancelButton=new JButton("Cancel");
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.add(OKButton);
		buttonPanel.add(cancelButton);
	}
	
	/**呼叫所有的confirm或者cancel。並且update設定*/
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==OKButton){
			for(int i=0;i<configTabPane.getComponentCount();i++){
				((ConfigPanel)configTabPane.getComponentAt(i)).confirm();
			}
			RecordProcessor.getInstance().updateUserSetting();
		}
		else if(event.getSource()==cancelButton){
			for(int i=0;i<configTabPane.getComponentCount();i++){
				((ConfigPanel)configTabPane.getComponentAt(i)).cancel();
			}
		}
		this.setVisible(false);
	}
}
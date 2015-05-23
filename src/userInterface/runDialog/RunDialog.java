package userInterface.runDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import userInterface.MainFrame;
import userInterface.TabPane;
import userInterface.Scroller;

public class RunDialog extends JDialog implements ActionListener{
	private static RunDialog runDialog;
	private JButton OKButton;
	private JButton cancelButton;
	private JButton easyButton;
	private JPanel buttonPanel;
	
	private JTabbedPane runTabPane;
	private RunPanel javaPanel;
	private RunPanel cppPanel;
	private RunPanel projectPanel;
	
	protected RunDialog(){
		this.setTitle("RUN");
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		
		runTabPane=new JTabbedPane();
		this.setButtonPanel();
		this.setRunPanel();
		
		this.setLayout(new BorderLayout());
		this.add(runTabPane,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		this.setIconImage(MainFrame.logoImage);
		
		this.setSize(400,450);
		this.setLocation(250,110);
		this.setVisible(true);
	}
	private void setRunPanel(){
		javaPanel=new RunJavaPanel();
		cppPanel=new RunCppPanel();
		projectPanel=new RunProjectPanel();
		runTabPane.add(javaPanel);
		runTabPane.add(cppPanel);
		runTabPane.add(projectPanel);
	}
	
	private void setButtonPanel(){
		buttonPanel=new JPanel();
		OKButton=new JButton("  RUN  ");
		cancelButton=new JButton("Cancel");
		easyButton=new JButton("Easy Run");
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
		easyButton.addActionListener(this);
		buttonPanel.add(OKButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(easyButton);
	}
	public static RunDialog getInstance(){
		if(runDialog==null)
			runDialog=new RunDialog();
		else
			runDialog.setVisible(true);
		
		runDialog.selectComportPanel();
		runDialog.getSelectedPanel().setHistoryValue();
		return runDialog;
	}
	
	/** 自動將預設的panel設出來 依照目前使用者所看的視窗是那種語言*/
	private void selectComportPanel(){
		Scroller currentScroller=TabPane.getInstance().getCurrentScroller();
		if(currentScroller!=null && currentScroller.getCoverFile()!=null){
			String subFileName=currentScroller.getCoverFile().toString();
			if(subFileName.contains("."))
				subFileName=subFileName.substring(subFileName.lastIndexOf("."));
			else
				return;
			if(subFileName.equalsIgnoreCase(".java"))
				runTabPane.setSelectedComponent(javaPanel);
			else if(subFileName.equalsIgnoreCase(".c")||subFileName.equalsIgnoreCase(".cpp")
					||subFileName.equalsIgnoreCase(".h"))
				runTabPane.setSelectedComponent(cppPanel);
		}
	}
	
	private RunPanel getSelectedPanel(){
		return (RunPanel)runTabPane.getSelectedComponent();
	}
	
	public void actionPerformed(ActionEvent event){
		this.setVisible(false);
		if(event.getSource()==OKButton){
			((RunPanel)runTabPane.getSelectedComponent()).confirm();
		}
		else if(event.getSource()==cancelButton){
			((RunPanel)runTabPane.getSelectedComponent()).cancel();
		}
		else if(event.getSource()==easyButton){
			((RunPanel)runTabPane.getSelectedComponent()).easyRun();
			this.setVisible(true);
		}
	}
}

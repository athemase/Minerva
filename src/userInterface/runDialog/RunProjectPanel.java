package userInterface.runDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class RunProjectPanel extends RunPanel implements ActionListener{
	JTextField commandField;
	JButton browseButton;
	File lastVisitedDic;
	public RunProjectPanel(){
		this.setName("Run Minerva Project");
		commandField=new JTextField(70);
		browseButton=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg")); 
		browseButton.addActionListener(this);
		this.setUILayout();
	}
	private void setUILayout(){
		this.setLayout(new SpringLayout());
		SpringLayout.Constraints constraint;
		
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(50),Spring.constant(260),Spring.constant(25)); 
		this.add(commandField,constraint);
		
		constraint=new SpringLayout.Constraints(Spring.constant(310),
				Spring.constant(50),Spring.constant(40),Spring.constant(23)); 
		this.add(browseButton,constraint);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 60);
		g.setColor(Color.BLACK);
		g.drawString("MINERVA PROJECT",40,35);
	}
	
	public void cancel() {}
	public void confirm() {}
	public void easyRun() {}
	public void setHistoryValue(){}
	
	public void actionPerformed(ActionEvent event){}
}

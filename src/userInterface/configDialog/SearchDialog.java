package userInterface.configDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import userInterface.MainFrame;

public class SearchDialog extends JDialog{
	private static SearchDialog searchDialog;
	private JTextField searchField;
	private JTextField replaceField;
	private JButton[] commandButton;
	private JPanel buttonPanel;
	private SearchPanel searchPanel;
	public SearchDialog(){
		this.setTitle("Search");
		this.setSize(300,300);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		
		this.setComponent();
		this.setUILayout();
		
		this.setIconImage(MainFrame.logoImage);
		this.setLocation(400,150);
		this.setVisible(true);
	}
	public static SearchDialog getInstance(){
		if(searchDialog==null)
			searchDialog=new SearchDialog();
		else
			searchDialog.setVisible(true);
		return searchDialog;
	}
	private void setComponent(){
		searchField=new JTextField();
		replaceField=new JTextField();
		commandButton=new JButton[3];
		commandButton[0]=new JButton("Find Next");
		commandButton[1]=new JButton("Replace");
		commandButton[2]=new JButton("Replace All");

		searchPanel=new SearchPanel();
		buttonPanel=new JPanel();
		for(int i=0;i<commandButton.length;i++)
			buttonPanel.add(commandButton[i]);
		
	}
	private void setUILayout(){
		this.setLayout(new BorderLayout());
		this.add(searchPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		searchPanel.setLayout(new SpringLayout());
		
		SpringLayout.Constraints constraint;
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(50),Spring.constant(200),Spring.constant(25)); 
		searchPanel.add(searchField,constraint);
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(130),Spring.constant(200),Spring.constant(25)); 
		searchPanel.add(replaceField,constraint);
	}
	
	private class SearchPanel extends JPanel{
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.WHITE);
			g.drawRect(20, 30, 240, 60);
			g.drawRect(20, 110, 240, 60);
			g.setColor(Color.BLACK);
			g.drawString("SEARCH",40,35);
			g.drawString("REPLACE TO", 40, 115);
		}
	}
}

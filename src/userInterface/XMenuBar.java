package userInterface;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import commandCenter.CommandCenter;
import uiCommand.FileCommand;
import uiCommand.HelpCommand;
import uiCommand.SearchCommand;
import userInterface.configDialog.ConfigDialog;
import userInterface.configDialog.OptionPanel;
import userInterface.runDialog.RunDialog;

/**MINERVA的menubar，沒有太多可以介紹的，非常簡單。*/
public class XMenuBar extends JMenuBar implements MenuConst,ActionListener{
	private static final int MENU_NUM=6;
	private static final int ITEM_NUM=23; 
	private static final String[] itemName={"New File","Open File","Save File"
		,"Save As...","Exit","Redo","Undo","Copy","Cut","Paste","Search"
		,"Run","Stop","Step","Next","Cont","Visualize","Unfold","Fold","Animate"
		,"Option","About","Author"};
	private JMenu[] menu;
	private JMenuItem[] menuItem;
	public XMenuBar(){
		this.setMenu();
		this.setMenuItem();
	}
	private void setMenu(){
		menu=new JMenu[MENU_NUM];
		menu[FILE]=new JMenu("File");
		menu[EDIT]=new JMenu("Edit");
		menu[SEARCH]=new JMenu("Search");
		menu[DEBUG]=new JMenu("Debug");
		menu[CONFIG]=new JMenu("Config");
		menu[HELP]=new JMenu("Help");
		for(int i=0;i<MENU_NUM;i++){
			this.add(menu[i]);
		}
	}
	private void setMenuItem(){
		final String longSpace="        ";
		menuItem=new JMenuItem[ITEM_NUM];
		
		for(int i=0;i<ITEM_NUM;i++){
			menuItem[i]=new JMenuItem(itemName[i]+longSpace);
			menuItem[i].setBackground(new Color(240,240,240));
			menuItem[i].addActionListener(this);
		}
		menu[FILE].add(menuItem[NEW]);
		menu[FILE].add(menuItem[OPEN]);
		menu[FILE].add(menuItem[SAVE]);
		menu[FILE].add(menuItem[SAVEAS]);
		menu[FILE].addSeparator();
		menu[FILE].add(menuItem[EXIT]);
		
		menu[EDIT].add(menuItem[REDO]);
		menu[EDIT].add(menuItem[UNDO]);
		menu[EDIT].add(menuItem[COPY]);
		menu[EDIT].add(menuItem[CUT]);
		menu[EDIT].add(menuItem[PASTE]);
		
		menu[SEARCH].add(menuItem[FIND]);
		
		menu[DEBUG].add(menuItem[RUN]);
		menu[DEBUG].add(menuItem[STOP]);
		menu[DEBUG].add(menuItem[STEP]);
		menu[DEBUG].add(menuItem[NEXT]);
		menu[DEBUG].add(menuItem[CONT]);
		menu[DEBUG].addSeparator();
		menu[DEBUG].add(menuItem[VISUALIZE]);
		menu[DEBUG].add(menuItem[UNFOLD]);
		menu[DEBUG].add(menuItem[FOLD]);
		menu[DEBUG].addSeparator();
		menu[DEBUG].add(menuItem[ANIMATE]);
		
		menu[CONFIG].add(menuItem[OPTION]);
		
		menu[HELP].add(menuItem[ABOUT]);
		menu[HELP].add(menuItem[AUTHOR]);
	}
	/**發生事件時，看是那個menuitem引起的，就做對應的事，值得注意的是在visualize unfold fold的時後，
	 * 可以按照使用者的游標位置，送個default的value過去。*/
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==menuItem[NEW]){
			FileCommand.newFile();
		}
		else if(event.getSource()==menuItem[OPEN]){
			FileCommand.openFile();
		}
		else if(event.getSource()==menuItem[SAVE]){
			FileCommand.saveFile();
		}
		else if(event.getSource()==menuItem[SAVEAS]){
			FileCommand.saveAsFile();
		}
		else if(event.getSource()==menuItem[EXIT]){
			try {
				FileCommand.exitProgram();
			} catch (IOException exception){}
		}
		else if(event.getSource()==menuItem[REDO]){}
		else if(event.getSource()==menuItem[UNDO]){}
		
		else if(event.getSource()==menuItem[RUN]){
			RunDialog.getInstance();
		}
		else if(event.getSource()==menuItem[STOP]){
			CommandCenter.stop();
		}
		else if(event.getSource()==menuItem[STEP]){
			CommandCenter.step();
		}
		else if(event.getSource()==menuItem[NEXT]){
			CommandCenter.next();
		}
		else if(event.getSource()==menuItem[CONT]){
			CommandCenter.cont();
		}
		else if(event.getSource()==menuItem[VISUALIZE]){
			OptionPanel.visualize();
		}
		else if(event.getSource()==menuItem[UNFOLD]){
			OptionPanel.unfold();
		}
		else if(event.getSource()==menuItem[FOLD]){
			OptionPanel.fold();
		}
		else if(event.getSource()==menuItem[ANIMATE]){
			CommandCenter.animate();
		}
		else if(event.getSource()==menuItem[OPTION]){
			ConfigDialog.getInstance();
		}
		else if(event.getSource()==menuItem[ABOUT]){
			HelpCommand.about();
		}
		else if(event.getSource()==menuItem[AUTHOR]){
			HelpCommand.author();
		}
		else if(event.getSource()==menuItem[FIND]){
			SearchCommand.search();
		}
	}
}

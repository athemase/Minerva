package userInterface.configDialog;

import javax.swing.JOptionPane;

import userInterface.Scroller;
import userInterface.TabPane;
import commandCenter.CommandCenter;

public class OptionPanel {
	public static void visualize(){
		Scroller currentScroller;
		String initMessage="";
		if((currentScroller=TabPane.getInstance().getCurrentScroller())!=null)
			initMessage=currentScroller.getCodePane().getSelectedText();
		
		String message=JOptionPane.showInputDialog("Visualize : ",initMessage);
		if((message!=null)&&(!message.equals("")))
			CommandCenter.visualize(message.trim());
	}
	public static void unfold(){
		Scroller currentScroller;
		String initMessage="";
		if((currentScroller=TabPane.getInstance().getCurrentScroller())!=null)
			initMessage=currentScroller.getCodePane().getSelectedText();
		
		String message=JOptionPane.showInputDialog("Unfold : ",initMessage);
		if((message!=null)&&(!message.equals("")))
			CommandCenter.unfold(message.trim());
	}
	public static void fold(){
		Scroller currentScroller;
		String initMessage="";
		if((currentScroller=TabPane.getInstance().getCurrentScroller())!=null)
			initMessage=currentScroller.getCodePane().getSelectedText();
		
		String message=JOptionPane.showInputDialog("Fold : ",initMessage);
		if((message!=null)&&(!message.equals("")))
			CommandCenter.fold(message.trim());
	}
}

package uiCommand;

import javax.swing.JOptionPane;

/**作者版本資訊*/
public class HelpCommand {
	public static void about(){
		String message="This is Minerva Editor \n Version is 0.8\n";
		JOptionPane.showMessageDialog(null,message,"About",JOptionPane.INFORMATION_MESSAGE);
	}
	public static void author(){
		String message="Author :  Neige\n Version Date : 2008/10/07";
		JOptionPane.showMessageDialog(null,message,"About",JOptionPane.INFORMATION_MESSAGE);
	}
}
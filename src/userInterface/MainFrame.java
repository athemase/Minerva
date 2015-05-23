package userInterface;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import data.DebugData;
import recordProcessor.RecordProcessor;
import uiCommand.FileCommand;

/**整個minerva的主要frame，所有的ui元件都在此之下，包含4個基礎元件，toolbar，menubar
 * tabpane，messagepane，和data之間溝通的的observer pattern也是傳到此處。*/
public class MainFrame extends JFrame implements WindowListener,Observer{
	private static MainFrame mainFrame;
	public static final Image logoImage=Toolkit.getDefaultToolkit().getImage("Icon\\JavaStyle\\eyeball.jpg");
	
	private XToolBar toolBar;
	private XMenuBar menuBar;
	private TabPane tabPane;
	private MessagePane messagePane;
	private DebugData data;
	private String buttonStyle="JavaStyle";
	
	protected MainFrame(){
		super("Minerva7.0");
		this.setIconImage(logoImage);
		
		this.setLookAndFeel();
		this.setComponent();
		this.getContentPane().setBackground(Color.BLACK);
		
		this.addWindowListener(this);
		this.updateUI();
		this.setSize(800,600);
		this.setVisible(true);
		
		data=DebugData.getInstance();
		data.addObserver(this);
	}
	
	public static MainFrame getInstance(){
//		System.out.println("MainFrame.getInstance");
		if(mainFrame==null)
		{
//			System.out.println("New a MainFrame");
			mainFrame=new MainFrame();
		}
		return mainFrame;
	}
	/** 將menubar toolbar tabpane messagepane加入主視窗 */
	private void setComponent(){
		toolBar=new XToolBar(buttonStyle);
		menuBar=new XMenuBar();
		tabPane=TabPane.getInstance();
		messagePane=MessagePane.getInstance();
		tabPane.loadOpenedFile();
		
		this.setLayout(new GridBagLayout());
		this.setJMenuBar(menuBar);
		this.layoutComponents(0,0,0,toolBar);
		this.layoutComponents(10,1,100,tabPane);
		this.layoutComponents(3, 2, 50, messagePane);
	}
	
	private void layoutComponents(int wy,int y,int ipady,JComponent comp){
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.anchor=GridBagConstraints.NORTHEAST;
		c.weightx=1;
		c.weighty=wy;
		c.gridx=0;
		c.gridy=y;
		c.ipady=ipady;
		this.add(comp,c);
	}
	/** 設定 look and feel，也就是視窗要用那一種按鈕外觀，我寫了2種按鈕外觀，java的style的
	 * 和windows的style的*/
	public void setLookAndFeel(){
		RecordProcessor record=RecordProcessor.getInstance();
		buttonStyle=(String)record.getUserSettingMap().get("ButtonStyle");
		if(buttonStyle==null)
			buttonStyle="WindowStyle";
	}
	
	/**observer pattern。當data變動時，會自動呼叫這個method。目前的事件分成5種
	 * 1.程式的當前點移動了，是當使用者run step next cont時會發生的
	 * 2.debugger停止了，當debug結束時或者使用者按下stop鈕時發生的
	 * 3.debugger開始了，當使用者按下run時發生
	 * 4.set valuemap，和第1種情況一樣，只是為了來設定value map
	 * 5.output message，當minerva有訊息要output在最下面的message pane時*/
	public void update(Observable obs, Object argment) {
		String command=(String)argment;

		if(command.equals("POSITION CHANGE"))
			tabPane.setToBreakPointPos(data.getCurFile(),data.getCurLine(), data.getCurProject());
		else if(command.equals("DEBUGGER STOP"))
			tabPane.clearAllPoint();
		else if(command.equals("SET VALUEMAP"))
			tabPane.setValueMap(data.getCurFile(), data.getValueMap(), data.getCurProject());
		else if(command.equals("DEBUGGER START"))
			messagePane.clearText();
	}
	public void updateUI(){
		RecordProcessor processor=RecordProcessor.getInstance();
		String style=(String)processor.getUserSettingMap().get("Style");
		
		// Modified by yjpeng, 2011 summer
		if(style == null)
		{
			style = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
//		final String buttonStyle=(String)processor.getUserSettingMap().get("ButtonStyle");
		final String buttonStyle = "WindowStyle";
		try {
			UIManager.setLookAndFeel(style);
			
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					SwingUtilities.updateComponentTreeUI(MainFrame.this);
					toolBar.setButtonIcon(buttonStyle);
					SwingUtilities.updateComponentTreeUI(toolBar);
				}
			});
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}
	}
	
	public void windowActivated(WindowEvent arg0){}
	public void windowClosed(WindowEvent arg0){}
	/** 當視窗關閉的時後，要做一些善後的工作*/
	public void windowClosing(WindowEvent arg0){
		try{
			FileCommand.exitProgram();
		}catch(IOException e){}
	}
	public void windowDeactivated(WindowEvent arg0){}
	public void windowDeiconified(WindowEvent arg0){}
	public void windowIconified(WindowEvent arg0){}
	public void windowOpened(WindowEvent arg0){}
}

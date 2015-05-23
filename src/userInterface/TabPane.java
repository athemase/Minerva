package userInterface;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTabbedPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import commandCenter.MinervaMessage;

import recordProcessor.BreakPointData;
import recordProcessor.RecordProcessor;
import uiCommand.FileCommand;

/**UI的核心元件之一，控制整個minerva的tabbed pane，將所有的scroller包含在其中，並管理之。
 * 另外有個彈跳視窗控制popup，popup是這個檔案的inner class，在下面。current scoller記得
 * 目前使用者在看的是那個視窗。*/
public class TabPane extends JTabbedPane implements ChangeListener{
	private static TabPane tabPane;
	Scroller currentScroller;
	Popup popup;
	
	protected TabPane(){
		popup=new Popup();
		this.addMouseListener(new MouseHandler());
		this.addChangeListener(this);
	}
	
	public static synchronized TabPane getInstance(){
		if(tabPane==null)
			tabPane=new TabPane();
		return tabPane;
	}
	
	/**將OpenedFile.ini的內容記錄的，上次開啟沒關的檔案，從新開啟出來。*/
	public void loadOpenedFile(){
		Vector openedFile=RecordProcessor.getInstance().getOpenFileVector();
		Iterator it=openedFile.iterator();
		while(it.hasNext()){
			String fileName=(String)it.next();
			File file=new File(fileName);
			if(file.exists())
				FileCommand.openFile(file);
		}
	}
	
	/**加入一個新的panel，是不含檔案的，意即，開新檔案*/
	public void addPanel(){
		this.addPanel("UnNamed",null,"");
	}
	/**加入一個新的panel是有含檔案的，意即，開啟舊檔*/
	public void addPanel(String fileName,File file,String text){
		CodePane codePane=new CodePane(file,text);
		Scroller scroller=new Scroller(codePane);
		currentScroller=scroller;
		scroller.setCoverFile(fileName,file);
		
		if(file!=null){
			BreakPointData data=RecordProcessor.getInstance().getFileBreakPoint(file.toString());
			if(data!=null){
				scroller.setHistoryBreakPoints(data.getBreakPointScript());
				String fileModify=file.lastModified()+"";
				String historyModify=data.getModifyTime();
				if(historyModify.compareTo(fileModify)<0){
					String msg = MinervaMessage.WARNING_TITLE+file.toString()+" : Breakpoints may be overdue"+"\n";
					MessagePane.getInstance().appendErrMessage(msg);
				}
			}
		}	
		this.add(scroller);
		if(file!=null)
			this.setToolTipTextAt(this.getComponentCount()-1,file.toString());
		this.setSelectedComponent(currentScroller);
	}
	
	public Scroller getCurrentScroller(){
		return currentScroller;
	}
	
	/**changeListener 也就是使用者改變目前所看的視窗時，會發生的事件*/
	public void stateChanged(ChangeEvent event){
		currentScroller=(Scroller)(this.getSelectedComponent());
	}
	
	/** 彈跳小視窗，當使用者在tabpane上按右鍵時，有小視窗彈跳出來 */
	private class Popup extends JPopupMenu{
		JMenuItem saveItem;
		JMenuItem saveAsItem;
		JMenuItem closeItem;
		public Popup(){
			saveItem=new JMenuItem("Save File");
			saveAsItem=new JMenuItem("Save As..");
			closeItem=new JMenuItem("close File");
			saveItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					FileCommand.saveFile();
				}
			});
			saveAsItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					FileCommand.saveAsFile();
				}
			});
			closeItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					FileCommand.closeFile();
				}
			});
			this.add(saveItem);
			this.add(saveAsItem);
			this.add(closeItem);
		}
	}
	
	/** 滑鼠右鍵按下去時，呼叫彈跳出來 */
	private class MouseHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent event){
			if(event.getButton()==MouseEvent.BUTTON3){
				popup.show(TabPane.this,event.getX(),25);
			}
		}
	}
	
	/**當程式 run step next cont，改變目前的程式停止點的時後，data的observer會呼叫這個method，
	 * 將程式設到正確的檔案的正確的行數。，設定時掃過所有的scroller，看那個scroller的檔案和目前程式
	 * 停止點的檔案一樣(按照project path來判斷是避免有2個檔案具同樣的名字時...)。*/
	public void setToBreakPointPos(String currentClass,int currentLine,String projectPath){
		if(projectPath.endsWith("\\")){
			projectPath = projectPath.substring(0, projectPath.lastIndexOf('\\'));
		}
		String currentClassName=currentClass.replace('.','\\');
		if(currentClassName.contains("$"))
			currentClassName=currentClassName.substring(0,currentClassName.lastIndexOf("$"));
		for(int i=0;i<this.getComponentCount();i++){
			Scroller scroller=(Scroller)this.getComponentAt(i);
			scroller.getLineArea().clearNowPoint();
			
			if(scroller.getCoverFile()!=null){
				String matchName=scroller.getCoverFile().toString();
				String matchPath = matchName.substring(0, matchName.lastIndexOf('\\'));
				matchName=matchName.substring(matchName.lastIndexOf('\\')+1,matchName.lastIndexOf('.'));				
				if(matchName.equals(currentClassName)&&matchPath.equals(projectPath)){
					currentScroller=scroller;
					currentScroller.setNowPoint(currentLine);
					this.setSelectedComponent(currentScroller);
					break;
				}
			}
		}
	}
	
	/**對所有的scroller，清掉目前的程式停止點，當stop debugger，或者debugger終結時發生。*/
	public void clearAllPoint(){
		for(int i=0;i<this.getComponentCount();i++){
			Scroller scroller=(Scroller)this.getComponentAt(i);
			scroller.getLineArea().clearNowPoint();
		}
	}
	
	/**設定value map，按目前程式停止在那個檔案來判斷。判斷原理和上面的setToBreakPointe一樣*/
	public void setValueMap(String currentClass,HashMap valueMap,String projectPath){
		if(valueMap==null)
			return;
		String currentClassName=currentClass.replace('.','\\');
		if(currentClassName.contains("$"))
			currentClassName=currentClassName.substring(0,currentClassName.lastIndexOf("$"));
		
		for(int i=0;i<this.getComponentCount();i++){
			Scroller scroller=(Scroller)this.getComponentAt(i);
			if(scroller.getCoverFile()!=null){
				String matchName=scroller.getCoverFile().toString();
				matchName=matchName.substring(0,matchName.lastIndexOf("."));
				if(matchName.endsWith(currentClassName)&&matchName.startsWith(projectPath)){
					scroller.getCodePane().setValueMap(valueMap);
					break;
				}
			}
		}
	}
}
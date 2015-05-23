package userInterface.runDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import commandCenter.CommandCenter;
import debugger.gdbCommand.CppFileProcessor;
import recordProcessor.BreakPointScript;
import recordProcessor.RecordProcessor;
import userInterface.Scroller;
import userInterface.TabPane;

/**執行cpp 和 c file用的視窗，會有lastVisitedDic代表使用者最後開啟的資料夾，2個主要要讓使用
 * 者輸入的是exe path和argument。*/
public class RunCppPanel extends RunPanel implements ActionListener{
	public static final int EXE=0;
	public static final int ARGUMENT=1;
	
	private JTextField commandField[];
	private JButton browseButton;
	private File lastVisitedDic;
	private RecordProcessor processor;
	private String mainPath;
	
	public RunCppPanel(){
		this.setName("Run Cpp File");
		processor=RecordProcessor.getInstance();
		
		commandField=new JTextField[2];
		commandField[EXE]=new JTextField(70);
		commandField[ARGUMENT]=new JTextField(70);
		browseButton=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg")); 
		browseButton.addActionListener(this);
		this.setUILayout();
	}
	private void setUILayout(){
		this.setLayout(new SpringLayout());
		SpringLayout.Constraints constraint;
		
		for(int i=0;i<commandField.length;i++){
			constraint=new SpringLayout.Constraints(Spring.constant(40),
					Spring.constant(i*80+50),Spring.constant(260),Spring.constant(25)); 
			this.add(commandField[i],constraint);
		}
		
		constraint=new SpringLayout.Constraints(Spring.constant(310),
				Spring.constant(50),Spring.constant(40),Spring.constant(23)); 
		this.add(browseButton,constraint);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 60);
		g.drawRect(20, 110, 350, 60);
		g.setColor(Color.BLACK);
		g.drawString("EXE PATH",40,35);
		g.drawString("ARGUMENT", 40, 115);
	}
	public void cancel(){}
	/**使用者按下確定後，將exepath和argument和gdbpath和project path(exe path的parent)和
	 * 要不要compile(c的情況是false，我不幫忙compile，java則會)傳進去。然後，將紀錄寫到history
	 * 去，讓下次要再執行同樣的檔案時，minerva可以自動指定上次的紀錄。*/
	public void confirm(){
		String exePath=commandField[EXE].getText();
		String argument=commandField[ARGUMENT].getText();
		String gdbPath=(String)processor.getUserSettingMap().get("GDB");
		HashMap historyMap=processor.getDebugHistoryMap();
		
		if(gdbPath==null || gdbPath.equals("default")){
			gdbPath="";
		}
		
		if(mainPath!=null && exePath!=null && !mainPath.equals("") && !exePath.equals("")){
			historyMap.put(mainPath,exePath);
			RecordProcessor.getInstance().updateDebugHistory();
			CommandCenter.initDebugger((new File(exePath)).getParentFile(),new File(exePath),argument,gdbPath,false);
			this.collectBreakpoint(new File(exePath).getParentFile());
			CommandCenter.run();
		}
	}
	/**這是為了方便用者按下這個時，可以自動指定資料夾，但不保證一定正確，指定方式是以現在
	 * scroller的視窗為基準，抓取其資料夾位置*/
	public void easyRun(){
		Scroller currentScroller=TabPane.getInstance().getCurrentScroller();
		if(currentScroller!=null && currentScroller.getCoverFile()!=null){
			File parentFile=currentScroller.getCoverFile().getParentFile();
			File siblingFile[]=parentFile.listFiles();
			for(int i=0;i<siblingFile.length;i++){
				if(siblingFile[i].toString().endsWith(".exe")){
					commandField[EXE].setText(siblingFile[i].toString());
					break;
				}
			}
		}
	}
	
	/**收集中斷點資訊，僅收集和自已相同project path的檔案*/
	private void collectBreakpoint(File projectPath){
		TabPane tabPane=TabPane.getInstance();
		for(int i=0;i<tabPane.getComponentCount();i++){
			Scroller scroller=(Scroller)tabPane.getComponentAt(i);
			String otherFileName="";
			if((scroller.getCoverFile())!=null)
				otherFileName=scroller.getCoverFile().toString();
			else
				continue;
			
			if(otherFileName.startsWith(projectPath.toString())){
				otherFileName=CppFileProcessor.getPureName(otherFileName);
				/*取得lineArea的breakpoint資料*/
				HashMap<Integer,BreakPointScript> point=scroller.getLineArea().getBreakPoint();
				//BreakPointScript point = scroller.getLineArea().getBreakPoint().get(it);
				CommandCenter.setBreakPoint(otherFileName+":"+CppFileProcessor.getPureName(commandField[EXE].getText()), point);
			}
		}
	}
	/**讀取使用者的舊記錄，並寫到視窗上在runDialog的getInstance()呼叫。*/
	public void setHistoryValue(){
		HashMap historyMap=processor.getDebugHistoryMap();
		
		Scroller currentScroller=TabPane.getInstance().getCurrentScroller();
		if(currentScroller!=null && currentScroller.getCoverFile()!=null){
			mainPath=currentScroller.getCoverFile().toString();
			String exePath=(String)historyMap.get(mainPath);
			if(exePath!=null)
				commandField[EXE].setText(exePath);
		}
	}
	
	/**按下browse button呼叫的。*/
	public void actionPerformed(ActionEvent event){
		JFileChooser jfc=new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(lastVisitedDic!=null)
			jfc.setCurrentDirectory(lastVisitedDic);
		else{
			lastVisitedDic=TabPane.getInstance().getCurrentScroller().getCoverFile().getParentFile();
			jfc.setCurrentDirectory(lastVisitedDic);
		}
		int result=jfc.showOpenDialog(this);
		if(result==JFileChooser.CANCEL_OPTION)
			return;
		File exeFile=jfc.getSelectedFile();
		if(exeFile==null || exeFile.getName().equals(""))
			return;
		lastVisitedDic=exeFile.getParentFile();
		commandField[EXE].setText(exeFile.toString());
	}
}

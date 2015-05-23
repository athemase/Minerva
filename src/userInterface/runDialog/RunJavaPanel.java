package userInterface.runDialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import network.MinervaServer;

import data.DebugData;
import debugger.jdbCommand.JavaFileProcessor;
import commandCenter.CommandCenter;
import recordProcessor.BreakPointScript;
import recordProcessor.RecordProcessor;
import uiCommand.FileCommand;
import userInterface.TabPane;
import userInterface.Scroller;

/**註解和RunCppPanel類似，參考之。*/
public class RunJavaPanel extends RunPanel implements ActionListener,ItemListener{
	public static final int PROJECT=0;
	public static final int MAIN=1;
	public static final int ARGUMENT=2;
	
	private JTextField commandField[];
	private JButton browseButton[];
	private RecordProcessor processor;
	private File lastVisitedDic;
	private JRadioButton compileSelect[];
	private ButtonGroup compileGroup;
	private boolean doCompile=false;
	
	public RunJavaPanel(){
		this.setName("Run Java File");
		processor=RecordProcessor.getInstance();
		
		commandField=new JTextField[3];
		browseButton=new JButton[2];
		
		commandField[PROJECT]=new JTextField(70);
		commandField[MAIN]=new JTextField(70);
		commandField[ARGUMENT]=new JTextField(70);
		
		browseButton[PROJECT]=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg"));
		browseButton[MAIN]=new JButton(new ImageIcon("Icon\\JavaStyle\\browse.jpg"));
		browseButton[PROJECT].addActionListener(this);
		browseButton[MAIN].addActionListener(this);
		
		compileSelect=new JRadioButton[2];
		compileSelect[0]=new JRadioButton("Run with Compile");
		compileSelect[1]=new JRadioButton("Run without Compile");
		compileGroup=new ButtonGroup();
		compileGroup.add(compileSelect[0]);
		compileGroup.add(compileSelect[1]);
		
		compileSelect[0].addItemListener(this);
		compileSelect[1].addItemListener(this);
		compileSelect[1].setSelected(true);
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
		for(int i=0;i<browseButton.length;i++){
			constraint=new SpringLayout.Constraints(Spring.constant(310),
					Spring.constant(i*80+50),Spring.constant(40),Spring.constant(23)); 
			this.add(browseButton[i],constraint);
		}
		constraint=new SpringLayout.Constraints(Spring.constant(40),
				Spring.constant(280),Spring.constant(130),Spring.constant(30)); 
		this.add(compileSelect[0],constraint);
		constraint=new SpringLayout.Constraints(Spring.constant(190),
				Spring.constant(280),Spring.constant(150),Spring.constant(30)); 
		this.add(compileSelect[1],constraint);
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(20, 30, 350, 60);
		g.drawRect(20, 110, 350, 60);
		g.drawRect(20,190,350,60);
		g.drawRect(20,270,350,60);
		g.setColor(Color.BLACK);
		g.drawString("PROJECT PATH",40,35);
		g.drawString("MAIN FILE", 40, 115);
		g.drawString("ARGUMENTS", 40, 195);
		g.drawString("COMPILE SELECT",40,275);
	}
	
	public void confirm(){
		String projectPath=commandField[PROJECT].getText();
		String mainPath=commandField[MAIN].getText();
		String argument=commandField[ARGUMENT].getText();
		String jdbPath=(String)processor.getUserSettingMap().get("JDB");
		HashMap historyMap=processor.getDebugHistoryMap();
		
		if(jdbPath==null || jdbPath.equals("default")){
			jdbPath="";
		}
		
		if(doCompile){
			FileCommand.saveFile();
		}
		if(!projectPath.equals("")&& !mainPath.equals("")){
			historyMap.put(mainPath,projectPath);
			RecordProcessor.getInstance().updateDebugHistory();
			if(projectPath.endsWith("\\"))
				projectPath=projectPath.substring(0,projectPath.length()-1);
			if(CommandCenter.initDebugger(new File(projectPath),new File(mainPath),argument,jdbPath,doCompile)){
				this.collectBreakpoint(new File(projectPath),new File(mainPath));
				CommandCenter.run();
			}
		}	
	}
	public void cancel(){}
	public void easyRun(){
		Scroller currentScroller=TabPane.getInstance().getCurrentScroller();
		if(currentScroller!=null && currentScroller.getCoverFile()!=null){
			String projectPath=currentScroller.getCoverFile().getParent();
			String mainPath=currentScroller.getCoverFile().toString();
			commandField[PROJECT].setText(projectPath);
			commandField[MAIN].setText(mainPath);
		}
	}
	private void collectBreakpoint(File projectPath,File mainPath){
		/**將breakpoint設定到gdb or jdb */
		TabPane tabPane=TabPane.getInstance();
		for(int i=0;i<tabPane.getComponentCount();i++){
			Scroller scroller=(Scroller)tabPane.getComponentAt(i);
			File otherFilePath=null;
			String otherFileName="";
			if((otherFilePath=scroller.getCoverFile())!=null)
				otherFileName=scroller.getCoverFile().toString();
			else
				continue;
			
			if(otherFileName.startsWith(projectPath.toString())){
				otherFileName=JavaFileProcessor.getPureName(projectPath,otherFileName);
				HashMap<Integer,BreakPointScript> point=scroller.getLineArea().getBreakPoint();
				Iterator innerFiles=JavaFileProcessor.getInnerFile(projectPath, otherFilePath,otherFileName).iterator();
				//HashMap<Integer,BreakPointScript> it=scroller.getLineArea().getBreakPoint();
				CommandCenter.setBreakPoint(otherFileName,point);
				
				while(innerFiles.hasNext()){
					String innerName=(String)innerFiles.next();
					point=scroller.getLineArea().getBreakPoint();
					//it=scroller.getLineArea().getBreakPoint();
					CommandCenter.setBreakPoint(innerName,point);
				}
			}
		}
	}
	
	public void setHistoryValue(){
		HashMap historyMap=processor.getDebugHistoryMap();
		historyMap=RecordProcessor.getInstance().getDebugHistoryMap();
		
		Scroller currentScroller=TabPane.getInstance().getCurrentScroller();
		if(currentScroller!=null && currentScroller.getCoverFile()!=null){
			String mainPath=currentScroller.getCoverFile().toString();
			String projectPath=(String)historyMap.get(mainPath);
			if(projectPath!=null){
				commandField[PROJECT].setText(projectPath);
				commandField[MAIN].setText(mainPath);
			}
		}
	}
	public void actionPerformed(ActionEvent event){
		JFileChooser jfc=new JFileChooser();
		if(lastVisitedDic==null){
			lastVisitedDic=TabPane.getInstance().getCurrentScroller().getCoverFile().getParentFile();
		}
		jfc.setCurrentDirectory(lastVisitedDic);
		if(event.getSource()==browseButton[0]){
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else if(event.getSource()==browseButton[1]){
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		
		int result=jfc.showOpenDialog(this);
		if(result==JFileChooser.CANCEL_OPTION)
			return;
		if(event.getSource()==browseButton[0]){
			File projectFile=jfc.getSelectedFile();
			if(projectFile==null || projectFile.getName().equals(""))
				return;
			lastVisitedDic=projectFile;
			commandField[PROJECT].setText(projectFile.toString());
		}
		else if(event.getSource()==browseButton[1]){
			File mainFile=jfc.getSelectedFile();
			if(mainFile==null || mainFile.getName().equals(""))
				return;
			commandField[MAIN].setText(mainFile.toString());
		}
	}
	public void itemStateChanged(ItemEvent event){
		if(event.getSource()==compileSelect[0]){
			if(event.getStateChange()==ItemEvent.SELECTED){
				doCompile=true;
			}
		}
		if(event.getSource()==compileSelect[1]){
			if(event.getStateChange()==ItemEvent.SELECTED){
				doCompile=false;
			}
		}
	}
}

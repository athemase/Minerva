package debugger.jdbCommand;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import recordProcessor.BreakPointScript;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.DebugProcess;
import commandCenter.MinervaMessage;
import commandCenter.DebugCommand;
import commands.Command;

import data.DebugData;
import network.MinervaClient;
import network.MinervaServer;


public class JavaBridge extends DebugBridge{
	
	private static JavaBridge javaBridge;	
	private boolean isAnimate=false;	
	private Command currentCommand;
	private DebugInfo info;
	private HashMap<String,HashMap<Integer,BreakPointScript>> breakpointData;
	private HashMap<Integer,BreakPointScript> scriptMap;
	private String currentScript;
	
	protected JavaBridge(){		
		mode = DO_NOTHING;
		info = new DebugInfo();
		currentScript = "";
		breakpointData = new HashMap<String,HashMap<Integer,BreakPointScript>>();
		scriptMap = new HashMap<Integer,BreakPointScript>();
	}
	public static JavaBridge getInstance(){
		if(javaBridge==null)
			javaBridge=new JavaBridge();
		return javaBridge;
	}
	
	public boolean initDebugger(File projectPath, File mainPath,String args,String debugPath,boolean isCompile){
		String command[];
		DebugData.getInstance().setRunning(true);
		
		if(isCompile){
			command=JavaMsgFormat.formCompileCommand(debugPath,projectPath.toString(), mainPath.toString());
			this.setMode(COMPILE);
			DebugProcess javac=new DebugProcess(command);
			if(!javac.isRunning()){
				System.err.println(MinervaMessage.COMMAND_NOT_FOUND);
				DebugData.getInstance().setRunning(false);
				return false;
			}
			while(this.getMode()==COMPILE){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
		else{
			info.setCompileStatus(true);			
		}
		if(!mainPath.toString().startsWith(projectPath.toString())){
			System.err.println(MinervaMessage.ILLEGAL_PATH);
			return false;
		}
		if(this.info.getCompileStatus()==true){
			String mainFileName=JavaFileProcessor.getPureName(projectPath, mainPath);
			DebugData.getInstance().setCurProject(projectPath.toString());
			command=JavaMsgFormat.formDebugCommand(debugPath,projectPath.toString(), mainFileName, args);
			this.info.setDebugger(new DebugProcess(command));
			if(!info.getDebugger().isRunning()){
				System.err.println(MinervaMessage.COMMAND_NOT_FOUND);
				DebugData.getInstance().setRunning(false);
				info.setDebugger(null);
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void setBreakPoint(String fileName,HashMap<Integer,BreakPointScript> breakpoint) throws IOException{
		this.breakpointData.put(fileName, breakpoint);
		Iterator it = breakpoint.keySet().iterator();
		while(it.hasNext()){
			int lineNumber = (Integer)it.next();
			String command="stop at "+fileName+":"+lineNumber;
			//this.info.setLineNumber(lineNumber);
			this.info.getDebugger().writeln(command);
		}
	}
	public void run() throws IOException{
		this.setMode(RUN);
		this.info.getDebugger().writeln("run");
	}
	public void stop() throws IOException{
		this.setMode(STOP);
		this.info.getDebugger().writeln("exit");
		this.info.getDebugger().getProcess().destroy();
		Iterator it = breakpointData.keySet().iterator();
		while(it.hasNext()){
			scriptMap = breakpointData.get(it.next().toString());
			Iterator itr = scriptMap.keySet().iterator();
			while(itr.hasNext()){
				//System.out.println(itr.next().toString());
				BreakPointScript bps = scriptMap.get(Integer.parseInt(itr.next().toString()));
				bps.setLoadState(false);
			}
		}
	}
	public void step() throws IOException{
		this.setMode(STEP);
		this.info.getDebugger().writeln("step");
	}
	public void cont() throws IOException{
		this.setMode(CONT);
		this.info.getDebugger().writeln("cont");
	}
	public void next() throws IOException{
		this.setMode(NEXT);
		this.info.getDebugger().writeln("next");
	}
	public void visualize(String varName) throws IOException{
		if(varName.endsWith(":script")){
			varName = varName.replaceFirst(":script","");
			info.setScriptMode(true);
		}
		else{
			info.setScriptMode(false);
		}
		if(JavaMsgFormat.isVarNameCurrect(varName)){
			this.setMode(VISUAL);
			info.setVarName(varName);
			this.info.getDebugger().writeln("print "+varName);
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
	}
	public void unfold(String varName) throws IOException{
		String tokens[]=varName.split(" ");
		
		if(tokens.length<1 || tokens.length%2==0){
			System.err.println(MinervaMessage.ILLEGAL_ARGS);
			return;
		}
		if(JavaMsgFormat.isVarNameCurrect(tokens[0])){
			this.setMode(UNFOLD);
			info.setVarName(varName);
			this.info.getDebugger().writeln("print "+tokens[0]);
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
	}
	public void fold(String varName) throws IOException{
		if(JavaMsgFormat.isVarNameCurrect(varName)){
			this.setMode(FOLD);
			info.setVarName(varName);
			this.info.getDebugger().writeln("print "+varName);
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
	}
	public void linenumber(String message) throws IOException{
		MinervaServer.getInstance().sendMessage(Integer.toString(info.getLineNumber()));	
	}
	public void setscript(String message) throws IOException{
		if(message.equals("SVM_DONE")){
			this.setCurrentScript("");
			if(this.info.getScriptCont()){
				this.cont();
			}
			System.out.println("minerva continue");
		}
	}
	
	public void start(){
		isSendingData=true;
	}
	public void end(){
		isSendingData=false;
	}
	public void ask(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(ASK);
		this.info.getDebugger().writeln("print "+this.info.getVarName());
	}
	public void struct(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(STRUCT);
		this.info.getDebugger().writeln("Fields "+this.info.getVarName());
	}
	public void show(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(SHOW);
		this.info.getDebugger().writeln("print "+this.info.getVarName());
	}
	public void scope(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(SCOPE);
		this.info.getDebugger().writeln("print "+this.info.getVarName());
	}
	
	public void animate(){
		if(this.info.getDebugger()!=null && this.isAnimate==false && MinervaClient.getClient()!=null){
			this.isAnimate=true;
			MinervaClient.getInstance().sendMessage("<ANIMATE> start");
		}
	}
	
	public synchronized void messageParser(String message) throws IOException{
		currentCommand = DebugCommand.getInstance().getJavaCommand(this.getMode());
		this.info.setMessage(message);
		currentCommand.setDebugInfo(info);
		if(this.getMode() == RUN || this.getMode() == STEP || this.getMode() == CONT || this.getMode() == NEXT){
			this.info.setDebuggerStatus(this.setCurrentPos(this.info.getMessage()));			
		}		
		this.setMode(currentCommand.excute());		
		this.notify();		
	}
	public boolean commandCompleted(String message){
		if(message.contains("\n") && message.endsWith("[1] "))
			return true;
		return false;
	}
	
	public boolean setCurrentPos(String message) throws IOException{
		String tokens[]=message.split("[ \\n\\r]");
		boolean isSetSuccess=false;
		int currentLine=0;
		String currentClass="";
		
		if(message.contains("Stopping due to deferred breakpoint errors.\n")){
			this.cont();
			return false;
		}
		
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].startsWith("line=")){
				try{
					/*目前中斷的行號*/
					currentLine=Integer.parseInt(tokens[i].substring("line=".length()));
					i--;

				    if(!tokens[i].matches("[0-9A-Za-z\\$_\\.]+[0-9A-Za-z\\$_\\<\\>]+\\(\\),"))
				    	i--;
					currentClass=tokens[i].substring(0,tokens[i].lastIndexOf("."));
					
					DebugData.getInstance().setCurPosition(currentClass,currentLine);
					isSetSuccess=true;
					break;
				}catch(NumberFormatException exception){
					System.err.println(MinervaMessage.SET_CURRENTPOS_ERROR);
					return false;
				}
			}
		}
		this.info.setLineNumber(currentLine);
		return isSetSuccess;
	}
	
	public DebugProcess getDebugger(){
		return this.info.getDebugger();
	}
	public void removeDebugger(){
		this.info.setDebugger(null);
	}
	public void inputCommand(String command) throws IOException{
		this.info.getDebugger().writeln(command);
	}
	public BreakPointScript getBreakPointScript(int lineNumber,String currentRun){
		String name = currentRun.substring(currentRun.lastIndexOf("\\")+1,currentRun.lastIndexOf("."));
		HashMap<Integer,BreakPointScript> data = breakpointData.get(name);
		if(data == null){
			return null;
		}
		
		if(data.containsKey(lineNumber) == false){
			return null;
		}
		return data.get(lineNumber);
	}
	public String getCurrentScript(){
		return this.currentScript;
	}
	public void setCurrentScript(String script){
		this.currentScript = script;
	}
	public void setScriptCont(boolean isScriptCont){
		this.info.setScriptCont(isScriptCont);
	}
}

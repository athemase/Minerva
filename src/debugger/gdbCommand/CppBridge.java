package debugger.gdbCommand;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import recordProcessor.BreakPointScript;

import commandCenter.CommandCenter;
import commandCenter.DebugBridge;
import commandCenter.DebugCommand;
import commandCenter.DebugInfo;
import commandCenter.DebugProcess;
import commandCenter.MinervaMessage;
import commands.Command;
import data.DebugData;
import network.*;

public class CppBridge extends DebugBridge{

	private static CppBridge cppBridge;
	private DebugInfo info;
	private Command currentCommand;
	private HashMap<String,HashMap<Integer,BreakPointScript>> breakpointData;
	private HashMap<Integer,BreakPointScript> scriptMap;
	private String currentScript;
	protected CppBridge(){		
		mode = DO_NOTHING;
		info = new DebugInfo();
		currentScript = "";
		breakpointData = new HashMap<String,HashMap<Integer,BreakPointScript>>();
		scriptMap = new HashMap<Integer,BreakPointScript>();
	}
	public static CppBridge getInstance(){
		if(cppBridge==null)
			cppBridge=new CppBridge();
		return cppBridge;
	}
	
	public boolean initDebugger(File projectPath,File mainPath,String args,String debugPath,boolean isCompile){
		String command[];
		DebugData.getInstance().setRunning(true);
		// temporarily
		//debugPath = "C:\\cygwin\\bin";
		
		command=CppMsgFormat.formDebugCommand(debugPath,mainPath.toString(),args);
		this.info.setDebugger(new DebugProcess(command,projectPath));
		if(!this.info.getDebugger().isRunning()){
			System.err.println(MinervaMessage.COMMAND_NOT_FOUND);
			this.info.setDebugger(null);
			return false;
		}
		DebugData.getInstance().setCurProject(projectPath.toString());
		
		try {
			this.info.getDebugger().writeln("set args "+args);
		} catch (IOException e) {}
		return true;
	}
	public void setBreakPoint(String fileName,HashMap<Integer,BreakPointScript> breakpoint)throws IOException{
		String[] tokens = fileName.split(":");
		String runFileName = tokens[1].substring(tokens[1].lastIndexOf("\\")+1,tokens[1].lastIndexOf("."));
		breakpointData.put(runFileName, breakpoint);
		Iterator it = breakpoint.keySet().iterator();
		while(it.hasNext()){
			int lineNumber = (Integer)it.next();
			String command="b "+tokens[0]+":"+lineNumber;
			this.info.getDebugger().writeln(command);
		}
	}
	public void run() throws IOException{
		this.setMode(RUN);
		this.info.getDebugger().writeln("run");
	}
	public void stop() throws IOException{
		this.setMode(STOP);
		this.info.getDebugger().writeln("quit");
		this.info.getDebugger().writeln("y");
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
		if(CppMsgFormat.isVarNameCurrect(varName)){
			this.setMode(VISUAL);
			this.info.setVarName(varName);
			this.info.getDebugger().writeln("whatis "+varName);
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
		
		if(CppMsgFormat.isVarNameCurrect(tokens[0])){
			this.setMode(UNFOLD);
			this.info.setVarName(varName);
			this.info.getDebugger().writeln("whatis "+tokens[0]);
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
	}
	public void fold(String varName) throws IOException{
		if(CppMsgFormat.isVarNameCurrect(varName)){
			this.setMode(FOLD);
			this.info.setVarName(varName);
			this.info.getDebugger().writeln("whatis "+varName);
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
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
		this.info.getDebugger().writeln("whatis "+this.info.getVarName());
	}
	public void show(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(SHOW);
		this.info.getDebugger().writeln("print "+this.info.getVarName());
	}
	public void struct(String message) throws IOException{
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(STRUCT);
		this.info.getDebugger().writeln("ptype "+this.info.getVarName());
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
	public void animate(){}
	public void scope(String message) throws IOException {
		this.info.setVarName(message.split(" ")[1]);
		this.setMode(SCOPE);
		this.info.getDebugger().writeln("print "+this.info.getVarName());
	}
	
	public synchronized void messageParser(String message) throws IOException{
		if(message.endsWith("Program exited normally.\n(gdb) "))
			this.stop();
		else{
			currentCommand = DebugCommand.getInstance().getCppCommand(this.getMode());
			this.info.setMessage(message);
			currentCommand.setDebugInfo(info);
			if(this.getMode() == RUN || this.getMode() == STEP || this.getMode() == CONT || this.getMode() == NEXT){
				this.info.setDebuggerStatus(this.setCurrentPos(this.info.getMessage()));			
			}		
			this.setMode(currentCommand.excute());		
			this.notify();
		}
		
	}
	
	public boolean commandCompleted(String printResult) {
		if(printResult.endsWith("(gdb) "))
			return true;
		if(printResult.startsWith("(gdb) "))
			return true;
		return false;
	}
	
	/** bug step next can set to currect break point*/ 
	public boolean setCurrentPos(String message) throws IOException{
		int currentLine=0;
		String currentClass="";
		
		if(message.equals("The program is not being run.\r\n")){
			this.stop();
		}
		String tokens[]=message.split("[ \\n\\r\\t]");
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].equals("at")&&tokens[i-1].contains(")")){
				try{
					currentClass=tokens[i+1].substring(0,tokens[i+1].lastIndexOf("."));
					currentClass=currentClass.replace("/", "\\");
					currentLine=Integer.parseInt(tokens[i+1].substring(tokens[i+1].lastIndexOf(":")+1));
					this.info.setLineNumber(currentLine);
					DebugData.getInstance().setCurPosition(currentClass,currentLine);
					return true;
				}catch(NumberFormatException exception){
					break;
				}catch(ArrayIndexOutOfBoundsException exception){
					break;
				}
			}
		}
		try{
			currentLine=Integer.parseInt(tokens[0]);
			this.info.setLineNumber(currentLine);
			DebugData.getInstance().setCurLine(currentLine);
			return true;
		}catch(NumberFormatException exception){
			return false;
		}
	}
	public void removeDebugger(){
		this.info.setDebugger(null);
	}
	public DebugProcess getDebugger(){
		return this.info.getDebugger();
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

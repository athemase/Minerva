package commandCenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import recordProcessor.BreakPointScript;

import network.MinervaClient;
import data.DebugData;
import debugger.gdbCommand.CppBridge;
import debugger.jdbCommand.JavaBridge;

/**處理所有debugger和ui的中間介面，是一個proxy。*/
public class CommandCenter {
	public static DebugBridge debugBridge;    /**指向目前正在run的debugger*/
	public static String currentRun;
	
	/**初始化debugger，設定debugger路徑，設定傳入參數，設定project，設定main file，設定是否要在run之前先compile*/
	public static boolean initDebugger(File projectPath,File mainPath,String argment,String debuggerPath,boolean doCompile){
		boolean isCurrectRun=false;
		if(projectPath==null || mainPath==null){
			System.err.println(MinervaMessage.ILLEGAL_PATH);
			return false;
		}
		if(debugBridge!=null){
			System.err.println(MinervaMessage.DEBUGGER_EXIST);
			return false;
		}

		String mainName=mainPath.toString();
		String subFileName="";
		if(mainName.contains("."))
			subFileName=mainName.substring(mainName.lastIndexOf("."));
		
		if(subFileName.equalsIgnoreCase(".java")){
			debugBridge=JavaBridge.getInstance();
			isCurrectRun=debugBridge.initDebugger(projectPath,mainPath,argment,debuggerPath,doCompile);
		}
		else if(subFileName.equalsIgnoreCase(".exe")){
			debugBridge=CppBridge.getInstance();
			isCurrectRun=debugBridge.initDebugger(projectPath, mainPath, argment,debuggerPath,doCompile);
		}
		else
			return false;
		
		if(isCurrectRun){
			if(MinervaClient.getClient()==null){}
			else if(mainName.equals(currentRun))
				MinervaClient.getInstance().sendMessage(XdivaMessage.RESET_VM);
			else
				MinervaClient.getInstance().sendMessage(XdivaMessage.CLEAR_VM);
		}
		else
			debugBridge=null;
		
		currentRun=mainName;
		return isCurrectRun;
	}
	
	/**設定中斷點*/
	public static void setBreakPoint(String fileName,HashMap<Integer,BreakPointScript> breakpoint){
		if(debugBridge!=null){
			try {
				debugBridge.setBreakPoint(fileName,breakpoint);
			} catch (IOException exception){}
		}
	}
	public static void run(){
		if(debugBridge!=null){
			try{
				debugBridge.run();
			}catch(IOException exception){}
		}
	}
	
	public static void stop(){
		if(debugBridge!=null){
			try{
				debugBridge.stop();
			}catch(IOException exception){}
		}
	}
	public static void step(){
		if(debugBridge!=null){
			try{
				debugBridge.step();
			}catch(IOException exception){}
		}
	}
	public static void cont(){
		if(debugBridge!=null){
			try{
				debugBridge.cont();
			}catch(IOException exception){}
		}
	}
	public static void next(){
		if(debugBridge!=null){
			try{
				debugBridge.next();
			}catch(IOException exception){}
		}
	}
	public static void animate(){
		if(debugBridge!=null){
			try{
				debugBridge.animate();
			}catch(IOException exception){}
		}
	}
	public static void inputCommand(String command){
		if(debugBridge!=null){
			try {
				debugBridge.inputCommand(command);
			} catch (IOException exception){}
		}
	}
	
	public static void visualize(String message){
		if(debugBridge==null)
			System.err.println(MinervaMessage.DEBUGGER_UNEXIST);
		else{
			try {
				debugBridge.visualize(message);
			} catch (IOException exception){}
		}
	}
	
	public static void unfold(String message){
		if(debugBridge==null)
			System.err.println(MinervaMessage.DEBUGGER_UNEXIST);
		else{
			try {
				debugBridge.unfold(message);
			} catch (IOException exception){}
		}
	}
	
	public static void fold(String message){
		if(debugBridge==null)
			System.err.println(MinervaMessage.DEBUGGER_UNEXIST);
		else{
			try {
				debugBridge.fold(message);
			} catch (IOException exception){}
		}
	}

	/**EditorServer呼叫，當有訊息從diva過來時....*/
	public static void serverReceived(String message){
		try{
			if(debugBridge==null)
				System.err.println(MinervaMessage.DEBUGGER_UNEXIST);
			else if(message.startsWith(XdivaMessage.ASK))
				debugBridge.ask(message);
			else if(message.startsWith(XdivaMessage.SHOW))
				debugBridge.show(message);
			else if(message.startsWith(XdivaMessage.STRUCT))
				debugBridge.struct(message);
			else if(message.startsWith(XdivaMessage.STEP))
				debugBridge.step();
			else if(message.startsWith(XdivaMessage.CONT))
				debugBridge.cont();
			else if(message.startsWith(XdivaMessage.NEXT))
				debugBridge.next();
			else if(message.startsWith(XdivaMessage.SCOPE))
				debugBridge.scope(message);
			else if(message.startsWith(XdivaMessage.LINENUMBER)){
				debugBridge.linenumber(message);
			}
			else if(message.startsWith(XdivaMessage.SCRIPT_VM)){
				debugBridge.setscript(message);
			}
			else if(message.startsWith(XdivaMessage.END)){
				/**for script use , 當diva回覆 end時 , 檢查這個breakpoint有沒有script要執行*/	
				if(debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun) != null){
					String script = debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun).loadScript();
					boolean status = debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun).getStatus();
					if(!script.equals("")){
						debugBridge.setCurrentScript(script);
						debugBridge.setScriptCont(status);
						debugBridge.visualize(script+":script");
						System.out.println("script process");
					}					
				}
				else{
					debugBridge.end();
				}
				
			}
			else if(message.equals(XdivaMessage.START)){
				debugBridge.start();
			}
		}
		catch(IOException exception){}
	}
}

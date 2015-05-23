package commands;
import java.io.*;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
/**debugger指令*/
public class Command {
	/**目前debugger的info*/
	protected DebugInfo info;
	/**下一個command mode*/
	protected int nextMode;
	/**command main execution method*/
	protected Execution_Behavior behavior;
	public Command(){
		nextMode = DebugBridge.DO_NOTHING;
	}
	public Command(Execution_Behavior behavior){
		info = null;
		nextMode = DebugBridge.DO_NOTHING;
		this.behavior = behavior;
	}
	public void setDebugInfo(DebugInfo info){
		this.info = info;
	}
	public void setBehavior(Execution_Behavior behavior){
		this.behavior = behavior;
	}
	public int excute() throws IOException{
		return behavior.exe(info);
	}
}

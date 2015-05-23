package commands.javaBehavior;

import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Show implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(JavaMsgFormat.isVarOutOfScope(info.getMessage()))
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formOutOfScopeResult(info.getVarName()));
		else if(!info.getMessage().contains("\""))
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formShowResult(info.getMessage(),info.getVarName()));
		else{
			mode = DebugBridge.SHOW_STRING;
			info.setPrintResult(info.getMessage());
			info.getDebugger().writeln("dump "+info.getVarName());
		}
		return mode;
	}
	
}

package commands.cppBehavior;

import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Ask implements Execution_Behavior{
	
	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(!info.getMessage().contains("="))
			MinervaServer.getInstance().sendMessage(CppMsgFormat.formOutOfScopeResult(info.getVarName()));
		else{
			info.setVarType(CppMsgFormat.getTypeValue(info.getMessage(), info.getVarName()));
			mode = DebugBridge.ASK_ADDR;
			info.getDebugger().writeln("print &"+info.getVarName());
		}
		return mode;
	}
	
}

package commands.cppBehavior;

import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Show implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(!info.getMessage().contains("="))
			MinervaServer.getInstance().sendMessage(CppMsgFormat.formOutOfScopeResult(info.getVarName()));
		else{
			String value=CppMsgFormat.getShowValue(info.getMessage());
			if(value==null){
				mode = DebugBridge.SHOW_ADDR;
				info.getDebugger().writeln("print &"+info.getVarName());
			}
			else
				MinervaServer.getInstance().sendMessage(CppMsgFormat.formShowPrimitiveResult(info.getVarName(),value));
		}
		return mode;
	}

}

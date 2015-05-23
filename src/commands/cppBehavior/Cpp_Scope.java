package commands.cppBehavior;

import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Scope implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		MinervaServer.getInstance().sendMessage(CppMsgFormat.formScopeResult(CppMsgFormat.isVariableOutOfScope(info.getMessage()),info.getVarName()));
		return mode;
	}
	
}

package commands.cppBehavior;

import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Struct implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		MinervaServer.getInstance().sendMessage(CppMsgFormat.formStructResult(info.getMessage(), info.getVarName()));
		return mode;
	}

}

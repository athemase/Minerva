package commands.cppBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Fold implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(CppMsgFormat.isVariableExist(info.getMessage()))
			MinervaClient.getInstance().sendMessage("fold "+info.getVarName());
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
		return mode;
	}

}

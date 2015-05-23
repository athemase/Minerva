package commands.cppBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;

public class Cpp_Unfold implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(CppMsgFormat.isVariableExist(info.getMessage())){
			String tokens[]=info.getVarName().split(" ");
			if(tokens.length==1)
				MinervaClient.getInstance().sendMessage("unfold "+info.getVarName());
			else
				MinervaClient.getInstance().sendMessage("unfold"+tokens.length/2+"D "+info.getVarName());
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
		return mode;
	}

}

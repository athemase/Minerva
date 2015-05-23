package commands.javaBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Fold implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(JavaMsgFormat.isVariableExist(info.getMessage())!=null)
			MinervaClient.getInstance().sendMessage("fold "+info.getVarName());
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
		return mode;
	}

}

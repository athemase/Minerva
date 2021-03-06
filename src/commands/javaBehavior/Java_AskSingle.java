package commands.javaBehavior;
import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;
public class Java_AskSingle implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		MinervaServer.getInstance().sendMessage(JavaMsgFormat.formSingleRef(
				info.getPrintResult(),info.getMessage(),info.getVarName()));
		return mode;
	}

}

package commands.cppBehavior;
import java.io.IOException;

import network.MinervaServer;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.gdbCommand.CppMsgFormat;
public class Cpp_ShowAddr implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		String varAddr=CppMsgFormat.getAddrValue(info.getMessage());
		MinervaServer.getInstance().sendMessage(CppMsgFormat.formShowRefResult(info.getVarName(),varAddr));
		return mode;
	}

}

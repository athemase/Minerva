package commands.javaBehavior;

import java.io.IOException;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import data.DebugData;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Compile implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		String errorMsg=JavaMsgFormat.formCompileErrorMsg(info.getMessage());
		if(errorMsg.equals(""))
			info.setCompileStatus(true);
		else{
			System.err.println(errorMsg);
			info.setCompileStatus(false);
			DebugData.getInstance().setRunning(false);
		}
		mode = DebugBridge.RUN;
		return mode;
	}

}

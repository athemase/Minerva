package commands.javaBehavior;
import java.io.IOException;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import data.DebugData;
import debugger.jdbCommand.JavaMsgFormat;
public class Java_DumpThis implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		DebugData.getInstance().setValueMap(JavaMsgFormat.formVariableValue(info.getMessage()));
		mode = DebugBridge.LOCALS;
		info.getDebugger().writeln("locals");
		return mode;
	}

}

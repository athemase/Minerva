package commands.cppBehavior;

import java.io.IOException;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;

public class Cpp_ShowPri implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		return mode;
	}

}

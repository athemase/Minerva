package commands.cppBehavior;

import java.io.IOException;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;

public class Cpp_Stop implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		System.err.println(MinervaMessage.TERMINATE);
		return mode;
	}

}

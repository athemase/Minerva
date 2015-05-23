package commands.javaBehavior;

import java.io.IOException;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;

public class Java_Run implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(info.getSetStatus()){			
			mode = DebugBridge.DUMP_THIS;
			info.getDebugger().writeln("dump this");
		}
		return mode;
	}

}

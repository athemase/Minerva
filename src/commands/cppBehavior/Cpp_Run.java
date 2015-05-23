package commands.cppBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.CommandCenter;
import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import data.DebugData;

public class Cpp_Run implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(MinervaClient.getClient()!=null){
			MinervaClient.getInstance().sendMessage("update");
		}
		return mode;
	}
	
}

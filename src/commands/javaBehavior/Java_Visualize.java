package commands.javaBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Visualize implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		
		System.out.println("  In Java_Visualize.java");
		
		int mode = DebugBridge.DO_NOTHING;
		if((JavaMsgFormat.isVariableExist(info.getMessage()))!=null){
			if(!info.getScriptMode())
			{
				MinervaClient.getInstance().sendMessage("visualize "+info.getVarName());
			}
			else
			{
				MinervaClient.getInstance().sendMessage("visualize "+info.getVarName()+":script");
			}
		}		
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
		return mode;
	}

}

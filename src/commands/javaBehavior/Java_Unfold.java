package commands.javaBehavior;

import java.io.IOException;

import network.MinervaClient;

import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commandCenter.MinervaMessage;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Unfold implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(JavaMsgFormat.isVariableExist(info.getMessage())!=null){
			String tokens[]=info.getVarName().split(" ");
			if(tokens.length==1)
				MinervaClient.getInstance().sendMessage("unfold "+info.getVarName()+":"+info.getLineNumber());
			else
				MinervaClient.getInstance().sendMessage("unfold"+tokens.length/2+"D "+info.getVarName()+":"+info.getLineNumber());
		}
		else
			System.err.println(MinervaMessage.ILLEGAL_VARNAME);
		return mode;
	}

}

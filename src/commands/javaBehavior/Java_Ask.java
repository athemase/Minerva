package commands.javaBehavior;

import java.io.IOException;

import network.MinervaServer;
import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import debugger.jdbCommand.JavaMsgFormat;

public class Java_Ask implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		if(JavaMsgFormat.isVarOutOfScope(info.getMessage()))
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formOutOfScopeResult(info.getVarName()));
		else if(!info.getMessage().contains("\"")&&info.getMessage().contains("[]")){
			info.setPrintResult(info.getMessage());
			info.getDebugger().writeln("dump "+info.getVarName());
			mode = DebugBridge.ASK_ARRAY_DIM;
		}
		else if(!info.getMessage().contains("\"")&&info.getMessage().contains("instance of"))
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formArrayMessage(info.getMessage(),null, info.getVarName()));
		else if(info.getMessage().contains("\"")){
			info.setPrintResult(info.getMessage());
			info.getDebugger().writeln("dump "+info.getVarName());
			mode = DebugBridge.ASK_SINGLE;
		}
		else if(info.getMessage().contains("= null"))
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formNullRef(info.getVarName()));
		else if(info.getMessage().contains(" = ")){
			String replyMsg=JavaMsgFormat.formPrimitive(info.getMessage(),info.getVarName());
			if(replyMsg.contains("unknow")){
				info.getDebugger().writeln("print "+info.getVarName()+"+10");
				info.setPrintResult(info.getMessage());
				mode = DebugBridge.ASK_CHAR_OR_INT;
			}
			else
				MinervaServer.getInstance().sendMessage(replyMsg);
		}
		else
			MinervaServer.getInstance().sendMessage(JavaMsgFormat.formNullRef(info.getVarName()));
		return mode;
	}

}

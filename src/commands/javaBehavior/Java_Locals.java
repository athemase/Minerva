package commands.javaBehavior;
import java.io.IOException;

import network.MinervaClient;
import network.MinervaServer;

import commandCenter.CommandCenter;
import commandCenter.DebugBridge;
import commandCenter.DebugInfo;
import commands.Execution_Behavior;
import data.DebugData;
import debugger.jdbCommand.JavaMsgFormat;
public class Java_Locals implements Execution_Behavior{

	public int exe(DebugInfo info) throws IOException {
		int mode = DebugBridge.DO_NOTHING;
		DebugData.getInstance().setValueMap(JavaMsgFormat.formLocalValue(info.getMessage(), DebugData.getInstance().getValueMap()));
		if(MinervaClient.getClient()!=null){
			MinervaClient.getInstance().sendMessage("update");
		}
		else{
			/**若是breakpoint有script的話 , 則執行這一段程式*/
			if(CommandCenter.debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun) != null){
				String script = CommandCenter.debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun).loadScript();
				boolean status = CommandCenter.debugBridge.getBreakPointScript(DebugData.getInstance().getCurLine(),CommandCenter.currentRun).getStatus();
				if(!script.equals("")){
					CommandCenter.debugBridge.setCurrentScript(script);
					CommandCenter.debugBridge.setScriptCont(status);
					CommandCenter.debugBridge.visualize(script+":script");
					System.out.println("script process");
				}					
			}
			mode = DebugBridge.VISUAL;
		}
		return mode;
	}

}

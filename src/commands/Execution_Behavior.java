package commands;

import java.io.IOException;

import commandCenter.DebugInfo;

/**Strategy pattern , 依照每種behavior內所覆寫的exe內容去執行相對應的command*/
public interface Execution_Behavior {
	public int exe(DebugInfo info) throws IOException;
}

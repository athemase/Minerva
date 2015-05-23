package commandCenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import recordProcessor.BreakPointScript;

/**所有debugger 和 minerva的中間橋梁，也是proxy。*/
public abstract class DebugBridge {
	protected int mode;                /**目前正在處理怎樣的命令*/
	protected boolean isSendingData;   /**如果正在和diva通訊中，則true*/
	
	/**Generic mode*/
	public static final int DO_NOTHING=-1;
	public static final int NULL=0;
	public static final int RUN=1;
	public static final int STOP=2;
	public static final int CONT=3;
	public static final int STEP=4;
	public static final int NEXT=5;
	public static final int VISUAL=6;
	public static final int UNFOLD=7;
	public static final int FOLD=8;
	public static final int ASK=9;
	public static final int STRUCT=10;
	public static final int SHOW=11;
	public static final int ANIMATE=12;
	public static final int SCOPE=13;
	
	/**JDB mode*/	
	public static final int ASK_SINGLE=100;
	public static final int LOCALS=101;
	public static final int DUMP_THIS=102;
	public static final int ASK_CHAR_OR_INT=103;
	public static final int ASK_ARRAY_DIM=104;
	public static final int COMPILE=105;
	public static final int SHOW_STRING=106;
	
	/**GDB mode*/
	public static final int SHOW_PRI=200;
	public static final int ASK_ADDR=201;
	public static final int SHOW_ADDR=202;
	
	/**debugger初始化*/
	public abstract boolean initDebugger(File projectPath, File mainPath, String argument,String debugPath,boolean isCompile);
	/**設定debug的breakpoint , 向jdb or gdb 下 stop at:lineNumber的指令*/
	public abstract void setBreakPoint(String fileName,HashMap<Integer,BreakPointScript> breakpoint) throws IOException;
	
	/**debug指令*/
	public abstract void run() throws IOException;
	public abstract void stop() throws IOException;
	public abstract void step() throws IOException;
	public abstract void cont() throws IOException;
	public abstract void next() throws IOException;
	public abstract void visualize(String varName) throws IOException;
	public abstract void unfold(String varName) throws IOException;
	public abstract void fold(String varName) throws IOException;
	public abstract void ask(String message) throws IOException;
	public abstract void show(String message) throws IOException;
	public abstract void struct(String message) throws IOException;
	public abstract void linenumber(String message) throws IOException;
	public abstract void setscript(String message) throws IOException;
	public abstract void animate() throws IOException;
	public abstract void start();
	public abstract void end();
	
	/**檢查目前的debugger所回傳的訊息 , 判斷目前debugger是否setup完畢*/
	public abstract boolean setCurrentPos(String message) throws IOException;
	/**移除debugger*/
	public abstract void removeDebugger();
	/**取得目前的debugger*/
	public abstract DebugProcess getDebugger();
	/**取得行號lineNumber的Script*/
	public abstract BreakPointScript getBreakPointScript(int lineNumber,String currentRun);
	/**取得目前DebugBridge所執行過的Script*/
	public abstract String getCurrentScript();
	/**設定目前DebugBridge的Script*/
	public abstract void setCurrentScript(String script);
	/**給予debugger所回傳的message , 提供DebugBridge目前command的mode使用 , 此method只由MessageCollector所呼叫*/
	public abstract void messageParser(String message) throws IOException;
	/**由message判斷command是否執行完畢*/
	public abstract boolean commandCompleted(String message);
	/**取得目前DebugBridge的command mode*/
	public int getMode(){
		return mode;
	}
	/**設定目前DebugBridge的command mode*/
	public void setMode(int modeNum){
		if(modeNum != DO_NOTHING)
			mode = modeNum;
	}
	/**DebugBridge是否已傳送Data , 目前沒有class呼叫*/
	public boolean isSendingData() {
		return isSendingData;
	}
	/**目前沒有class呼叫 所以我也不知道這個method做甚麼用*/
	public abstract void inputCommand(String command) throws IOException;
	/**處理DIVA要minerva對哪一個變數做scope*/
	public abstract void scope(String message) throws IOException;
	public abstract void setScriptCont(boolean isScriptCont);
}

package commandCenter;

import java.util.HashMap;

import commands.Command;
import commands.cppBehavior.*;
import commands.javaBehavior.*;
/**Command pattern , 將debugger commands統一處理*/
public class DebugCommand {
	/**jdb commands 的collector*/
	private HashMap<Integer,Command> jcommandCollect;
	/**gdb commands 的collector*/
	private HashMap<Integer,Command> ccommandCollect;
	private static DebugCommand singleton = null;
	private DebugCommand(){
		jcommandCollect = new HashMap<Integer, Command>();
		ccommandCollect = new HashMap<Integer, Command>();		
		jinit();
		cinit();
	}
	/**將jdb commands初始化*/
	private void jinit(){
		jcommandCollect.put(DebugBridge.ASK, new Command(new Java_Ask()));
		jcommandCollect.put(DebugBridge.FOLD, new Command(new Java_Fold()));
		jcommandCollect.put(DebugBridge.RUN, new Command(new Java_Run()));
		jcommandCollect.put(DebugBridge.SCOPE, new Command(new Java_Scope()));
		jcommandCollect.put(DebugBridge.SHOW, new Command(new Java_Show()));
		jcommandCollect.put(DebugBridge.STEP, new Command(new Java_Step()));
		jcommandCollect.put(DebugBridge.CONT, new Command(new Java_Step()));
		jcommandCollect.put(DebugBridge.NEXT, new Command(new Java_Step()));
		jcommandCollect.put(DebugBridge.STOP, new Command(new Java_Stop()));
		jcommandCollect.put(DebugBridge.STRUCT, new Command(new Java_Struct()));
		jcommandCollect.put(DebugBridge.UNFOLD, new Command(new Java_Unfold()));
		jcommandCollect.put(DebugBridge.VISUAL, new Command(new Java_Visualize()));
		jcommandCollect.put(DebugBridge.ASK_ARRAY_DIM,  new Command(new Java_AskArrayDim()));
		jcommandCollect.put(DebugBridge.ASK_CHAR_OR_INT, new Command(new Java_AskCharOrInt()));
		jcommandCollect.put(DebugBridge.ASK_SINGLE, new Command(new Java_AskSingle()));
		jcommandCollect.put(DebugBridge.COMPILE, new Command(new Java_Compile()));
		jcommandCollect.put(DebugBridge.DUMP_THIS, new Command(new Java_DumpThis()));
		jcommandCollect.put(DebugBridge.LOCALS, new Command(new Java_Locals()));
		jcommandCollect.put(DebugBridge.SHOW_STRING, new Command(new Java_ShowString()));
	}
	/**將gdb commands初始化*/
	private void cinit(){
		ccommandCollect.put(DebugBridge.ASK,  new Command(new Cpp_Ask()));
		ccommandCollect.put(DebugBridge.FOLD,  new Command(new Cpp_Fold()));
		ccommandCollect.put(DebugBridge.RUN,  new Command(new Cpp_Run()));
		ccommandCollect.put(DebugBridge.SCOPE,  new Command(new Cpp_Scope()));
		ccommandCollect.put(DebugBridge.SHOW,  new Command(new Cpp_Show()));
		ccommandCollect.put(DebugBridge.STEP,  new Command(new Cpp_Run()));
		ccommandCollect.put(DebugBridge.CONT,  new Command(new Cpp_Run()));
		ccommandCollect.put(DebugBridge.NEXT,  new Command(new Cpp_Run()));
		ccommandCollect.put(DebugBridge.STOP,  new Command(new Cpp_Stop()));
		ccommandCollect.put(DebugBridge.STRUCT,  new Command(new Cpp_Struct()));
		ccommandCollect.put(DebugBridge.UNFOLD,  new Command(new Cpp_Unfold()));
		ccommandCollect.put(DebugBridge.VISUAL,  new Command(new Cpp_Visualize()));
		ccommandCollect.put(DebugBridge.SHOW_PRI, new Command(new Cpp_ShowPri()));
		ccommandCollect.put(DebugBridge.ASK_ADDR, new Command(new Cpp_AskAddr()));
		ccommandCollect.put(DebugBridge.SHOW_ADDR, new Command(new Cpp_ShowAddr()));		
	}
	public static DebugCommand getInstance(){
		if(singleton == null){
			singleton = new DebugCommand();
		}
		return singleton;
	}
	/**取得相對應debugmode的指令*/
	public Command getJavaCommand(int debugmode){
		return jcommandCollect.get(debugmode);
	}
	/**取得相對應debugmode的指令*/
	public Command getCppCommand(int debugmode){
		return ccommandCollect.get(debugmode);
	}
}

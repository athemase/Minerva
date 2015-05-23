package commandCenter;
/**處理目前debugger的 information*/
public class DebugInfo {
	/**debugger回傳給minerva的訊息*/
	protected String message;
	/**變數名稱*/
	protected String varName;
	/**變數型態*/
	protected String varType;
	/**當diva下ask之類的指令時 , debugger所回傳的訊息*/
	protected String printResult;
	/**目前Minerva的debugger類型*/
	protected DebugProcess debugger;
	/**debugger是否setup success*/
	protected boolean isSetSuccess;
	/**是否compile success*/
	protected boolean isCompileSuccess;
	/**目前是否有Script寫入*/
	protected boolean isScriptMode;
	/**中斷點是否要執行continue, for script*/
	protected boolean isScriptCont;
	/**目前行號*/
	protected int lineNumber;
	/**DebugInfo建構子 , 將debug預設為null , scriptmode預設為false*/
	public DebugInfo(){
		this.debugger = null;
		this.isScriptMode = false;
	}
	/**設定debugger回傳給minerva的訊息*/
	public void setMessage(String message){
		this.message = message;
	}
	/**設定變數名稱*/
	public void setVarName(String varName){
		this.varName = varName;
	}
	/**設定目前debugger*/
	public void setDebugger(DebugProcess debugger){
		this.debugger = debugger;
	}
	/**設定目前的printresult*/
	public void setPrintResult(String printResult){
		this.printResult = printResult;
	}
	/**設定目前變數型態*/
	public void setVarType(String varType){
		this.varType = varType;
	}
	/**設定debugger的setup status*/
	public void setDebuggerStatus(boolean isSetSuccess){
		this.isSetSuccess = isSetSuccess;
	}
	/**設定compile status*/
	public void setCompileStatus(boolean isCompileSuccess){
		this.isCompileSuccess = isCompileSuccess;
	}
	/**設定目前script status*/
	public void setScriptMode(boolean isScriptMode){
		this.isScriptMode = isScriptMode;
	}
	/**設定目前行號*/
	public void setLineNumber(int lineNumber){
		this.lineNumber = lineNumber;
	}
	public void setScriptCont(boolean isScriptCont){
		this.isScriptCont = isScriptCont;
	}
	/**取得message*/
	public String getMessage(){
		return this.message;
	}
	/**取得變數名稱*/
	public String getVarName(){
		return this.varName;
	}
	/**取得變數型態*/
	public String getVarType(){
		return this.varType;
	}
	/**取得printresult*/
	public String getPrintResult(){
		return this.printResult;
	}
	/**取得目前debugger*/
	public DebugProcess getDebugger(){
		return this.debugger;
	}
	/**取得setup status*/
	public boolean getSetStatus(){
		return this.isSetSuccess;
	}
	/**取得compile status*/
	public boolean getCompileStatus(){
		return this.isCompileSuccess;
	}
	/**取得目前script status*/
	public boolean getScriptMode(){
		return this.isScriptMode;
	}
	public int getLineNumber(){
		return this.lineNumber;
	}
	public boolean getScriptCont(){
		return this.isScriptCont;
	}
}

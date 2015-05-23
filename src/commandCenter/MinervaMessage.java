package commandCenter;

/**一些minerva的輸出訊息*/
public interface MinervaMessage {
	public static final String ERROR_TITLE="[ Error ]:";
	public static final String WARNING_TITLE="[Warning]:";
	public static final String SUCCESS_TITLE="[Success]:";
	
	public static final String CANNOT_RUN=ERROR_TITLE+"Can't compile or " +
			"run this file, file must be c or cpp or java file";
	public static final String DEBUGGER_EXIST=ERROR_TITLE+"Debugger is running, " +
			"close current debugger before run a new one";
	public static final String DEBUGGER_UNEXIST=ERROR_TITLE+"Debugger haven't " +
			"start yet, please run debugger first";
	public static final String ILLEGAL_ARGS=ERROR_TITLE+"Number of args is illegal";
	public static final String ILLEGAL_VARNAME=ERROR_TITLE+"Variable is not Exist";
	public static final String ILLEGAL_PATH=ERROR_TITLE+"The file path or project" +
			" path is not currect";
	public static final String TERMINATE=SUCCESS_TITLE+"Program Terminate";
	public static final String UNFOLD_INDEX_ERROR =ERROR_TITLE+"Unfold Index Error";
	public static final String SET_CURRENTPOS_ERROR = ERROR_TITLE+"Cannot set to current position";
	public static final String COMMAND_NOT_FOUND=ERROR_TITLE+"JDB or GDB debugger path not found";
	
	public static final String CONNECTE_DIVA_SUCCESS=SUCCESS_TITLE+"Connect to diva Server Success";
	public static final String GET_DIVA_CONNECT=SUCCESS_TITLE+"Get connection from diva";
	public static final String DIVA_NOT_FOUND=ERROR_TITLE+"Diva server not found";
}

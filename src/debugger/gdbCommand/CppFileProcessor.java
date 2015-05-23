package debugger.gdbCommand;

public class CppFileProcessor {
	public static String getPureName(String fileName){
		fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
		return fileName;
	}
}

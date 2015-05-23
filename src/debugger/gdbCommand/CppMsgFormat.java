package debugger.gdbCommand;

import commandCenter.XdivaTag;

public class CppMsgFormat implements XdivaTag{
	public static boolean isVariableExist(String printResult) {
		if(printResult.contains(" = "))
			return true;
		return false;
	}

	public static boolean isVarNameCurrect(String varName) {
		if(varName.matches("[0-9A-Za-z\\.\\*\\[\\]_$->]+"))
			return true;
		return false;
	}
	
	public static String[] formDebugCommand(String debugPath,String mainPath,String args){
		// temporarily
		//mainPath = "/cygdrive/c/Users/ncupeng/Desktop/diva/Examples/cpp/bw2/a.exe";
		
		String command="gdb?-q?\""+mainPath+"\"";
		
		if(!debugPath.equals("")){
			if(!debugPath.endsWith("\\"))
				debugPath=debugPath+"\\";
			command="\""+debugPath+"gdb\"?-q?\""+mainPath+"\"";
		}
		String[] commandArray=command.split("\\?");
		return commandArray;
	}
	
	public static String getTypeValue(String whatisResult,String varName){	
		String tokens[]=whatisResult.split("[\\n\\r\\t]");
		String varType="";
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].contains("type = ")){
				varType=tokens[i].substring(tokens[i].indexOf("type = ")+7);
				return varType;
			}
		}
		return null;
	}
	public static String getAddrValue(String printResult){
		String tokens[]=printResult.split("[\\n\\r\\t]");
		String address="";
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].contains(" = ")){
				String dataLine[]=tokens[i].split(" ");
				address=dataLine[dataLine.length-1];
				if(address.contains("\"")||address.contains("\'"))
					address=dataLine[dataLine.length-2];
				return address;
			}
		}
		return null;
	}
	
	public static String formAskResult(String printResult,String varName,String varType,String varAddr){
		varType=varType.replace(" ", "");
		if((varType==null)||(varAddr==null))
			return formAskNullResult(varName);
		if(varType.contains("*")&&varType.contains("[")){
			String varPreType=varType.substring(0,varType.indexOf("["));
			String varPostType=varType.substring(varType.indexOf("["));
			varPreType="("+varPreType+")";
			varType=varPreType+varPostType;
		}
		else{
			varType=varType.replace("&", "");
		}
		return VARNAME+" "+varName+" "+VARTYPE+" "+varType+" "+ADDR+" "+varAddr;
	}
	
	public static String formAskNullResult(String varName){
		return VARNAME+" "+varName+" "+VARTYPE+" null "+ADDR+" none";
	}
	
	public static String formStructResult(String ptypeResult,String className){
		String tokens[]=ptypeResult.split("[\\n\\r]");
		int attQun=0;
		String result="";
		String attType="";
		String attName="";
		String attDim="";
		
		if(!tokens[0].contains("{"))
			return CLASSNAME+" "+className+" "+ATTQUN+" 0";
		
		for(int i=1;i<tokens.length;i++){
			if(tokens[i].startsWith("    ")&&!tokens[i].contains("(")){
				tokens[i]=tokens[i].substring(4);
				attType=attName=attDim="";
				if(tokens[i].contains("[")&&tokens[i].contains("*")){
					attType=tokens[i].substring(0,tokens[i].lastIndexOf("*")+1);
					attName=tokens[i].substring(tokens[i].lastIndexOf("*")+1,tokens[i].indexOf("["));
					attDim=tokens[i].substring(tokens[i].indexOf("["),tokens[i].indexOf(";"));
					attType=attType.replace(" ", "");
					attType="("+attType+")"+attDim;
				}
				else if(tokens[i].contains("*")){
					attType=tokens[i].substring(0,tokens[i].lastIndexOf("*")+1);
					attName=tokens[i].substring(tokens[i].lastIndexOf("*")+1,tokens[i].indexOf(";"));
					attType=attType.replace(" ", "");
				}
				else if(tokens[i].contains("[")){
					attType=tokens[i].substring(0,tokens[i].indexOf(" "));
					attName=tokens[i].substring(tokens[i].indexOf(" ")+1,tokens[i].indexOf("["));
					attDim=tokens[i].substring(tokens[i].indexOf("["),tokens[i].indexOf(";"));
					attType=attType.replace(" ", "");
					attType=attType+attDim;
				}
				else{
					attType=tokens[i].substring(0,tokens[i].indexOf(" "));
					attName=tokens[i].substring(tokens[i].lastIndexOf(" ")+1,tokens[i].indexOf(";"));
				}
				result+=" "+VARNAME+" "+attName+" "+VARTYPE+" "+attType+" ";
				attQun++;
			}
		}
		return CLASSNAME+" "+className+" "+ATTQUN+" "+attQun+result;
	}
	
	public static String getShowValue(String printResult){
		String tokens[]=printResult.split("[\\n\\r\\t]");
		String value="";
		if(printResult.contains("\""))
			return printResult.substring(printResult.indexOf("\"")+1,printResult.lastIndexOf("\""));
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].contains("{"))
				return null;
			else if(tokens[i].contains(" = ")){
				String dataLine[]=tokens[i].split(" ");
				value=dataLine[dataLine.length-1];
				if(tokens[i].contains("\""))
					value=tokens[i].substring(tokens[i].indexOf("\""),tokens[i].lastIndexOf("\"")+1);
				return value;
			}
		}
		return null;
	}
	
	public static String formShowPrimitiveResult(String varName,String value){
		value=value.replace("\'", "");
		return VARNAME+" "+varName+" "+VALUE+" "+value;
	}
	
	public static String formShowRefResult(String varName,String varAddr){
		if(varAddr==null)
			return formShowNullResult(varName);
		else
			return VARNAME+" "+varName+" "+VALUE+" "+varAddr;
	}
	
	public static String formShowNullResult(String varName){
		return VARNAME+" "+varName+" "+VALUE+" null";
	}

	public static boolean isVariableOutOfScope(String message) {
		if(!message.contains("="))
			return true;
		return false;
	}

	public static String formScopeResult(boolean b, String varName) {
		return VARNAME+" "+varName+" "+VALUE+" "+!b;
	}

	public static String formOutOfScopeResult(String varName) {
		return ERROR+" wrong variable name";
	}
}

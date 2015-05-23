package debugger.jdbCommand;

import java.util.HashMap;
import commandCenter.XdivaTag;

public class JavaMsgFormat implements XdivaTag{
	public static boolean isVarNameCurrect(String varName){
		if(varName.matches("[$_0-9A-Za-z\\[\\]\\.]+"))
			return true;
		return false;
	}
	public static String isVariableExist(String printResult){
		String tokens[]=printResult.split("[ \\n\\r]");
		if(!printResult.contains("="))
			return null;
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].equals("=")&&tokens[i+1].equals("null"))
				return null;
			if(tokens[i].equals("=")&&!tokens[i+1].equals("null"))
				return tokens[i-1];
		}
		return null;
	}
	
	public static String[] formCompileCommand(String debugPath,String projectPath,String mainPath){
		String command="";
		if(!debugPath.equals("")){
			if(debugPath.endsWith("\\"))
				debugPath=debugPath+"javac?-g?";
			else
				debugPath=debugPath+"\\javac?-g?";
		}
		else
			debugPath="javac?-g?";
		debugPath=debugPath+"?-classpath?";
		
		if(projectPath.endsWith(":"))
			projectPath=projectPath+"\\";
		else if(projectPath.contains(" "))
			projectPath="\""+projectPath+"\"";
		if(mainPath.contains(" "))
			mainPath="\""+mainPath+"\"";
		
		command=debugPath+projectPath+"?"+mainPath;
		return command.split("\\?");
	}
	public static String[] formDebugCommand(String debugPath,String projectPath,String mainPath,String args){
		String[] argsArray=args.split(" ");

		if(!debugPath.equals("")){
			if(debugPath.endsWith("\\"))
				debugPath=debugPath+"jdb";
			else
				debugPath=debugPath+"\\jdb";
		}
		else
			debugPath="jdb";
		debugPath=debugPath+"?-classpath";
		
		if(projectPath.endsWith(":"))
			projectPath=projectPath+"\\";
		else if(projectPath.contains(" "))
			projectPath="\""+projectPath+"\"";
		if(mainPath.contains(" "))
			mainPath="\""+mainPath+"\"";
		
		String command=debugPath+"?"+projectPath+"?"+mainPath;
		String[] commandArray=command.split("\\?");
			
		String[] replyCommand=new String[argsArray.length+commandArray.length];
		int length;
		
		for(length=0;length<commandArray.length;length++)
			replyCommand[length]=commandArray[length];
		for(int i=0;i<argsArray.length;i++){
			replyCommand[length]=argsArray[i];
			length++;
		}
		
		return replyCommand;
	}
	public static String formStructResult(String fieldsResult,String className){
		String tokens[]=fieldsResult.split("[\\r\\n]");
		int quanity=0;
		String result="";
		String attName="";
		String attType="";
		String attDim="";
		
		for(int i=1;i<tokens.length;i++){
			if(tokens[i].endsWith("[1] "))
				break;
			String attribute[]=tokens[i].split(" ");
			if(attribute.length<=1)
				break;
			else{
				quanity++;
				attName=attribute[1];
				attType=attribute[0];
				if(attType.equals("boolean"))
					attType="bool";
				if(attType.contains("[")){
					attDim=attType.substring(attType.indexOf("["));
					attType=attType.substring(0,attType.indexOf("["));		
					if(!isPrimitive(attType))
						attType="("+attType+"*)"+attDim+"*";
					else
						attType=attType+attDim+"*";	
				}
				else if(!isPrimitive(attType))
					attType+="*";
				result+=" "+VARNAME+" "+attName+" "+VARTYPE+" "+attType;
			}
		}
		return CLASSNAME+" "+className+" "+ATTQUN+" "+quanity+result;
	}
	
	public static String formShowResult(String printResult,String varName){
		String tokens[]=printResult.split("[\\n\\r]");
		String varValue="";
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].contains(" = ")){
				if(tokens[i].contains("\""))
					varValue=printResult.substring(printResult.indexOf("\""),printResult.lastIndexOf("\"")+1);
				else if(tokens[i].contains("instance of")){
					varValue=tokens[i].substring(tokens[i].indexOf("id="),tokens[i].lastIndexOf(")"));
				}
				else{
					varValue=tokens[i].substring(tokens[i].indexOf("=")+2);
				}
			}
		}
		return VARNAME+" "+varName+" "+VALUE+" "+varValue;
	}
	
	public static String formArrayMessage(String printResult,String dumpResult,String varName){
		String tokens[]=printResult.split("[ \\n\\r]");
		String varType="";
		String varDim="";
		int firstDim=0;
		
		try{
			if(dumpResult!=null){
				firstDim=JavaMsgFormat.getArrayDim(dumpResult);
			}
			for(int i=0;i<tokens.length;i++){
				if(tokens[i].equals("=")){
					varType=tokens[i+3].substring(0,tokens[i+3].indexOf("["));
					varDim=tokens[i+3].substring(tokens[i+3].indexOf("["));
					if(dumpResult!=null){
						varDim=varDim.replace("[]", "["+firstDim+"]");
					}
					if(!isPrimitive(varType))
						varType="("+varType+"*)";
					return VARNAME+" "+varName+" "+VARTYPE+" "+varType+varDim+"* "+ADDR+" none";
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return formNullRef(varName);
		}
		return formNullRef(varName);
	}
	public static int getArrayDim(String dumpResult){
		dumpResult=dumpResult.substring(dumpResult.indexOf("{"),dumpResult.lastIndexOf("}"));
		int arrayDim=dumpResult.split(",").length;
		return arrayDim;
	}

	public static String formSingleRef(String printResult,String dumpResult,String varName){
		String varType=null;
		if(dumpResult.contains("\"")){
			varType="java.lang.String*";
			return VARNAME+" "+varName+" "+VARTYPE+" "+varType+" "+ADDR+" none";
		}
		else if(dumpResult.contains("}") && printResult.contains("@") && printResult.contains("\"")){
			varType=printResult.substring(printResult.indexOf("\"")+1,printResult.indexOf("@"))+"*";
			return VARNAME+" "+varName+" "+VARTYPE+" "+varType+" "+ADDR+" none";
		}
		else
			return formNullRef(varName);
	}
	public static String formNullRef(String varName){
		return VARNAME+" "+varName+" "+VARTYPE+" null "+ADDR+" none";
	}
	public static String formPrimitive(String printResult,String varName){
		String tokens[]=printResult.split("[ \\n\\r]");
		String varType="";
		
		if(!printResult.contains("="))
			return formNullRef(varName);
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].equals("=")){
				if(tokens[i+1].equals("true")||tokens[i+1].equals("false"))
					varType="bool";
				else if(tokens[i+1].matches("-?[0-9]+\\.[0-9]+"))
					varType="double";
				else if(tokens[i+1].matches("-?[1-9][0-9]+"))
					varType="int";
				else if(tokens[i+1].matches("[0-9]"))
					varType="unknow";
				else if(tokens[i+1].length()==1)
					varType="char";
				else
					return formNullRef(varName);
				return VARNAME+" "+varName+" "+VARTYPE+" "+varType+" "+ADDR+" none";
			}
		}
		return formNullRef(varName);
	}
	public static String formCharInt(String oldMessage,String newMessage,String varName){
		String tokensOld[]=oldMessage.split("[ \\n\\r]");
		String tokensNew[]=newMessage.split("[ \\n\\r]");
		int oldValue=0;
		int newValue=0;
		String varType="";
		try{
			for(int i=0;i<tokensOld.length;i++){
				if(tokensOld[i].equals("="))
					oldValue=Integer.parseInt(tokensOld[i+1]);
				if(tokensNew[i].equals("="))
					newValue=Integer.parseInt(tokensNew[i+1]);
			}
		}catch(NumberFormatException e){
			oldValue=0;
			newValue=0;
		}
		if(newValue==oldValue+10)
			varType="int";
		else
			varType="char";
		return VARNAME+" "+varName+" "+VARTYPE+" "+varType+" "+ADDR+" none";
	}
	
	public static HashMap formVariableValue(String dumpResult) {
		if(dumpResult.contains("{")&&dumpResult.contains("}"))
			dumpResult=dumpResult.substring(dumpResult.indexOf("{"),dumpResult.lastIndexOf("}")+1);
		else 
			return null;
		
		String tokens[]=dumpResult.split("[\\n\\r]");
		HashMap<String,String> valueMap=new HashMap<String,String>();
		
		for(int i=0;i<tokens.length;i++){
			if(!tokens[i].contains(":"))
				continue;
			try{
				String varName=tokens[i].substring(4,tokens[i].indexOf(":"));
				String varValue=tokens[i].substring(tokens[i].indexOf(":")+2);
				valueMap.put(varName,varValue);
			}catch(ArrayIndexOutOfBoundsException exception){
				return null;
			}
		}
		return valueMap;
	}
	
	public static HashMap formLocalValue(String localResult,HashMap<String,String> valueMap){
		if(valueMap==null)
			valueMap=new HashMap<String,String>();
		
		String tokens[]=localResult.split("[\\n\\r]");
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].contains("=")){
				try{
					String varName=tokens[i].substring(0,tokens[i].indexOf("=")-1);
					String varValue=tokens[i].substring(tokens[i].indexOf("=")+2);
					valueMap.put(varName, varValue);
				}catch(ArrayIndexOutOfBoundsException exception){
					return null;
				}
			}
		}
		return valueMap;
	}
	
	public static boolean isPrimitive(String varType){
		String primitive[]={"int","boolean","char","long","double","float",
				"byte","short","void","bool"};
		for(int i=0;i<primitive.length;i++){
			if(varType.equals(primitive[i]))
				return true;
		}
		return false;
	}
	
	public static String formCompileErrorMsg(String compileMsg){
		String[] tokens=compileMsg.split("\\r\\n");
		String replyMsg="";
		for(int i=0;i<tokens.length;i++){
			if(!tokens[i].equals("")){
				if((tokens[i].charAt(0)!=9)&&(tokens[i].charAt(0)!=32)&&(tokens[i].contains(":")))
					replyMsg+=tokens[i]+"\n";
			}
		}
		return replyMsg;
	}
	public static String formStringResult(String dumpResult, String printResult, String varName) {
		String value="";
		if(dumpResult.contains("\"")){
			value=printResult.substring(printResult.indexOf("\""),printResult.lastIndexOf("\"")+1);	
		}
		else
			value=printResult.substring(printResult.indexOf("\"")+1,printResult.lastIndexOf("\""));	
		return VARNAME+" "+varName+" "+VALUE+" "+value;
	}
	public static boolean isVarOutOfScope(String message) {
		if(!message.contains("="))
			return true;
		String tokens=message.split("=")[0];
		if(tokens.contains("com.sun.tools.example.debug.expr.ParseException:"))
			return true;
		return false;
	}
	public static String formOutOfScopeResult(String varName) {
		return ERROR+" wrong variable name";
	}
	public static String formScopeResult(boolean b,String varName) {
		return VARNAME+" "+varName+" "+VALUE+" "+!b;
	}
}

package debugger.jdbCommand;

import java.io.File;
import java.util.Vector;

public class JavaFileProcessor {
	public JavaFileProcessor(){}
	public static String getPureName(String projectName,String fileName){
		if(projectName.charAt(projectName.length()-1)!='\\')
			projectName+='\\';
		String pureName=fileName.substring(projectName.length(),fileName.length());
		pureName=pureName.split("\\.")[0];
		pureName=pureName.replace("\\", ".");
		return pureName;
	}
	public static String getPureName(File projectPath,String fileName){
		return getPureName(projectPath.toString(),fileName);
	}
	public static String getPureName(String projectName,File filePath){
		return getPureName(projectName,filePath.toString());
	}
	public static String getPureName(File projectPath,File filePath){
		return getPureName(projectPath.toString(),filePath.toString());
	}
	
	public static Vector getInnerFile(File projectPath,File otherFilePath,String matchName){
		File parent=otherFilePath.getParentFile();
		File[] sibling=parent.listFiles();
		Vector<String> innerFile=new Vector<String>();
		
		for(int i=0;i<sibling.length;i++){
			if(!sibling[i].toString().endsWith(".class"))
				continue;
			String siblingName=getPureName(projectPath,sibling[i]);
			if(siblingName.startsWith(matchName+"$"))
				innerFile.add(siblingName);
		}
		return innerFile;
	}
}

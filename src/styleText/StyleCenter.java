package styleText;

import java.io.File;
import userInterface.CodePane;

/**所有字型字色變化管理中心，算是factory pattern*/
public class StyleCenter {
	private static StyleCenter styleCenter;
	protected StyleCenter(){}
	public static StyleCenter getInstance(){
		if(styleCenter==null)
			styleCenter=new StyleCenter();
		return styleCenter;
	}
	
	/** 由讀進來的codepane 和其副檔名，來決定使用那一種style */
	public CodeStyle setCodeStyle(CodePane codePane,File file){
		CodeStyle style;
		if(file==null)
			style=new PureStyle(codePane);
		else{
			String subFileName;
			if(file.toString().contains("."))
				subFileName=file.toString().substring(file.toString().lastIndexOf("."));
			else
				subFileName="";
			
			if(subFileName.equalsIgnoreCase(".java"))
				style=new JavaStyle(codePane);
			else if(subFileName.equalsIgnoreCase(".c")||subFileName.equalsIgnoreCase(".cpp")||subFileName.equalsIgnoreCase(".h"))
				style=new CppStyle(codePane);
			else
				style=new PureStyle(codePane);
		}
		return style;
	}
}

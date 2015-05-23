package styleText;

import javax.swing.text.Style;
import userInterface.CodePane;

/**字型style的母class，abstract的*/
public abstract class CodeStyle {
	CodePane codePane;  /**管理的panel是那個*/
	
	public abstract void renderCodeStyle();				/**掃描文件從新繪製字體顏色*/
	public abstract boolean isKeyWord(String word);		/**回傳該word是否是keyword*/
	public abstract boolean isVariableChar(int aChar);  /**回傳該char是否可以當變數名稱*/
	
	/**將範圍內的字，以指定的style變換字色*/
	protected boolean outputBuffer(int startPos,int length,Style outputStyle){
		codePane.getStyledDocument().setCharacterAttributes(startPos,length, outputStyle, true);
		return false;
	}
	
	/**以滑鼠指標的所在位置進行展開，找尋位置上是否有變數*/
	public String getVariableAT(String text,int caretPosition){
		String editStr=text.replace("\r", "");
		
		int catchStart;
		int catchEnd;

		if(caretPosition<=0 || caretPosition>=editStr.length())
			return "";
		else{
			catchStart=caretPosition-1;
			catchEnd=caretPosition-1;
			while((catchStart>=0)&&this.isVariableChar(editStr.charAt(catchStart))){
				catchStart--;
			}
			while(((catchEnd<editStr.length()-1)&&this.isVariableChar(editStr.charAt(catchEnd)))){
				catchEnd++;
			}
			if(catchStart>=catchEnd)
				return "";
			return editStr.substring(catchStart+1,catchEnd);
		}
	}
}

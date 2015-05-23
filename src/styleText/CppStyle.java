package styleText;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import userInterface.CodePane;

/**c及cpp的字型變色系統*/
public class CppStyle extends CodeStyle{
	private Style keywordStyle;       /**關鍵字*/
	private Style normalStyle;        /**正常的字*/
	private Style commentStyle;       /**註解的字*/
	private Style stringStyle;        /**字串字元的字*/
	private Style preProcessorStyle;  /**前置處理器的字*/
	private Style defaultStyle;		  /**預設的style*/
	
	protected CppStyle(CodePane codePane){
		this.codePane=codePane;

		preProcessorStyle=codePane.addStyle("PREPROCESSOR", null);
		StyleConstants.setForeground(preProcessorStyle, new Color(200,150,0));
		StyleConstants.setItalic(preProcessorStyle, true);
		
		commentStyle=codePane.addStyle("COMMENT", null);
		StyleConstants.setForeground(commentStyle, new Color(0,150,0));
		StyleConstants.setItalic(commentStyle, true);

		stringStyle=codePane.addStyle("STRING", null);
		StyleConstants.setForeground(stringStyle, Color.RED);
		
		normalStyle=codePane.addStyle("NORMAL", null);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		
		keywordStyle=codePane.addStyle("KEYWORD", null);
		StyleConstants.setForeground(keywordStyle, Color.BLUE);
		StyleConstants.setBold(keywordStyle, true);
		
		/**取得預設的style並將他的tab size設成4*/
		defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setTabSet(defaultStyle,this.setTabSize(4));
		
		StyleConstants.setFontFamily(defaultStyle, "Courier New");
		StyleConstants.setFontSize(defaultStyle, 13);
		codePane.getStyledDocument().setParagraphAttributes(0,codePane.getDocument().getLength(),defaultStyle, true);
	}
	
	/**設定tab的大小，由w的長度*4來當tab的長度，並連續設定100個位置 4 8 12 ... 400*/
	private TabSet setTabSize(int tabSize){
		FontMetrics fm=codePane.getFontMetrics(codePane.getFont());
		int charWidth=fm.charWidth('w');
		int tabWidth=charWidth*tabSize;
		
		TabStop[] tabs=new TabStop[100];
		for(int i=0;i<tabs.length;i++){
			tabs[i]=new TabStop((i+1)*tabWidth);
		}
		return new TabSet(tabs);
	}
	
	/**啟動設定*/
	public void renderCodeStyle(){
		int textLength=codePane.getDocument().getLength();
		String text="";
		try {
			text = codePane.getDocument().getText(0, textLength-1);
		} catch (BadLocationException e) {}
		StringBuffer buffer=new StringBuffer();
		this.outputBuffer(0,textLength, normalStyle);
		
		for(int i=0;i<textLength-1;i++){
			if(this.isVariableChar(text.charAt(i))){
				buffer.append(text.charAt(i));
			}
			else{
				if(this.isKeyWord(buffer.toString()))
					this.outputBuffer(i-buffer.length(), buffer.length(), keywordStyle);
				buffer.delete(0, buffer.length());
			}
		}
		
		int startPos=0;
		int endPos=0;
		for(int i=0;i<textLength-1;i++){
			if((text.charAt(i)=='/')&&(i<textLength-2)&&(text.charAt(i+1)=='*')){
				startPos=i;
				endPos=text.indexOf("*/",i+2);
				if(endPos==-1)
					endPos=text.length();
				this.outputBuffer(startPos, endPos-startPos+2, commentStyle);
				i=endPos+1;
			}
			else if((text.charAt(i)=='/')&&(i<textLength-2)&&(text.charAt(i+1)=='/')){
				startPos=i;
				endPos=text.indexOf("\n",i+1);
				if(endPos==-1)
					endPos=text.length();
				this.outputBuffer(startPos, endPos-startPos+1, commentStyle);
				i=endPos;
			}
			else if(text.charAt(i)=='\"'){
				startPos=i;
				endPos=getMatchToken(text,startPos+1,"\"");				
				this.outputBuffer(startPos, endPos-startPos+1, stringStyle);
				i=endPos;
			}
			else if(text.charAt(i)=='\''){
				startPos=i;
				endPos=getMatchToken(text,startPos+1,"\'");
				this.outputBuffer(startPos, endPos-startPos+1, stringStyle);
				i=endPos;
			}
			else if((text.charAt(i)=='#')){
				startPos=i;
				endPos=text.indexOf("\n",i+1);
				if(endPos==-1)
					endPos=text.length();
				this.outputBuffer(startPos, endPos-startPos+1, preProcessorStyle);
				i=endPos;
			}
		}
	}
	
	/**因為會有/" /' // 等這些問題，所以必須處理一下 才能找到正確的相對應字元*/
	private int getMatchToken(String text,int position,String token){
		int skewNumber=0;
		int endPos=0;
		while(true){
			endPos=text.indexOf(token,position);
			position=endPos;
			if(endPos<0)
				return text.length();
			for(skewNumber=0;text.charAt(--position)=='\\';){
				skewNumber++;
			}	
			if(skewNumber%2==0)
				return endPos;
			else
				position=endPos+1;
		}
	} 
	
	/**c的關鍵字table*/
	public boolean isKeyWord(String word){
		if(word.equals("asm")||word.equals("auto")||word.equals("bool")||
		   word.equals("break")||word.equals("case")||word.equals("catch")||
		   word.equals("char")||word.equals("class")||word.equals("const")||
		   word.equals("asm")||word.equals("const_cast")||word.equals("continue")||
		   word.equals("default")||word.equals("delete")||word.equals("do")||
		   word.equals("double")||word.equals("dynamic_cast")||word.equals("else")||
		   word.equals("enum")||word.equals("explicit")||word.equals("export")||
		   word.equals("extern")||word.equals("false")||word.equals("float")||
		   word.equals("for")||word.equals("friend")||word.equals("goto")||
		   word.equals("if")||word.equals("inline")||word.equals("int")||
		   word.equals("long")||word.equals("mutable")||word.equals("namespace")||
		   word.equals("new")||word.equals("operator")||word.equals("private")||
		   word.equals("protected")||word.equals("public")||word.equals("register")||
		   word.equals("reinterpret_cast")||word.equals("return")||
		   word.equals("short")||word.equals("signed")||word.equals("sizeof")||
		   word.equals("static")||word.equals("static_cast")||word.equals("struct")||
		   word.equals("switch")||word.equals("template")||word.equals("this")||
		   word.equals("throw")||word.equals("true")||word.equals("try")||
		   word.equals("typedef")||word.equals("typeid")||word.equals("typename")||
		   word.equals("union")||word.equals("unsigned")||word.equals("using")||
		   word.equals("virtual")||word.equals("void")||word.equals("volatile")||
		   word.equals("wchar_t")||word.equals("while"))
			return true;
		else
			return false;
	}
	
	/** 判斷某char 是否是成為變數的char */
	/** [ A-Z a-z 0-9 $ _ ]才可*/
	public boolean isVariableChar(int aChar){
		if((aChar>=97 && aChar <=122)||(aChar>=65 && aChar<=90) || (aChar>=48 && aChar<=57)
			||aChar==36||aChar==95)
			return true;
		else
			return false;
	}
}

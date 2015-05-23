package styleText;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import userInterface.CodePane;

/**一般文件檔的字色變化*/
public class PureStyle extends CodeStyle{
	private Style normalStyle;		/**一般字色*/
	private Style defaultStyle;		/**預設字色*/

	protected PureStyle(CodePane codePane){
		this.codePane=codePane;
		
		normalStyle=codePane.addStyle("NORMAL", null);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		
		defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setTabSet(defaultStyle,this.setTabSize(4));
		
		StyleConstants.setFontFamily(defaultStyle, "Courier New");
		StyleConstants.setFontSize(defaultStyle, 13);
		codePane.getStyledDocument().setParagraphAttributes(0,codePane.getDocument().getLength(),defaultStyle, true);
	}
	
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
	public void renderCodeStyle(){
		String text=codePane.getText();
		codePane.getStyledDocument().setParagraphAttributes(0, text.length(), normalStyle, false);
	}
	public boolean isKeyWord(String word){
		return false;
	}
	public boolean isVariableChar(int aChar){
		return false;
	}
}

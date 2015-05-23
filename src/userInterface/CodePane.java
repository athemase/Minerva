package userInterface;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import javax.swing.JTextPane;

import styleText.CodeStyle;
import styleText.StyleCenter;

/**此檔案是show出code的那個板面，程式碼都顯示在其上，具3個attribute，isWidthScroll這個是
 * swing的東西，用來決定板面是否要和JScrollPane一樣大。valueMap是用來設定tooltipText的
 * 也就是當滑鼠移到變數上的時後，會show出其值，如果此變數有在value map中，value map的內容是
 * debugger寫在data裡面的。StyleCenter是我的自訂物件，用來決定字色變化的。*/
public class CodePane extends JTextPane{
	private boolean isWidthScroll=true;
	private HashMap valueMap;			/**紀錄此scope可被看到的變數群*/
	private boolean isChanged=false;	/**內容自存檔後是否有再被變動*/
	private CodeStyle codeStyle;		/**採何種變色style*/
	
	public CodePane(){
		this(null,"");
	}
	public CodePane(File file,String text){
		this.setFont(new Font("Courier New",Font.PLAIN,13));
		this.setText(text, file);
		this.addMouseMotionListener(new MouseHandler());
	}
	
	/** 和ScrollPane設定size有關 */
	public boolean getScrollableTracksViewportWidth(){
		return isWidthScroll;
	}
	public void setScrollableTracksViewPortWidth(boolean isWidthScroll){
		this.isWidthScroll=isWidthScroll;
	}
	
	/**如果使用者已有自已select的字串，則回傳select的，否則以此基準點抓取字串 
	 * 用來完成，visualize unfold fold時，使用者不用手動打入變數名字的方便功能
	 * 讓minerva以caret position為基準抓取前後字元形成字串*/ 
	public String getSelectedText(){
		if(super.getSelectedText()!=null)
			return super.getSelectedText();
		else
			return codeStyle.getVariableAT(this.getText(), this.getCaretPosition());
	}
	
	/** 使用讀檔方式讀入一個檔案時，按照讀入的檔案是那種語言，設定字體變色 */
	public void setText(String text,File file){
		text=text.replace("\r", "");
		super.setText(text);
		StyleCenter.getInstance().setCodeStyle(this, file);
	}
	
	public void setValueMap(HashMap valueMap){
		this.valueMap=valueMap;
	}
	
	/**抓取滑鼠的位置，和判斷其他的字串是不是變數，有無值可以show*/
	private class MouseHandler extends MouseAdapter{
		public void mouseMoved(MouseEvent event){
			int caretPosition=CodePane.this.viewToModel(new Point(event.getX(),event.getY()));
			String variable=codeStyle.getVariableAT(CodePane.this.getText(), caretPosition);
			if((valueMap!=null)&&(valueMap.get(variable)!=null)){
				CodePane.this.setToolTipText(variable + " = " + valueMap.get(variable));
			}
			else
				CodePane.this.setToolTipText(null);
		}
	}
	
	public void setChanged(boolean change){
		this.isChanged=change;
	}
	public boolean isChanged(){
		return this.isChanged;
	}
	public CodeStyle getCodeStyle(){
		return this.codeStyle;
	}
	public void setCodeStyle(CodeStyle codeStyle){
		this.codeStyle=codeStyle;
		codeStyle.renderCodeStyle();
	}
}

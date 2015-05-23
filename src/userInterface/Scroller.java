package userInterface;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import recordProcessor.BreakPointScript;
import styleText.StyleCenter;

/**每個tabbedpane上面的視窗，都是一個scroller，是ui的核心物件。而每個scroller物件都會裝3樣東西
 * 1.CodePane : 顯示code的那個pane
 * 2.LineArea : 顯示行號的那個area
 * 3.coverFile : 這個視窗所擁有的檔案*/
public class Scroller extends JScrollPane implements CaretListener,DocumentListener,AdjustmentListener{
	private CodePane codePane;
	private LineArea lineArea;
	private int lineCount;
	private File coverFile;
	
	public Scroller(CodePane codePane){
		/**設定顯示code的panel*/
		super(codePane);
		this.getViewport().setBackground(Color.WHITE);
		this.codePane=codePane;
		
		/**設定行號列*/
		lineArea=new LineArea();
		this.setRowHeaderView(lineArea);
		resetLineArea();
		this.setViewportBorder(BorderFactory.createEtchedBorder());
		
		/**事件處理器*/
		codePane.addCaretListener(this);
		codePane.getStyledDocument().addDocumentListener(this);
		this.getHorizontalScrollBar().addAdjustmentListener(this);
	}
	
	/**滑鼠指標移動後*/
	public void caretUpdate(CaretEvent event){
		if(codePane.getPreferredSize().width<this.getViewport().getWidth())
			codePane.setScrollableTracksViewPortWidth(true);
		else
			codePane.setScrollableTracksViewPortWidth(false);
		resetLineArea();
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				codePane.getCodeStyle().renderCodeStyle();
			}
		});
	}
	public LineArea getLineArea(){
		return lineArea;
	}
	public File getCoverFile(){
		return coverFile;
	}
	/**設定檔案，和變色系統*/
	public void setCoverFile(String name,File mainFile){
		this.setName(name);
		this.coverFile=mainFile;
		codePane.setCodeStyle(StyleCenter.getInstance().setCodeStyle(codePane,coverFile));
	}
	
	/**當中斷點移動時，除了需要告知LineArea從新畫行號以外，也要將畫面帶到那個點附近*/
	public void setNowPoint(int nowPoint){
		this.getVerticalScrollBar().setValue(nowPoint*15-60);
		lineArea.setNowPoint(nowPoint);
	}
	public CodePane getCodePane(){
		return codePane;
	}
	
	/**為了和一般的ide一樣，有多少行就顯示多少行*/
	private void resetLineArea(){
		String text=codePane.getText();
		int lineCount=1;
		for(int i=0;i<text.length();i++){
			if(text.charAt(i)=='\n')
				lineCount++;
		}
		if(this.lineCount!=lineCount){
			this.lineCount=lineCount;
			lineArea.setLineArea(this.lineCount);
		}
	}
	
	public void setHistoryBreakPoints(HashMap<Integer,BreakPointScript> bpSet){
		lineArea.setHistoryBreakPoints(bpSet);
	}
	
	private void setNameAfterChange(){
		if(!codePane.isChanged()){
			codePane.setChanged(true);
			int order=TabPane.getInstance().getComponentZOrder(this);
			String titleName=TabPane.getInstance().getTitleAt(order);
			if(!titleName.endsWith("(*)"))
				TabPane.getInstance().setTitleAt(order, titleName+"(*)");
		}
	}
	
	public void changedUpdate(DocumentEvent event){}
	public void insertUpdate(DocumentEvent arg0){
		setNameAfterChange();
	}
	public void removeUpdate(DocumentEvent arg0){
		setNameAfterChange();
	}

	public void adjustmentValueChanged(AdjustmentEvent event) {
		this.caretUpdate(null);
	}
}
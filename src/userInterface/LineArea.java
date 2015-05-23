package userInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextPane;

import recordProcessor.BreakPointScript;
import userInterface.configDialog.ScriptPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;

/**這個檔案是用來顯示行號列的那個panel，上面有實做滑鼠按下事件，來判斷加中斷點的事件。
 * 中斷點是記在一個treeset裡，也就是markset。而nowpoint是指如果目前程式跑到這個檔
 * 按的某一行，則要show出黃色的三角形，指出目前程式跑到這一行。*/
public class LineArea extends JTextPane implements Observer{
	private HashMap<Integer,BreakPointScript> markSet;
	private BreakPointScript bData;
	int nowPoint;
	int markPoint;
	
	public LineArea(){
		markSet=new HashMap<Integer,BreakPointScript>();
		this.setFont(new Font("Courier New",Font.PLAIN,13));
		this.setBackground(new Color(224,224,224));
		this.setEditable(false);
		this.addMouseListener(new MouseHandler());
	}
	
	/**當使用者按enter之類的按鈕，讓行號列增減時，則會呼叫這個method，從新設定行號總列數*/
	public void setLineArea(int lineCount){
		StringBuffer strBuffer=new StringBuffer();
		for(int i=1;i<=lineCount;i++)
			strBuffer.append("  "+i+"\n");
		this.setText(strBuffer.toString());
	}
	public HashMap<Integer,BreakPointScript> getBreakPoint(){
		return markSet;
	}
	/**設定目前的點，並從新畫*/
	public void setNowPoint(int nowPoint){
		this.nowPoint=nowPoint;
		repaint();
	}
	/**debugger停止後，或者中斷點移動後，則要清掉上次的nowpoint。*/
	public void clearNowPoint(){
		nowPoint=0;
		repaint();
	}
	/**從新繪置板面，按markset的中斷點情形，和nowpoint的值來畫*/
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d=(Graphics2D)g;
		
		Iterator it=markSet.keySet().iterator();
		/**繪制所有的中斷點，用漸層色*/
		while(it.hasNext()){
			Integer markPoint=(Integer)it.next();
			GradientPaint gradient=new GradientPaint(new Point(3,markPoint*15-3),new Color(255,0,0),new Point(13,markPoint*15+8),new Color(255,255,255));
			g2d.setPaint(gradient);
			g2d.fill(new Ellipse2D.Double(4,markPoint*15-12,13,13));
		}
		/**繪制現在程式的停止點，黃色小三角*/
		if(nowPoint>0){
			g.setColor(Color.YELLOW);
			int xPoint[]={7,7,16};
			int yPoint[]={nowPoint*15-11,nowPoint*15-1,nowPoint*15-6};
			g.fillPolygon(xPoint, yPoint, 3);
			g.setColor(new Color(200,200,0));
			g.drawPolygon(xPoint, yPoint, 3);
		}
	}
	public int getNowPoint(){
		return this.nowPoint;
	}
	/**使用者按下這個panel的時後，判斷他按的x y點，來加入中斷點，如果中斷點已存在，則取消中斷點*/
	private class MouseHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent event){
			/**使用者按下滑鼠左鍵觸發*/
			markPoint=event.getY()/15+1;			
			if(event.getButton() == MouseEvent.BUTTON1){
				
				if(markSet.containsKey(markPoint)){
					markSet.remove(markPoint);
				}
				else{
					markSet.put(markPoint, new BreakPointScript("",false));
				}
				LineArea.this.repaint();
			}
			/**使用者按下滑鼠右鍵觸發script dialog ,透過observer pattern 將在script dialog所設定的script data
			 通知LineArea的script data要做更新*/
			else if(event.getButton() == MouseEvent.BUTTON3){
				bData = ScriptPanel.getInstance().getScript();
				bData.addObserver(LineArea.this);
			}
		}
	}
	/**透過讀取紀錄檔案，一口氣set許多break point*/
	public void setHistoryBreakPoints(HashMap<Integer,BreakPointScript> bpSet) {
		Iterator kitr=bpSet.keySet().iterator();
		Iterator vitr = bpSet.values().iterator();
		while(kitr.hasNext()){
			markSet.put((Integer)kitr.next(), (BreakPointScript)vitr.next());
		}
	}

	public void update(Observable arg0, Object arg1) {
		bData = (BreakPointScript)arg0;
		if(markSet.keySet().contains(markPoint)){
			markSet.remove(markPoint);
			markSet.put(markPoint, new BreakPointScript(bData.getScript(),bData.getStatus()));			
		}
		
	}
}

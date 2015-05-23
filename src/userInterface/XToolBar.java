package userInterface;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.BorderFactory;

import commandCenter.CommandCenter;
import userInterface.configDialog.OptionPanel;
import userInterface.runDialog.RunDialog;

/**minerva的toolbar，相當簡單。*/
public class XToolBar extends JToolBar implements ActionListener{
	private static final int RUN=0;
	private static final int STOP=1;
	private static final int STEP=2;
	private static final int NEXT=3;
	private static final int CONT=4;
	private static final int VISUALIZE=5;
	private static final int UNFOLD=6;
	private static final int FOLD=7;
	private static final int ANIMATE=8;
	
	private static final int BUTTON_NUM=9;
	JButton toolButton[];
	
	public XToolBar(String buttonStyle){
		this.setButton();
		this.setButtonIcon(buttonStyle);
		this.setFloatable(false);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	}
	
	/** 將toolButton 放入toolbar 並加入actionListener */
	private void setButton(){
		toolButton=new JButton[BUTTON_NUM];
		for(int i=0;i<BUTTON_NUM;i++){
			toolButton[i]=new JButton();
			toolButton[i].addActionListener(this);
			this.add(toolButton[i]);
		}
		toolButton[RUN].setToolTipText("RUN");
		toolButton[STOP].setToolTipText("STOP");
		toolButton[STEP].setToolTipText("STEP");
		toolButton[NEXT].setToolTipText("NEXT");
		toolButton[CONT].setToolTipText("CONT");
		toolButton[VISUALIZE].setToolTipText("VISUALIZE");
		toolButton[UNFOLD].setToolTipText("UNFOLD");
		toolButton[FOLD].setToolTipText("FOLD");
		toolButton[ANIMATE].setToolTipText("ANIMATE");
	}
	
	/** 設定button的icon 傳入的style就代表其icon所放置的資料夾位置 */
	public void setButtonIcon(String style){
		toolButton[RUN].setIcon(new ImageIcon("Icon\\"+style+"\\run.jpg"));
		toolButton[STOP].setIcon(new ImageIcon("Icon\\"+style+"\\stop.jpg"));
		toolButton[STEP].setIcon(new ImageIcon("Icon\\"+style+"\\step.jpg"));
		toolButton[NEXT].setIcon(new ImageIcon("Icon\\"+style+"\\next.jpg"));
		toolButton[CONT].setIcon(new ImageIcon("Icon\\"+style+"\\cont.jpg"));
		toolButton[VISUALIZE].setIcon(new ImageIcon("Icon\\"+style+"\\visualize.jpg"));
		toolButton[UNFOLD].setIcon(new ImageIcon("Icon\\"+style+"\\unfold.jpg"));
		toolButton[FOLD].setIcon(new ImageIcon("Icon\\"+style+"\\fold.jpg"));
		toolButton[ANIMATE].setIcon(new ImageIcon("Icon\\"+style+"\\animate.jpg"));
	}
	
	/**和menubar做的事幾乎一樣*/
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==toolButton[RUN]){
			RunDialog.getInstance();
		}
		else if(event.getSource()==toolButton[STOP]){
			CommandCenter.stop();
		}
		else if(event.getSource()==toolButton[STEP]){
			CommandCenter.step();
		}
		else if(event.getSource()==toolButton[NEXT]){
			CommandCenter.next();
		}
		else if(event.getSource()==toolButton[CONT]){
			CommandCenter.cont();
		}
		else if(event.getSource()==toolButton[VISUALIZE]){
			OptionPanel.visualize();
		}
		else if(event.getSource()==toolButton[UNFOLD]){
			OptionPanel.unfold();
		}
		else if(event.getSource()==toolButton[FOLD]){
			OptionPanel.fold();
		}
		else if(event.getSource()==toolButton[ANIMATE]){
			CommandCenter.animate();
		}
	}
}

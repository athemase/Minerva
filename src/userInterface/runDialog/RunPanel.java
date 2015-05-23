package userInterface.runDialog;

import javax.swing.JPanel;

public abstract class RunPanel extends JPanel{
	public abstract void confirm();
	public abstract void cancel();
	public abstract void easyRun();
	public abstract void setHistoryValue();
}

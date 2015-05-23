package userInterface.configDialog;

import javax.swing.JPanel;

/**所有config panel的super class，定義2個method，這2個method分別為了當使用者按下
 * ok和cancel時要呼叫的。*/
public abstract class ConfigPanel extends JPanel{
	public abstract void confirm();
	public abstract void cancel();
}

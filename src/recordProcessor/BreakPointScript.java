package recordProcessor;

import java.util.Observable;

public class BreakPointScript extends Observable{
	private String script;
	private boolean isCont;
	private boolean isLoad;
	public BreakPointScript(String script,boolean isCont){
		this.script = script;
		this.isCont = isCont;
		this.isLoad = false;
	}
	public void setScriptData(String script,boolean isCont){
		this.script = script;
		this.isCont = isCont;
		this.setChanged();
		this.notifyObservers();
	}
	public String getScript(){
		return this.script;
	}
	public String loadScript(){
		if(this.isLoad == false){
			this.isLoad = true;
			return this.script;
		}		
		return "";
	}
	public boolean getStatus(){
		return this.isCont;
	}
	public boolean getLoadStatus(){
		return this.isLoad;
	}
	public void setLoadState(boolean status) {
		this.isLoad = status;		
	}
}

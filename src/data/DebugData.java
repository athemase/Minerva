package data;

import java.util.Observable;
import java.util.HashMap;

public class DebugData extends Observable{
	private static DebugData data;
	/**現在停在那一行*/
	private int curLine;
	/**現在停在那個file*/
	private String curFile; 
	/**現在執行的project path*/
	private String curProject;
	/**已知變數暫存map*/
	private HashMap valueMap;  
	/**debug啟動中?*/
	private boolean isRunning;    
	
	protected DebugData(){}
	public static DebugData getInstance(){
		if(data==null)
			data=new DebugData();
		return data;
	}
	/**設定目前執行到的檔案和行數，透過observer pattern給ui*/
	public void setCurPosition(String curFile,int curLine){
		this.curFile=curFile;
		this.curLine=curLine;
		this.setChanged();
		this.notifyObservers(new String("POSITION CHANGE"));
	}
	public void setCurLine(int curLine){
		this.curLine=curLine;
		this.setChanged();
		this.notifyObservers(new String("POSITION CHANGE"));
	}
	
	/**目前jdb專用，將dump this , locals，這2個命令得到的變數資訊放入HashMap*/
	public void setValueMap(HashMap valueMap){
		this.valueMap=valueMap;
		this.setChanged();
		this.notifyObservers("SET VALUEMAP");
	}
	/**設定目前的專案路徑，classpath*/
	public void setCurProject(String projectName){
		this.curProject=projectName;
	}
	
	/**重新啟動要清空message 這個output buffer*/
	public void setRunning(boolean isRunning){
		this.isRunning=isRunning;
		if(this.isRunning==false){
			this.setChanged();
			this.notifyObservers("DEBUGGER STOP");
		}
		else if(this.isRunning==true){
			this.setChanged();
			this.notifyObservers("DEBUGGER START");
		}
	}
	
	public int getCurLine(){
		return curLine;
	}
	public String getCurFile(){
		return curFile;
	}
	public HashMap getValueMap(){
		return valueMap;
	}
	public String getCurProject(){
		return curProject;
	}
}

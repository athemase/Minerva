package commandCenter;

import java.io.IOException;
import java.util.ArrayList;

import data.DebugData;

/**一條thread，隨時接收debugger的訊息，告訴minerva*/
public class MessageCollector extends Thread{
	private static MessageCollector actionTrigger;
	/**一般而言，只放一個debugger，(可能可以移除)*/
	ArrayList<DebugProcess> cmdArray; 
	/**暫存訊息的buffer*/
	StringBuffer strBuffer;             
	DebugData data;
	char buffer[];
	
	protected MessageCollector(){
		cmdArray=new ArrayList<DebugProcess>();
		buffer=new char[4096];
		this.start();
	}
	public static MessageCollector getInstance(){
		if(actionTrigger==null)
			actionTrigger=new MessageCollector();
		return actionTrigger;
	}
	public void addCommand(DebugProcess thisCE){
		cmdArray.add(thisCE);
	}
	
	public void run(){
		while(true){
			for(int i=0;i<cmdArray.size();i++){
				final DebugProcess thisDP=(DebugProcess)cmdArray.get(i);
				try{
					/**如果debugger未完了，則會丟出IllegalThreadStateException，則try的內容不執行*/
					/**否則，會執行這段try的內容(表示debugger已完了)*/
					thisDP.getProcess().exitValue();
					if(thisDP==CommandCenter.debugBridge.getDebugger()){
						strBuffer=null;
						CommandCenter.debugBridge.removeDebugger();
						CommandCenter.debugBridge=null;
						DebugData.getInstance().setRunning(false);
						System.err.println(MinervaMessage.TERMINATE);
					} 
					else{
						try {
							CommandCenter.debugBridge.messageParser(strBuffer.toString());
							strBuffer=null;
						} catch (IOException e){}
					}
					cmdArray.remove(thisDP);
					i--;
					continue;
				}catch(IllegalThreadStateException exception){
					
				}
			
				try{
					/**如果結果抓取完了，則parse結果*/
					if(!thisDP.getReader().ready()){
						if(strBuffer!=null && CommandCenter.debugBridge.commandCompleted(strBuffer.toString())){
							CommandCenter.debugBridge.messageParser(strBuffer.toString());							
							strBuffer=null;
						}
					}
					if(strBuffer==null)
						strBuffer=new StringBuffer();
					
					/**否則，則繼續從Process的reader繼續抓取字串*/
					int length;
					while (thisDP.getReader().ready()&&(length=thisDP.getReader().read(buffer,0,buffer.length))!=-1){
						strBuffer.append(new String(buffer,0,length));
					}
				}catch(IOException exception){}
			}
			if(cmdArray.size()==0){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {}
			}
			else{
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {}
			}
		}
	}
}

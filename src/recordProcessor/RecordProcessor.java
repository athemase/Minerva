package recordProcessor;

import java.util.HashMap;
import java.util.Vector;

/**檔案管理proxy，管理底下的4個開檔程式，DebugHistory OpenedFile Setting BreakpoingHistory*/
public class RecordProcessor {
	public static RecordProcessor recordProcessor;
	private HashMap userSettingMap; 
	private Vector openFileVector;
	private HashMap debugHistoryMap;
	private HashMap breakpointMap;
	
	/**facade pattern，將4個檔案處理器的hashmap或vector放在自已下面*/
	protected RecordProcessor(){
		openFileVector=OpenedFileProcessor.getInstance().getHistoryMap();
		userSettingMap=UserSettingProcessor.getInstance().getSettingMap();
		debugHistoryMap=DebugHistoryProcessor.getInstance().getHistoryMap();
		breakpointMap=BreakPointProcessor.getInstance().getHistoryMap();
	}
	public static RecordProcessor getInstance(){
		if(recordProcessor==null)
			recordProcessor=new RecordProcessor();
		return recordProcessor;
	}
	
	/**4個檔案處理器的setter，讓外面的class透過這邊將資訊update到4個proccessor*/
	public void setUserSettingMap(HashMap settingMap){
		this.userSettingMap=settingMap;
	}
	public void setOpenFileMap(Vector openFileVector){
		this.openFileVector=openFileVector;
	}
	public void setDebugHistoryMap(HashMap debugHistoryMap){
		this.debugHistoryMap=debugHistoryMap;
	}
	public void setBreakPointData(String fileName,long modifyTime,HashMap<Integer,BreakPointScript> bpSet){
		BreakPointProcessor.getInstance().update(fileName, modifyTime, bpSet);
	}
	
	/**4個檔案處理器的hashmap或vector的getter，讓外面的class透過這邊取得資訊*/
	public HashMap getUserSettingMap(){
		return userSettingMap;
	}
	public Vector getOpenFileVector(){
		return openFileVector;
	}	
	public HashMap getDebugHistoryMap(){
		return debugHistoryMap;
	}
	public HashMap getBreakPoingMap(){
		return breakpointMap;
	}
	public BreakPointData getFileBreakPoint(String fileName){
		return BreakPointProcessor.getInstance().getFileBreakPoint(fileName);
	}
	
	/**4個檔案處理器的更新器，這邊是做總更新用，通常是程式關閉時呼叫*/
	public void updateDebugHistory(){
		DebugHistoryProcessor.getInstance().update();
	}
	public void updateOpenedFile(String openFileList){
		OpenedFileProcessor.getInstance().update(openFileList);
	}
	public void updateUserSetting(){
		UserSettingProcessor.getInstance().update();
	}
	public void updateBreakPointHistory(){
		BreakPointProcessor.getInstance().updateAll();
	}
}

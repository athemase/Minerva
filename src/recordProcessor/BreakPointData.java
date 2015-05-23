package recordProcessor;

import java.util.HashMap;

/**由breakPointHistory取得break point的訊息並儲存起來的資料結構
 * 一個BreakPointData對應一個檔案*/
public class BreakPointData {
	private String fileName;			   /**有中斷點紀錄的檔案名字*/
	private String lastModifiedTime;       /**該檔案最後被修改的時間*/
	private HashMap<Integer,BreakPointScript> breakPointsInfo;
	private boolean isRead;
	public BreakPointData(String fileName,String lastModifiedTime,HashMap<Integer,BreakPointScript> info){
		this.fileName=fileName;
		this.lastModifiedTime=lastModifiedTime;
		this.breakPointsInfo = info;
		this.isRead = false;
	}
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	public String getFileName(){
		return fileName;
	}
	public void setModifyTime(String modifyTime){
		this.lastModifiedTime=modifyTime;
	}
	public String getModifyTime(){
		return this.lastModifiedTime;
	}
	public HashMap<Integer,BreakPointScript> getBreakPointScript(){
		return this.breakPointsInfo;
	}
	public void setBreakPoint(HashMap<Integer,BreakPointScript> info){
		this.breakPointsInfo = info;
	}
	public void setReadStatus(boolean status){
		this.isRead = status;
	}
	public boolean getReadStatus(){
		return this.isRead;
	}
}

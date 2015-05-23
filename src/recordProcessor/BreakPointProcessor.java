package recordProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**讀取BreakPointHistory.ini並建立對應的BreakPointData來給minerva做預設的中斷點設定*/
public class BreakPointProcessor {
	private static BreakPointProcessor breakPointReader;
	private HashMap<String,BreakPointData> breakPointMap;	/**用來儲存所有breakpoint資料的hashmap <filename,breakpointdata>*/
	private BufferedReader reader;
	private BufferedWriter writer;
	
	/**建構子，建立一個HashMap來存放各個檔案的中斷點紀錄，key是檔名，entry是breakpointdata*/
	protected BreakPointProcessor(){
		breakPointMap=new HashMap<String,BreakPointData>();
		readHistoryFile();
	}
	
	public static BreakPointProcessor getInstance(){
		if(breakPointReader==null)
			breakPointReader=new BreakPointProcessor();
		return breakPointReader;
	}
	/**讀取中斷點紀錄檔，存入hashmap*/
	private void readHistoryFile(){
		String line="";
		try {
			reader=new BufferedReader(new FileReader("BreakPointHistory.ini"));
			/**讀取BreakPointHistory.ini主迴圈，檔案格式是由
			 * 檔名 最後修改日期 中斷點行號(s) 以?為格開單位，因為windows接受檔名具有空白*/
			while((line=reader.readLine())!=null){
				if(line.equals("")){
					continue;
				}
				String tokens[]=line.split("\\?");
				String fileName=tokens[0];
				String modifyTime=tokens[1];
				HashMap<Integer,BreakPointScript> bpScript = new HashMap<Integer,BreakPointScript>();
				while(!(line=reader.readLine()).equals("}")){
					if(line.equals("{"))
						continue;
					else{
						String bpTokens[] = line.split("\\%");
						String lineNum = bpTokens[1];
						String script = bpTokens[2];
						String noneStop = bpTokens[3];
						if(noneStop.equals("true")){
							bpScript.put(Integer.parseInt(lineNum), new BreakPointScript(script,true));
						}
						else{
							bpScript.put(Integer.parseInt(lineNum), new BreakPointScript(script,false));
						}						
					}
				}
				BreakPointData data=new BreakPointData(fileName,modifyTime,bpScript);
				breakPointMap.put(fileName, data);
			}
		} catch (Exception exception){} 
	}
	
	public HashMap<String,BreakPointData> getHistoryMap(){
		return breakPointMap;
	}
	
	/**取出特定檔案的break point 記錄*/
	public BreakPointData getFileBreakPoint(String fileName){
		BreakPointData data=breakPointMap.get(fileName);
		if(data != null){
			data.setReadStatus(true);
		}
		return data;
	}
	
	/**更新特定檔案的中斷點紀錄，如檔案原來並不存在在breakpointHistory.ini中，則建立一筆*/
	public void update(String fileName,long modifyTime,HashMap<Integer,BreakPointScript> bpSet){
		BreakPointData data=breakPointMap.get(fileName);
		if(data!=null){
			data.setModifyTime(modifyTime+"");
			data.setBreakPoint(bpSet);
			data.setReadStatus(true);
		}
		else{
			data=new BreakPointData(fileName,""+modifyTime,bpSet);
			data.setReadStatus(true);
			breakPointMap.put(fileName, data);			
		}
	}
	/**更新所有開啟中檔案的中斷點紀錄，當程式要關閉時才執行*/
	public void updateAll(){
		Iterator it=breakPointMap.keySet().iterator();
		try{
			writer=new BufferedWriter(new FileWriter("BreakPointHistory.ini"));
			while(it.hasNext()){
				BreakPointData data=(BreakPointData)breakPointMap.get(it.next());
				HashMap<Integer,BreakPointScript> bpSet=data.getBreakPointScript();
				if(!bpSet.isEmpty() && data.getReadStatus()){
					writer.write(data.getFileName()+"?");
					writer.write(data.getModifyTime()+"\r\n{\r\n");
					Set<Integer> bnSet=bpSet.keySet();
					Collection<BreakPointScript> bsSet = bpSet.values();
					Iterator nItr = bnSet.iterator();
					Iterator sItr = bsSet.iterator();
					while(nItr.hasNext()){
						BreakPointScript temp = (BreakPointScript)sItr.next();
						writer.write(" %"+nItr.next().toString());
						writer.write("%"+temp.getScript());
						writer.write("%"+temp.getStatus()+"\r\n");
					}
					writer.write("}\r\n");
					writer.newLine();
					writer.flush();
				}
			}
			writer.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}

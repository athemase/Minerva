package recordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**這個檔案用來處理過往的debug紀錄，讀入DebugHistory.ini，如果遇到以前有debug過的file，可以直
 * 接幫使用者設定好預設值，省得使用者操作run那個視窗*/
public class DebugHistoryProcessor {
	private static DebugHistoryProcessor debugHistoryReader;
	private HashMap<String,String> historyMap;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	protected DebugHistoryProcessor(){
		historyMap=new HashMap<String,String>();
		readHistoryFile();
	}
	public static DebugHistoryProcessor getInstance(){
		if(debugHistoryReader==null)
			debugHistoryReader=new DebugHistoryProcessor();
		return debugHistoryReader;
	}
	
	/**讀入DebugHistory.ini，並以"?"為分隔條件，將字串存入hashmap*/
	private void readHistoryFile(){
		String line="";
		try {
			reader=new BufferedReader(new FileReader("DebugHistory.ini"));
			while((line=reader.readLine())!=null){
				String tokens=line.split("[\\n\\r]")[0];
				String key=tokens.substring(0,tokens.indexOf("?"));
				String value=tokens.substring(tokens.indexOf("?")+1);
				historyMap.put(key, value);
			}
		} catch (Exception exception){} 
	}
	
	/**如果有新的debug紀錄，會存入hashmap中*/
	public void update(){
		Set set=historyMap.keySet();
		Iterator it=set.iterator();
		String key="";
		String value="";
		
		try {
			writer=new BufferedWriter(new FileWriter("DebugHistory.ini"));
			while(it.hasNext()){
				key=(String)it.next();
				value=(String)historyMap.get(key);
				writer.write(new String(key+"?"+value));
				writer.newLine();
				writer.flush();
			}
			writer.close();
		}catch (IOException e) {}
	}
	
	public HashMap getHistoryMap(){
		return historyMap;
	}
}

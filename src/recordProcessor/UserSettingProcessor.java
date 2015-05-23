package recordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**讀取使用者的設定，就一些零零碎碎的設定*/
public class UserSettingProcessor {
	private static UserSettingProcessor setting;
	private HashMap<String,String> settingMap;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	protected UserSettingProcessor(){
		settingMap=new HashMap<String,String>();
		readSettingFile();
	}
	public static UserSettingProcessor getInstance(){
		if(setting==null)
			setting=new UserSettingProcessor();
		return setting;
	}
	/** 讀取setting.ini檔的內容 並放入settingMap */
	private void readSettingFile(){
		String line="";

		try {
			reader=new BufferedReader(new FileReader("setting.ini"));
			while((line=reader.readLine())!=null){
				String tokens=line.split("[\\n\\r]")[0];
				String key=tokens.substring(0,tokens.indexOf("="));
				String value=tokens.substring(tokens.indexOf("=")+1);
				settingMap.put(key, value);
			}
		} catch (Exception exception){} 
	}
	
	/** 將新設定寫入 setting.ini檔 */
	public synchronized void update(){
		Set set=settingMap.keySet();
		Iterator it=set.iterator();
		String key="";
		String value="";
		
		try{
			writer=new BufferedWriter(new FileWriter("setting.ini"));
			while(it.hasNext()){
				key=(String)it.next();
				value=(String)settingMap.get(key);	
				writer.write(new String(key+"="+value));
				writer.newLine();
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {}
	}
	
	public HashMap getSettingMap(){
		return settingMap;
	}
}

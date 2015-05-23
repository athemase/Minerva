package recordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**這個檔案用來處理開檔紀錄，當minerva啟動以後，就會讀取這個檔案，將使用者上次未關閉的視窗開啟*/
public class OpenedFileProcessor {
	private static OpenedFileProcessor openfileHistory;
	private Vector<String> historyVector;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	protected OpenedFileProcessor(){
		historyVector=new Vector<String>();
		readOpenedFile();
	}
	public static OpenedFileProcessor getInstance(){
		if(openfileHistory==null)
			openfileHistory=new OpenedFileProcessor();
		return openfileHistory;
	}
	
	/**讀取開檔紀錄檔，直接將一列資訊放入vector*/
	private void readOpenedFile(){
		String line="";
		try {
			reader=new BufferedReader(new FileReader("OpenedFile.ini"));
			while((line=reader.readLine())!=null){
				if(!line.equals(""))
					historyVector.add(line);
			}
		} catch (Exception exception){} 
	}
	
	/**程式關閉前，會自動呼叫這個來更新開檔紀錄，如果程式正常關閉*/
	public void update(String openFileList){
		String tokens[]=openFileList.split("\\n");
		
		try {
			writer=new BufferedWriter(new FileWriter("OpenedFile.ini"));
		
			for(int i=0;i<tokens.length;i++){
				writer.write(tokens[i]);
				writer.newLine();
				writer.flush();
			}
			writer.close();
		}catch(IOException excetpion){}
	}
	
	public Vector getHistoryMap(){
		return historyVector;
	}
}

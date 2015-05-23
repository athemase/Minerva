package uiCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFileChooser;

import commandCenter.CommandCenter;
import recordProcessor.BreakPointScript;
import recordProcessor.RecordProcessor;
import network.MinervaClient;
import network.MinervaServer;
import userInterface.Scroller;
import userInterface.TabPane;

/**使用者選取menu的file後觸發*/
public class FileCommand {
	/**儲存最後拜訪過的資料夾*/
	public static File lastSavedDic;
	public static File lastOpenedDic;
	
	/**開新檔案，建立一個無檔的panel*/
	public static void newFile(){
		TabPane tabPane=TabPane.getInstance();
		tabPane.addPanel();
	}
	
	/**開啟舊檔，建立一個包含開啟的檔案的panel，這個是由openfileProcessor呼叫的，並非使用者。
	 * Open history File Processor才會指定要開啟那個檔案，without use JFileChooser*/
	public static void openFile(File specificFile){
		if(specificFile==null)
			return;
		String fileName=specificFile.toString();
		TabPane tabPane=TabPane.getInstance();
		
		if(specificFile==null)
			return;
		try{
			BufferedReader input=new BufferedReader(new FileReader(specificFile));
			char buffer[] = new char[10240];
			int leng;
			StringBuffer sb=new StringBuffer();
			while((leng=input.read(buffer, 0, buffer.length))!=-1){
				sb.append(buffer,0,leng);
			}
			tabPane.addPanel(fileName.substring(fileName.lastIndexOf("\\")+1), specificFile,sb.toString());
			input.close();
		}catch(IOException exception){}
	}
	
	/**使用者按下menu的開啟舊檔時呼叫*/
	public static void openFile(){
		TabPane tabPane=TabPane.getInstance();
		
		JFileChooser jfc=new JFileChooser();
		BufferedReader input;
		
		/**啟動file chooser，讓使用者選取要開啟的檔案*/
		if(lastOpenedDic!=null)
			jfc.setCurrentDirectory(lastOpenedDic);
		
		int result=jfc.showOpenDialog(tabPane);
		if(result==JFileChooser.CANCEL_OPTION)
			return;
		File file = jfc.getSelectedFile();
		if(file==null || file.getName().equals(""))
			return;
		else{
			/**尋找此檔案是否已被開啟在minerva當中，如果是，則不從覆開啟成2個視窗*/
			lastOpenedDic=file.getParentFile();
			for(int i=0;i<tabPane.getComponentCount();i++){
				Scroller scroller=(Scroller)tabPane.getComponentAt(i);
				File existFile=scroller.getCoverFile();
				if(existFile==null)
					continue;
				if(file.toString().equals(existFile.toString()))
					return;
			}
			
			/**讀取檔案內容，並新增一個panel*/
			try{
				input=new BufferedReader(new FileReader(file));
				
				char buffer[] = new char[10240];
				int leng;
				StringBuffer sb=new StringBuffer();
				while((leng=input.read(buffer, 0, buffer.length))!=-1){
					sb.append(buffer,0,leng);
				}
				String fileName=file.toString();
				fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
				tabPane.addPanel(fileName,file,sb.toString());
				input.close();
			}catch(IOException exception){}
		}	
	}
	
	/**使用者按下 儲存檔案時*/
	public static void saveFile(){
		TabPane tabPane=TabPane.getInstance();
		BufferedWriter output;
		/**儲存使用者當下focus的panel，如果是沒有檔案的新檔案，則呼到另存新檔*/
		if(tabPane.getCurrentScroller()==null)
			return;
		if(tabPane.getCurrentScroller().getCoverFile()==null){
			saveAsFile();
			return;
		}
		else{
			/**寫入檔案內容*/
			Scroller currentScroller=tabPane.getCurrentScroller();
			String text=currentScroller.getCodePane().getText();
			try {
				output=new BufferedWriter(new FileWriter(currentScroller.getCoverFile()));
				output.write(text);
				output.flush();
				output.close();
			} catch (IOException e) {}
			/**從新設定panel title，如果具有(*)則拿掉*/
			int order=TabPane.getInstance().getComponentZOrder(currentScroller);
			String titleName=TabPane.getInstance().getTitleAt(order);
			boolean changed=currentScroller.getCodePane().isChanged();
			if(changed&&titleName.endsWith("(*)")){
				TabPane.getInstance().setTitleAt(order, titleName.substring(0,titleName.lastIndexOf("(")));
				currentScroller.getCodePane().setChanged(false);
			}
		}
	}
	
	/**另存新檔*/
	public static void saveAsFile(){
		TabPane tabPane=TabPane.getInstance();
		if(tabPane.getCurrentScroller()==null)
			return;
		
		/**彈跳jfilechooser讓使用者決定要儲存的檔名*/
		JFileChooser jfc=new JFileChooser();
		BufferedWriter output;
		if(lastSavedDic!=null)
			jfc.setCurrentDirectory(lastSavedDic);
		int result=jfc.showSaveDialog(tabPane);
		if(result==JFileChooser.CANCEL_OPTION)
			return;
		File file=jfc.getSelectedFile();
		if(file==null || file.getName().equals(""))
			return;
		else{
			/**寫入檔案內容*/
			lastSavedDic=file.getParentFile();
			Scroller currentScroller=tabPane.getCurrentScroller();
			String text=tabPane.getCurrentScroller().getCodePane().getText();
			try {
				output=new BufferedWriter(new FileWriter(file));
				output.write(text);
				output.flush();
				output.close();
			} catch (IOException e) {}
			/**從新設定panel的coverfile和title和tooltip*/
			currentScroller.setCoverFile(file.getName(),file);
			tabPane.setTitleAt(tabPane.getSelectedIndex(),((Scroller)tabPane.getSelectedComponent()).getName());
			tabPane.setToolTipTextAt(tabPane.getSelectedIndex(),file.toString());
			currentScroller.getCodePane().setChanged(false);
		}
	}
	
	/**popup menu的關閉檔案*/
	public static void closeFile(){
		TabPane tabPane=TabPane.getInstance();
		tabPane.remove(tabPane.getCurrentScroller());
	}
	
	/**離開程式時，關閉視窗時*/
	public static void exitProgram() throws IOException{
		/**如果尚有debugger未停止則停止它*/
		if(CommandCenter.debugBridge!=null){
			CommandCenter.debugBridge.stop();
		}
		/**如果socket尚未關閉則關閉它*/
		if(MinervaClient.getClient()!=null)
			MinervaClient.getInstance().closeConnection();
		if(MinervaServer.getServer()!=null){
			MinervaServer.getInstance().closeServer();
		}
		/**將開啟中的檔案寫入紀錄當中，開檔紀錄，中斷點紀錄*/
		String openFileList="";
		for(int i=0;i<TabPane.getInstance().getComponentCount();i++){
			Scroller scroller=(Scroller)TabPane.getInstance().getComponentAt(i);
			File curFile=scroller.getCoverFile();
			if(curFile!=null){
				openFileList+=curFile.toString()+"\n";
				HashMap<Integer,BreakPointScript> bpSet=scroller.getLineArea().getBreakPoint();
				RecordProcessor.getInstance().setBreakPointData(curFile.toString(),curFile.lastModified(),bpSet);
			}
		}
		RecordProcessor.getInstance().updateOpenedFile(openFileList);
		RecordProcessor.getInstance().updateBreakPointHistory();
		
		/**直到確定debugger皆停止了，就離開*/
		while(CommandCenter.debugBridge!=null){
			try{
				CommandCenter.debugBridge.getDebugger().getProcess().destroy();
				CommandCenter.debugBridge.getDebugger().getProcess().exitValue();
				break;
			}catch(IllegalThreadStateException exception){}
		}
		System.exit(0);
	}
}

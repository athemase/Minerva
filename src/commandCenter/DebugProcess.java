package commandCenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**java.lang.Process，用來將debugger的io和minerva的io做連接*/
public class DebugProcess {
	protected Process process;
	protected ProcessBuilder launcher;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	protected MessageCollector actionTrigger; 
	protected boolean isCommandFound=false;
	
	public DebugProcess(String[] command){
		this(command,null);
	}
	public DebugProcess(String[] command,File projectPath){
		actionTrigger=MessageCollector.getInstance();
		launcher = new ProcessBuilder(command);
		launcher.redirectErrorStream(true);
		if(projectPath!=null)
			launcher.directory(projectPath);
		try {
			process = launcher.start();
			process.exitValue();
		} catch (IOException e) {} 
		catch(IllegalThreadStateException exception){
			writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			actionTrigger.addCommand(this);
			isCommandFound=true;
		}
	}
	public void writeln(String str) throws IOException{
		writer.write(str);
		writer.newLine();
		writer.flush();	
	}
	public BufferedReader getReader(){
		return reader;
	}
	public Process getProcess(){
		return process;
	}
	public boolean isRunning(){
		return isCommandFound;
	}
}

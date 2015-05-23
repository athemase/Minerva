package network;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import commandCenter.CommandCenter;
import commandCenter.MinervaMessage;
import commandCenter.XdivaMessage;
import data.DebugData;

import recordProcessor.RecordProcessor;

/**主動送訊息給diva的socket，通常是送visualize unfold fold命令*/
public class MinervaClient {
	private static MinervaClient editClient;
	private BufferedWriter writer;
	private Socket socket;
	private int divaPort;
	private String divaIP;
	
	/**連到diva的minerva client，主要發送visualize unfold fold命令*/
	protected MinervaClient(){
		this.createConnection();
		try {
			/**主動連接到diva，如果成功則ouput一筆訊息*/
			socket=new Socket(this.divaIP,this.divaPort);
			if(socket.isConnected()){
				MinervaServer.initialize(socket);
				writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				//System.err.println(MinervaMessage.CONNECTE_DIVA_SUCCESS+divaIP);
				
				System.out.println("  Connected to DIVA, "+this.divaIP+", "+this.divaPort);
			}
		}catch (UnknownHostException e){
			System.err.println(MinervaMessage.DIVA_NOT_FOUND);
		}catch (IOException e){
			System.err.println(MinervaMessage.DIVA_NOT_FOUND);
		}
	}
	public static MinervaClient getInstance(){
		if(editClient==null)
			editClient=new MinervaClient();
		return editClient;
	}
	/**讀取使用者設定紀錄，決定diva在那個ip端上，由setting.ini取得記錄*/
	private void createConnection(){
		HashMap settings=RecordProcessor.getInstance().getUserSettingMap();
		String address=(String)settings.get("IPAddress");
		String portName=(String)settings.get("Port");
		/**如果無紀錄，預設使用port 2000，否則從setting.ini取得port number*/
		try{
			if(portName==null)
				this.divaPort=2000;
			else
				this.divaPort=Integer.parseInt(portName);
		}catch(NumberFormatException e){
			this.divaPort=2000;
		}
		if(address==null)
			this.divaIP="127.0.0.1";
	}
	
	/**傳送資料給diva，主要傳送visualize unfold fold命令*/
	public void sendMessage(String message){
		message+=" "+XdivaMessage.ENDLINE;
		System.out.println("SEND TO CLIENT:["+message+"]");
		try {
			if((socket!=null)&&(socket.isConnected())){
				writer.write(message);
				writer.flush();
			}
			else
				this.closeConnection();
		} catch (Exception e){
			this.closeConnection();
		}
	}
	/**當diva關閉時，關閉連線*/
	public void closeConnection(){
		try {
			if(writer!=null)
				this.writer.close();
			if(socket!=null)
				this.socket.close();
		} catch (IOException e) {}
	    editClient=null;
    }
	public static MinervaClient getClient(){
		return editClient;
	}
	
	/**回傳是否還在連線中*/
	public boolean isConnected(){
		if(socket==null)
			return false;
		else 
			return socket.isConnected();
	}
}

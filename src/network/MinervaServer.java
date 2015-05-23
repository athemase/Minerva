
package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import commandCenter.CommandCenter;
import commandCenter.MinervaMessage;
import commandCenter.XdivaMessage;
import data.DebugData;

/**和diva構通的minerva server，主要回應ask struct show scope命令*/
public class MinervaServer extends Thread{
	private static MinervaServer editServer;
	//private ServerSocket server;		/**server socket*/
	private Socket connection;			/**diva連入的connection，目前只支援連到單一diva*/
	
	private BufferedWriter output;
	private BufferedReader input;
	
	private int serverPort;				/**和diva構通的port*/
	private String message;				/**儲存收到的訊息*/
	
	/**預設和diva構通的port是2001號*/
	/*
	protected MinervaServer(){
		this(2001);
	}
	*/
	/**或者由使用者自已決定port number(可由setting.ini取得)*/
	/*
	protected MinervaServer(int port){
		serverPort=port;
		try {
			server=new ServerSocket(serverPort);
		} catch (IOException e) {}
		this.start();
	}
	*/
	
	static void initialize( Socket socket ) {
		if( editServer != null ) {
			editServer.closeConnection();
			editServer.closeServer();
			editServer = null;
		}
		editServer = new MinervaServer( socket );
	}
	
	private MinervaServer( Socket socket ) {
		this.connection = socket;
		try{
			this.input=new BufferedReader(new InputStreamReader(this.connection.getInputStream()));;
			this.output=new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
		}catch(IOException e){
		}
		this.serverPort = socket.getPort();
		this.start();
	}
	
	public static MinervaServer getInstance(){
		return editServer;
	}
	/*
	public static MinervaServer getInstance(int port){
		if(editServer==null)
			editServer=new MinervaServer(port);
		return editServer;
	}
	*/
	
	/**server thread接受訊息主迴圈*/
	public void run(){
		while(true){
//			try{
				/**接受diva過來的連線並建立I/O*/
				//connection=server.accept();
//				input=new BufferedReader(new InputStreamReader(connection.getInputStream()));;
//				output=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
//			}catch(IOException e){
//				continue;
//			}
			
			/**連線失敗，回到迴圈的頭從新接連線*/
			if((connection==null)||(!connection.isConnected()))
				continue;
			
			/**連線成功，將成功訊息貼到MessagePane*/
			message=MinervaMessage.GET_DIVA_CONNECT+connection.getInetAddress()+":"+
					serverPort;
			System.err.println(message);
			
			/**接受訊息主迴圈，直到接收到"<OVER>" 一次以讀取一列為單位，讀取完丟至CommandCenter做處理*/
//			do{
				try{
					message=input.readLine();
					System.out.println("RECV FROM SERVER:["+message+"]");
					CommandCenter.serverReceived(message);
				}catch(Exception exception){
					break;
				}
//			}while(!message.startsWith(XdivaMessage.OVER));
			/**如果接受到over，則離開收訊息回圈等待下一個連線*/
//			this.closeConnection();
		}
	}
	
	/**關閉現有的diva connection*/
	public void closeConnection(){
		try{
			if(connection!=null)
				connection.close();
			if(output!=null)
				output.close();
			if(input!=null)
				input.close();
		}catch(IOException exception){}
	}
	
	/**關閉minerva server thread*/
	public void closeServer(){
//		try {
//			if(server!=null)
//				server.close();
			editServer=null;
//		} catch (IOException e){}
	}
	
	public static MinervaServer getServer(){
		return editServer;
	}
	/**判斷是否在和diva連線中*/
	public boolean isConnected(){
		if((connection!=null)&&(connection.isConnected()))
			return true;
		else
			return false;	
	}
	/**送訊息給diva，這邊通常是回應ask show struct命令*/
	public void sendMessage(String sendMsg){
		sendMsg=sendMsg+" "+XdivaMessage.ENDLINE;
		System.out.println("SEND TO SERVER:["+sendMsg+"]");
		try{
			if((connection!=null)&&(connection.isConnected())){
				output.write(sendMsg);
				output.flush();
			}
		}catch(IOException exception){}
	}
}

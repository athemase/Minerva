import userInterface.MainFrame;
import network.MinervaServer;

public class MinervaVII {
	public MinervaVII(){
		/**啟動主要視窗*/
		MainFrame.getInstance().updateUI();
		/**啟動server thread*/
//		MinervaServer.getInstance();
	}
	public static void main(String args[]){
		new MinervaVII();
	}
}
import java.io.IOException;

import org.jfree.data.general.DefaultPieDataset;

import jpcap.JpcapCaptor;

/**
 * 
 */

/**
 * 
 *
 */
public class MyJPCaptor extends Thread {
	static JpcapCaptor jpcap = null;
	static int selectedDevice = 0;

	
	public static int getSelectedDevice() {
		return selectedDevice;
	}
	
    private static DefaultPieDataset dataset = new DefaultPieDataset();
    
	
	public static void setSelectedDevice(int selectedDevice) {
		System.out.println("Selected Device : " + selectedDevice);
		MyJPCaptor.selectedDevice = selectedDevice;
	}
	
	public static void startCapture(){
		try {
			jpcap = JpcapCaptor.openDevice(JpcapCaptor.getDeviceList()[selectedDevice],1000,false,20);
			MyJPCaptor captor = new MyJPCaptor();
			captor.start();
			EntryClass.setLogo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void stopCapture(){
		jpcap.breakLoop();
		EntryClass.setChart(ChartMaker.getPieChart());
	}
	
	public static void restartCaptureWithNewDevice(int selectedDevice,boolean started){
		CapturedPacket.resetDATA();
		if(started){
			jpcap.breakLoop();
			jpcap.close();
		}
		setSelectedDevice(selectedDevice);
		if(started){
			startCapture();
		}
	}
	@Override
	public void run() {
		jpcap.loopPacket(-1,new EntryClass());		
	}
}

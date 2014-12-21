package swdev.rfidreader;

import jssc.SerialPort;
import jssc.SerialPortException;

public class RFIDListener extends Thread {

	private String rfid = null;
	public String getRfid() {
		return rfid;
	}


	public void setRfid(String rfid) {
		this.rfid = rfid;
	}


	//public boolean ReadIt = false;
	public void run() {
		
			try {
				SerialPort serialPort = new SerialPort("COM3");
				serialPort.openPort();// Open serial port
				serialPort.setParams(9600, 8, 1, 0);// Set params.

				byte[] buffer = serialPort.readBytes(16);// Read 10 bytes from
															// serial port
				String s = new String(buffer);
				//System.out.println(s.substring(1, 12));
				rfid=s.substring(1, 12);
				serialPort.closePort();// Close serial port
			}

			catch (SerialPortException ex) {
				System.out.println(ex);
			}
		}
	

}
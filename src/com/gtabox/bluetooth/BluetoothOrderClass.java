package com.gtabox.bluetooth;

public class BluetoothOrderClass {
	private String order = ""; 
	static final String OK_ORDER = "OK\r\n";
	static final String TEST_ORDER = "AT\r\n";   //测试命令
	static final String SCAN_ORDER = "AT+INQ\r\n"; //扫描周围设备命令
	static final String SCAN_START_ORDER = "+INQS\r\n";//扫描后接收到的起始命令，再往后会接收到设备名
	static final String SCAN_END_ORDER = "+INQE\r\n";//扫描后接收完成结束命令
	static String CONNECT_DEVICE_ORDER = "AT+CONN";//连接设备命令
	static String CONNECTING_ORDER = "+CONNECTING>>";//正在连接中
	static String CONNECTED_ORDER = "+CONNECTED>>";//已连接
	static final String AT_RESET_ORDER = "AT+RESET\r\n";//重启设备
	public String[] ReceiveBuffer/* = new String[50]*/;
	public BluetoothOrderClass() {
		// TODO Auto-generated constructor stub
		ReceiveBuffer = new String[50];
		for(int x = 0; x < 50; x++){
			ReceiveBuffer[x] = "";
		}
	}
	
	public void clearReceiveBuffer(){
		for(int x = 0; x < 50; x++){
			ReceiveBuffer[x] = "";
		}
	}
	
	public void clearOrder(){
		order = "";
	}
	
	public void setOrder(String str){
		order = str;
	}
	
	public String getOrder(){
		return order;
	}

}

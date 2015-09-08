package com.gtafe.until;

import java.util.Arrays;

public class Command {
	byte[] buffer;
	SuportMethod sup = new SuportMethod();

	public static final byte coordinatorAddr = 0x00;
	public static final byte alcoholSensorAddr = 0x01;
	public static final byte tempHumiSensorAddr = 0x02;
	public static final byte illuminationSensorAddr = 0x03;
	public static final byte gasSensorAddr = 0x04;
	public static final byte hallSensorAddr = 0x05;
	public static final byte raindropSensorAddr = 0x06;
	public static final byte megnetoresistiveSensorAddr = 0x07;
	public static final byte vibrationSensorAddr = 0x08;
	public static final byte infraredSensorAddr = 0x09;
	public static final byte ctrlNodeAddr = 0x0A;
	public static final byte ultrasonicSensorAddr = 0x11;
	public static final byte ds18b20SensorAddr = 0x12;
	public static final byte pressSensorAddr = 0x14;
	public static final byte accelerationSensorAddr = 0x15;
	public static final byte atmosphericSensorAddr = 0x16;
	public static final byte waterFlowSensorAddr = 0x00;
	public static final byte getSensorAddr(final int num){
		byte[] sensorAddr = new byte[50];
		sensorAddr[0] = alcoholSensorAddr;
		sensorAddr[1] = tempHumiSensorAddr;
		sensorAddr[2] = illuminationSensorAddr;
		sensorAddr[3] = gasSensorAddr;
		sensorAddr[4] = hallSensorAddr;
		sensorAddr[5] = raindropSensorAddr;
		sensorAddr[6] = megnetoresistiveSensorAddr;
		sensorAddr[7] = vibrationSensorAddr;
		sensorAddr[8] = infraredSensorAddr;
		sensorAddr[9] = ctrlNodeAddr;
		sensorAddr[10] = ultrasonicSensorAddr;
		sensorAddr[11] = ds18b20SensorAddr;
		sensorAddr[12] = pressSensorAddr;
		sensorAddr[13] = accelerationSensorAddr;
		sensorAddr[14] = atmosphericSensorAddr;
		if(num > 14 || num <0 )
			return sensorAddr[8];
		return sensorAddr[num];
	}
	
	/**
	 * 模块类传感器、工业类传感器、控制模块类
	 * 根据类别可以确定传感器属于哪种类型
	 * */
	public static final byte MODULE_TYPE = 0x01;
	public static final byte INDUSTRY_TYPE = 0x02;
	public static final byte CONTROL_TYPE = 0x03;
	
	//温湿度2
	public byte[] getTemprature(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x02;  //2
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x45;
		buffer[9] = (byte) 0xF9;
		return buffer;	
	}
	//水滴6
	public byte[] getWaterDrop(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x06;  //6
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x44;
		buffer[9] = (byte) 0x7D;
		return buffer;	
	}
	//光照3
	public byte[] getLight(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x03;  //3
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x44;
		buffer[9] = (byte)0x28;
		return buffer;	
	}
	//震动8
	public byte[] getVibration(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x08;  //8
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x45;
		buffer[9] = (byte)0x53;
		return buffer;	
	}
	//红外9
	public byte[] getInfraRed(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x09;  //9
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x44;
		buffer[9] = (byte) 0x82;
		return buffer;	
	}
	
	//烟雾4
	public byte[] getGas(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x04;  //4
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x45;
		buffer[9] = (byte) 0x9f;
		return buffer;	
	}
	//酒精1
	public byte[] getAlcohol(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x01;  //1
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x45;
		buffer[9] = (byte) 0xCA;
		return buffer;	
	}
	
	//霍尔5
	public byte[] getHall(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x05;  //5
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x44;
		buffer[9] = (byte) 0x4E;
		return buffer;	
	}
	
	//倾角7
	public byte[] getDip_Angle(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x07;  //7
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x45;
		buffer[9] = (byte) 0xAC;
		return buffer;	
	}
	
	//大气压
	public byte[] getAtmospheric_Pressure(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x16; //22
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x46;
		buffer[9] = (byte) 0xED;
		return buffer;	
	}
	
	//超声波
	public byte[] getUltrasonic(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x11; //17
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x47;
		buffer[9] = (byte)0x5A;
		return buffer;	
	}
	
	//单独温度传感器DS18B20
	public byte[] getDS18b20(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x12; //18
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x47;
		buffer[9] = (byte)0x69;
		return buffer;	
	}
	
	//压力
	public byte[] getPressure(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x14; //20
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x47;
		buffer[9] = (byte)0x0F;
		return buffer;	
	}
	
	//三轴加速
	public byte[] getAxis(){
		buffer = new byte[10];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x15;  //21
		buffer[3] = (byte)0x03;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x00;
		buffer[8] = (byte)0x46;
		buffer[9] = (byte)0xDE;
		return buffer;	
	}
	
	//步进电机控制
	public byte[] MotorControl(String circle_num,String direction){
		byte[] circle = sup.hexStringToByteArray(circle_num);
		byte[] direct = sup.hexStringToByteArray(direction);
		buffer = new byte[14];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x0A;  //10
		buffer[3] = (byte)0x10;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x01;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x03;
		buffer[8] = (byte)0x01;
		buffer[9] = direct[0];
		buffer[10] = circle[0];
		buffer[11] = circle[1];
		byte[] check = new byte[2];
		sup.get_crc16(Arrays.copyOfRange(buffer, 2, 12), 10,check);
				
		buffer[12] = check[0];
		buffer[13] = check[1];
		return buffer;
	}
	public byte[] NumShow(String show_num){
		byte[] num = sup.hexStringToByteArray(show_num);
		buffer = new byte[14];
		buffer[0] = (byte)0x7E;
		buffer[1] = (byte)0x7E;
		buffer[2] = (byte)0x0A;  //10
		buffer[3] = (byte)0x10;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x03;
		buffer[6] = (byte)0x00;
		buffer[7] = (byte)0x01;
		buffer[8] = (byte)0x01;
		buffer[9] = num[0];
		byte[] check = new byte[2];
		sup.get_crc16(Arrays.copyOfRange(buffer, 2, 10), 8,check);
		buffer[10] = check[0];
		buffer[11] = check[1];
		return buffer;
	}
	
	//自动上传协调器以及其他节点信息14
	public byte[] getAutoUploadingNodeInfoCmd(){
		//7E 7E 00 14 01 BF 00
		byte[] buffer = new byte[7];
		buffer[0] = (byte)0x7e;
		buffer[1] = (byte)0x7e;
		buffer[2] = (byte)0x00;
		buffer[3] = (byte)0x14;
		buffer[4] = (byte)0x01;
		byte[] crc = new byte[2];
		sup.get_crc16(Arrays.copyOfRange(buffer, 2, 5), 3, crc);
		buffer[5] = crc[0];
		buffer[6] = crc[1];
		return buffer;
	}
	
	//获取协调器节点信息13
	public byte[] getCoordinatorNodeInfoCmd(){
		//7E 7E 00 13 40 7D
		byte[] buffer = new byte[6];
		buffer[0] = (byte)0x7e;
		buffer[1] = (byte)0x7e;
		buffer[2] = (byte)0x00;
		buffer[3] = (byte)0x13;
		byte[] crc = new byte[2];
		sup.get_crc16(Arrays.copyOfRange(buffer, 2, 4), 2, crc);
		buffer[4] = crc[0];
		buffer[5] = crc[1];
		return buffer;
	}
	
	//指定节点地址获取信息13
	public byte[] getAssignNodeInfoCmd(byte addr){
		//7E 7E (xx) 13 (xx) (xx)
		byte[] buffer = new byte[6];
		buffer[0] = 0x7e;
		buffer[1] = 0x7e;
		buffer[2] = addr;
		buffer[3] = 0x13;
		byte[] crc = new byte[2];
		sup.get_crc16(Arrays.copyOfRange(buffer, 2, 4), 2, crc);
		buffer[4] = crc[0];
		buffer[5] = crc[1];
		return buffer;
	}
}

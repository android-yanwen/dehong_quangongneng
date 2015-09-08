/**
 * @author 鄢文
 * @brief 根据ModeBus通信协议解析串口上传而来的数据，包括地址、类型、数据等信息
 * @param str 传入的字符串数据*/
package com.gtafe.until;

import com.gtafe.taiyuan_sensor.R;

public class ModeBusStrParse {
	private String str;
	public static final String MODEBUS_DATA_HEAD = "7E";
	public static final String COORDINATOR_NODE = "0000"; //协调器节点
	public static final String TEMP_HUMI_SENSOR = "0201";//温湿度传感器
	public static final String ILLUMINATE_SENSOR = "0202";//光照传感器
	public static final String SMOK_SENSOR = "0203";//烟雾传感器
	public static final String PERSON_SENSOR = "0204";//人体感应传感器
	public static final String THREE_ACCELERATED_SPEED_SENSOR = "0205";//三轴加速度传感器
	public static final String VIBRATION_SENSOR = "0206";//振动传感器
	public static final String INFRARED_SENSOR = "0207";//红外传感器
	public static final String VOICE_SENSOR = "0208";//声响传感器
	public static final String FIRE_SENSOR = "0209";//火焰传感器
	public static final String ULTRASONIC_SENSOR = "020A";//超声波传感器
	public static final String PRESSURE_SENSOR = "020B";//大气压传感器
	public static final String ULTRAVIOLET_RAYS_SENSOR = "020C";//紫外线检测传感器
	public static final String INFRARED_ECHO_SENSOR = "020D";//红外反射传感器
	public static final String INFRARED_CORRELATION_SENSOR = "020E";//红外对射传感器
	public static final String TEMP_18B20_SENSOR = "020F";//18B20
	public static final String HOLL_SENSOR = "0210";//霍尔传感器
	public static final String RAIN_SENSOR = "0211";//雨滴传感器
	public static final String ALCOHOL_SENSOR = "0212";//酒精传感器
			
	public ModeBusStrParse(String string) {
		// TODO Auto-generated constructor stub
//		str = new String();
		str = string;
	}
	/**
	 * @author YANWEN
	 * @name getSensorImg
	 * @brief 根据传感器名字获得对应的传感器图片
	 * @param sensorTy 传感器类型
	 * @return img 图片
	 * */
	public static final int getSensorImg(String sensorTy){
		int img = 0;
		if(sensorTy.equals(TEMP_HUMI_SENSOR) == true){
			img = R.drawable.temp_humi_img80_80;
		}
		else if(sensorTy.equals(ILLUMINATE_SENSOR)){
			img = R.drawable.illuminate_img80_80;
		}
		else if(sensorTy.equals(SMOK_SENSOR)){
			img = R.drawable.smok_sensor_img;
		}
		else if(sensorTy.equals(PERSON_SENSOR)){
			img = R.drawable.pressure_sensor_img;
		}
		else if(sensorTy.equals(THREE_ACCELERATED_SPEED_SENSOR)){
			img = R.drawable.acceleration_sensor_img;
		}
		else if(sensorTy.equals(VIBRATION_SENSOR)){
			img = R.drawable.vibration_sensor_img;
		}
		else if(sensorTy.equals(INFRARED_SENSOR)){
			img = R.drawable.infrared_sensor_img;
		}
		else if(sensorTy.equals(VOICE_SENSOR)){
			
		}
		else if(sensorTy.equals(FIRE_SENSOR)){

		}
		else if(sensorTy.equals(ULTRASONIC_SENSOR)){
			img = R.drawable.ultrasonic_sensor_img;
		}
		else if(sensorTy.equals(PRESSURE_SENSOR)){
			img = R.drawable.pressure_sensor_img;
		}
		else if(sensorTy.equals(ULTRAVIOLET_RAYS_SENSOR)){
			
		}
		else if(sensorTy.equals(INFRARED_ECHO_SENSOR)){

		}
		else if(sensorTy.equals(INFRARED_CORRELATION_SENSOR)){

		}
		else if(sensorTy.equals(TEMP_18B20_SENSOR)){
			img = R.drawable.ds18b20_sensor_img;
		}
		else if(sensorTy.equals(HOLL_SENSOR)){
			img = R.drawable.hall_sensor_img;
		}
		else if(sensorTy.equals(RAIN_SENSOR)){
			img = R.drawable.rain_sensor_img;
		}
		else if(sensorTy.equals(ALCOHOL_SENSOR)){
			img = R.drawable.alcohol_img80_80;
		}
		return img;
	}
	public static final String getSersorName(String sensorNm){
		String name = null;
		if(sensorNm.equals(TEMP_HUMI_SENSOR) == true){
			name = "温湿度传感器";
		}
		else if(sensorNm.equals(ILLUMINATE_SENSOR)){
			name = "光照传感器";
		}
		else if(sensorNm.equals(SMOK_SENSOR)){
			name = "烟雾传感器";
		}
		else if(sensorNm.equals(PERSON_SENSOR)){
			name = "人体感应传感器";
		}
		else if(sensorNm.equals(THREE_ACCELERATED_SPEED_SENSOR)){
			name = "三轴加速度传感器";
		}
		else if(sensorNm.equals(VIBRATION_SENSOR)){
			name = "振动传感器";
		}
		else if(sensorNm.equals(INFRARED_SENSOR)){
			name = "红外传感器";
		}
		else if(sensorNm.equals(VOICE_SENSOR)){
			name = "声音传感器";
		}
		else if(sensorNm.equals(FIRE_SENSOR)){
			name = "火焰传感器";
		}
		else if(sensorNm.equals(ULTRASONIC_SENSOR)){
			name = "超声波传感器";
		}
		else if(sensorNm.equals(PRESSURE_SENSOR)){
			name = "大气压传感器";
		}
		else if(sensorNm.equals(ULTRAVIOLET_RAYS_SENSOR)){
			name = "紫外线检测传感器";
		}
		else if(sensorNm.equals(INFRARED_ECHO_SENSOR)){
			name = "红外反射传感器";
		}
		else if(sensorNm.equals(INFRARED_CORRELATION_SENSOR)){
			name = "红外对射传感器";
		}
		else if(sensorNm.equals(TEMP_18B20_SENSOR) == true){
			name = "DS18B20温度传感器";
		}
		else if(sensorNm.equals(HOLL_SENSOR)){
			name = "霍尔传感器";
		}
		else if(sensorNm.equals(RAIN_SENSOR)){
			name = "雨滴传感器";
		}
		else if(sensorNm.equals(ALCOHOL_SENSOR)){
			name = "酒精传感器";
		}
		else{
			name = "协调器节点";
		}
		return name;
	}
	public String getSensorCmd(){
		String cmd;
		cmd = str.substring(2, 4);
		return cmd;
	}
	/**
	 * @author yanwen
	 * @name getSensorCategory
	 * @brief 获得传感器的类别：是工业类还是模块类或者其他类
	 * @param null
	 * @return null
	 * */
	public String getSensorCategory(){
		String category = null;
		category = str.substring(4, 6);
		return category;
	}
	public String getSensorAddr(){
		String addrL = null, addrH, addr;
		addrL = str.substring(6, 8);
		addrH = str.substring(8, 10);
		addr = addrH + addrL;
		return addr;
	}
	/**
	 * @author yanwen
	 * @name getSensorType
	 * @brief 获得传感器的类型，此函数和getSensorCategory()（在前）函数共同确定传感器
	 * @param null
	 * @return null
	 * */
	public String getSensorType(){
		String type;
		type = str.substring(10, 12);
		return type;
	}
	/**
	 * @author yanwen
	 * @name getSensorTypes
	 * @brief 获得传感器的类型getSensorCategory() + getSensorType();
	 * @param null
	 * @return types
	 * */
	public String getSensorTypes(){
		String types;
		types = getSensorCategory() + getSensorType();
		return types;
	}
	public String getSensorDataLength(){
		String length;
		length = str.substring(12, 14);
		return length;
	}
	/**
	 * @author yanwen
	 * @name getSensorData
	 * @brief 获得读出来的Hex数据，此时还没转化为实际的传感器数据需要调用getSensorIntData函数进行转化
	 * @param length 获得的hex数据长度
	 * @return data 返回获得的Hex数据
	 * */
	public String getSensorData(String length){
		String data;
		data = str.substring(14, 14+(Integer.parseInt(length, 16))*2);
//		Log.d("GTA", "data..............:" + data);
		return data;
	}
	/**
	 * @author yanwen
	 * @name getSensorIntData
	 * @brief 将传感器的hex数据转化为实际的传感器数据
	 * @param sensorTys 传感器名字类型 
	 *        hexData 传感器的Hex数据
	 * @return intData 传感器的数据十进制数据
	 * */
	public String getSensorIntData(String sensorTys, String hexData){
		String intData = null;
		if(sensorTys.equals(TEMP_HUMI_SENSOR)){
			String hDataL = hexData.substring(0, 2);
			String hDataH = hexData.substring(2, 4);
			String tDataL = hexData.substring(4, 6);
			String tDataH = hexData.substring(6, 8);
		    intData = "湿度：" + Integer.valueOf(hDataH, 16).toString()/* + "." */+ Integer.valueOf(hDataL, 16) + "％" +
		    		"\n温度：" + Integer.valueOf(tDataH, 16).toString() /*+ "." */+ Integer.valueOf(tDataL, 16) + "℃";
		}
		else if(sensorTys.equals(ILLUMINATE_SENSOR)){
			// 判断数据是否是开关量
			if(hexData.length() == 2){
				if(hexData.equals("00"))
				{
					intData = "光照强" + hexData;
				}
				else if(hexData.equals("01")){
					intData = "光照弱" + hexData;
				}
			} else{
				String illumilatorData = hexData.substring(2, 4) + hexData.substring(0, 2);
				long value = Integer.parseInt(illumilatorData, 16) * 2;
				intData = "光照大小：" + String.valueOf(value) + "lux";
			}
		}
		else if(sensorTys.equals(SMOK_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无烟雾" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有烟雾" + hexData;				
			}
		}
		else if(sensorTys.equals(PERSON_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无人" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有人" + hexData;				
			}
		}
		else if(sensorTys.equals(THREE_ACCELERATED_SPEED_SENSOR)){
			String dataX = hexData.substring(0, 1);
			String dataY = hexData.substring(2, 3);
			String dataZ = hexData.substring(4, 5);
			intData = "X:" + Integer.valueOf(dataX, 16).toString() + 
					"\nY:" + Integer.valueOf(dataY, 16) + 
					"\nZ:" + Integer.valueOf(dataZ, 16);
		}
		else if(sensorTys.equals(VIBRATION_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无振动" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有振动" + hexData;				
			}
		}
		else if(sensorTys.equals(INFRARED_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无遮挡" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有遮挡" + hexData;				
			}
		}
		else if(sensorTys.equals(VOICE_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无声音" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有声音" + hexData;				
			}
		}
		else if(sensorTys.equals(FIRE_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无火焰" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有火焰" + hexData;				
			}
		}
		else if(sensorTys.equals(ULTRASONIC_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无遮挡" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有遮挡" + hexData;				
			}
		}
		else if(sensorTys.equals(PRESSURE_SENSOR)){
			String dataH = hexData.substring(2, 4);
			String dataL = hexData.substring(0, 2);
			String data = dataH + dataL;
			int temp = Integer.parseInt(data, 16);
			intData = "压力大小：" + String.valueOf(temp);
		}
		else if(sensorTys.equals(ULTRAVIOLET_RAYS_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无紫外线" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有紫外线" + hexData;				
			}
		}
		else if(sensorTys.equals(INFRARED_ECHO_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无回应" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有回应" + hexData;				
			}
		}
		else if(sensorTys.equals(INFRARED_CORRELATION_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无对射" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有对射" + hexData;
			}
		}
		else if(sensorTys.equals(TEMP_18B20_SENSOR)){
			String dataL = hexData.substring(2, 4);
			String dataH = hexData.substring(0, 2);
//			Log.d("GTA", "..........dataL:" + dataL);
//			Log.d("GTA", "..........dataH:" + dataH);
			if(dataL.substring(0, 1).equals("0")){
				dataL = dataL.substring(1);
			}
			intData = "当前温度：" + dataH + "." + dataL + "℃" /*Integer.valueOf(dataH, 16).toString() + "." + Integer.valueOf(dataL, 16)*/;
			
		}
		else if(sensorTys.equals(HOLL_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无磁铁" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有磁铁" + hexData;
			}
		}
		else if(sensorTys.equals(RAIN_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无雨滴" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有雨滴" + hexData;
			}
		}
		else if(sensorTys.equals(ALCOHOL_SENSOR)){
			if(hexData.equals("00"))
			{
				intData = "无酒精" + hexData;
			}
			else if(hexData.equals("01")){
				intData = "有酒精" + hexData;
			}
		}
		return intData;
	}
}

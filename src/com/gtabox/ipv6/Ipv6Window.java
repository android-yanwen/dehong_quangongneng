package com.gtabox.ipv6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gtafe.taiyuan_sensor.MainActivity;
import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.taiyuan_sensor.WebServiceThread;
import com.gtafe.until.ModeBusStrParse;
import com.gtafe.until.SuportMethod;

public class Ipv6Window extends SerialPortActivity{
	private ListView ipv6_lv;
//	private String[] sens_iv;
	int[] sens_iv;
	private String[] sens_name;
	private String[] sens_data;
	private String[] sens_addr;
	private SuportMethod sup;
	private MyHandler myHandler;
	private Map<String, Map<String, Object>> sensorMap;
	private String[] keyNames = new String[50];

	private List<Map<String, Object>> listItem = new ArrayList<Map<String,Object>>();
	private SimpleAdapter adp;
	public Ipv6Window() {
		// TODO Auto-generated constructor stub
//		sens_iv = new String[]{"图片1", "图片2", "图片3", "图片4", "图片5"};
/*		sens_iv = new int[]{R.drawable.temp_humi_img80_80, R.drawable.illuminate_img80_80, R.drawable.alcohol_img80_80};
		sens_name = new String[]{"温度传感器", "雨滴传感器", "光照传感器"};
		sens_data = new String[]{"25℃","有雨滴","1350"};
		sens_addr = new String[]{"1101:A33A:540A", "7728:CA89:0009", "7729:AC87:8878"};*/
		sup = new SuportMethod();
		sensorMap = new HashMap<String, Map<String, Object>>();
		for(int x = 0; x < keyNames.length; x++){
			keyNames[x] = "";
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipv6_window_xml);
		myHandler = new MyHandler();
		
/*		List<Map<String, Object>> listItem = new ArrayList<Map<String,Object>>();
		for(int x = 0; x < sens_addr.length; ++x){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", sens_iv[x]);
			map.put("name", sens_name[x]);
			map.put("data", sens_data[x]);
//			map.put("addr", sens_addr[x]);
			listItem.add(map);
		}
		SimpleAdapter simpAdp = new SimpleAdapter(getApplicationContext(), listItem, R.layout.ipv6_sensor_window_xml,
				new String[]{"image","name","data"},
				new int[]{R.id.sens_img_tv,R.id.sens_name_tv,R.id.sens_data_tv});*/
		ipv6_lv = (ListView)findViewById(R.id.ipv6_lv);
/*		ipv6_lv.setAdapter(simpAdp);*/
		adp = new SimpleAdapter(getApplicationContext(), 
				listItem, 
				R.layout.ipv6_sensor_window_xml,
				new String[]{"SensorImage", "SensorName", "SensorData"}, 
				new int[]{R.id.sens_img_tv, R.id.sens_name_tv, R.id.sens_data_tv});
		ipv6_lv.setAdapter(adp);
	}
	
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		if(size < 10 || buffer[0] != 0x7e)return;
		byte[] buf = Arrays.copyOf(buffer, size);
		String bufStr = sup.byteToHexString(buf).toUpperCase();
		Log.d("GTA", "..........bufStr:"+bufStr);
		if(bufStr.isEmpty() == true) return;
		ModeBusStrParse modebus = new ModeBusStrParse(bufStr);
		Map<String, Object> map = new HashMap<String, Object>();
		String sensorType = modebus.getSensorCategory() + modebus.getSensorType();
		Log.d("GTA", "..........."+ sensorType);
		map.put("SensorImage", ModeBusStrParse.getSensorImg(sensorType));
		map.put("SensorName", ModeBusStrParse.getSersorName(sensorType));
		Log.d("GTA", "..........."+ ModeBusStrParse.getSersorName(sensorType));
		String sensorAddr = modebus.getSensorAddr();//获得地址
		String sensorHexData = modebus.getSensorData(modebus.getSensorDataLength());
		String sensorData = modebus.getSensorIntData(sensorType, sensorHexData);
		map.put("SensorData", sensorData);
		sensorMap.put(sensorType, map);

		//判断如果是温湿度传感器，则传两次，将温度和湿度分开传
		if(sensorType == ModeBusStrParse.TEMP_HUMI_SENSOR){
			String tempData = sensorHexData.substring(6, 8) + sensorHexData.substring(4, 6);
			new WebServiceThread(MainActivity.WEBSERVICE, 
					Integer.parseInt(sensorAddr, 16), 
					Integer.parseInt(modebus.getSensorType(), 16), 
					MainActivity.SYSTEM_ID, 
					tempData).start();
			String humiData = sensorHexData.substring(2, 4) + sensorHexData.substring(0, 2);
			new WebServiceThread(MainActivity.WEBSERVICE, 
					0xFE, //湿度
					Integer.parseInt(modebus.getSensorType(), 16), 
					MainActivity.SYSTEM_ID, 
					humiData).start();
		}
		else {
			//将获得的数据上传至云服务器
			new WebServiceThread(MainActivity.WEBSERVICE, 
					Integer.parseInt(sensorAddr, 16), 
					Integer.parseInt(modebus.getSensorType(), 16), 
					MainActivity.SYSTEM_ID, 
					sensorHexData).start();
		}
		
		//储存传感器名字键值
		for(int x = 0;;x++){
			if(keyNames[x].isEmpty() || keyNames[x].equals(sensorType)){
				keyNames[x] = sensorType;
				break;
			}
		}
		Message msg = myHandler.obtainMessage();
		msg.what = 1;
		msg.obj = sensorMap;
		myHandler.sendMessage(msg);
	}
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				int[] sensorImg = new int[sensorMap.size()];
				String[] sensorName = new String[sensorMap.size()];
				String[] sensorData = new String[sensorMap.size()];
				for(int x = 0; x < sensorMap.size(); x++){
					Map<String, Object> sensor = sensorMap.get(keyNames[x]);
					sensorImg[x] = (Integer) sensor.get("SensorImage");
					sensorName[x] = (String) sensor.get("SensorName");
					sensorData[x] = (String) sensor.get("SensorData");
				}
				//定义SimpleAdapter将数据添加到ListView视图中
				listItem.clear();
				for(int x = 0; x < sensorMap.size(); x++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("SensorImage", sensorImg[x]);
					map.put("SensorName", sensorName[x]);
					map.put("SensorData", sensorData[x]);
					listItem.add(map);
				}
				adp.notifyDataSetChanged();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}

/**
 * @author 鄢文
 * @name ZibeeDataWindow.java
 * @brief 通过串口获取传感器的数据，并显示传感器的数据和zigbee节点的一些详细信息
 * @data 2015/05/07*/
package com.gtabox.zigbee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
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

public class ZigbeeDataWindow extends SerialPortActivity{
	private AlertDialog.Builder dialog = null;
	private SuportMethod sup;
/*	private String[] sensor_name = new String[]
			{"温湿度传感器", "光照传感器", "红外传感器"};
	private String[] zigbee_addr = new String[]
			{"短地址", "短地址", "短地址"};*/
	private String[] names = new String[50];
	private Map<String, Map<String, Object>> sensorMap = new HashMap<String, Map<String, Object>>();
	private MyHandler myhandler;
	
	public ListView lv;
	private List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adpt;
	public ZigbeeDataWindow() {
		// TODO Auto-generated constructor stub
		sup = new SuportMethod();
		for(int x = 0; x < 50; ++x){
			names[x] = "";
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zigbee_data_window_xml);
		myhandler = new MyHandler();
		lv = (ListView)findViewById(R.id.my_list);
/*		List<Map<String, Object>> listItem = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < sensor_name.length; ++i){
			Map<String, Object> mapItem = new HashMap<String, Object>();
			mapItem.put("Name", sensor_name[i]);
			mapItem.put("Addr", zigbee_addr[i]);
			listItem.add(mapItem);
		}
		//创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItem, R.layout.sensor_data_xml, 
														new String[]{"Name", "Addr"},
														new int[]{R.id.sensor_type_tv, R.id.sensor_addr_tv});
		lv = (ListView)findViewById(R.id.my_list);
		lv.setAdapter(simpleAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// TODO Auto-generated method stub
//				Log.d("GTA", arg0.toString());
//				Log.d("GTA", view.toString());
//				Log.d("GTA", Integer.toString(position));
//				Log.d("GTA", Integer.toString((int)id));
				String sensor_type = sensor_name[position];
				if(sensor_type.equals(sensor_name[0]) == true){  //判断是否是温湿度传感器
					Intent temp_humi_intent = new Intent(ZigbeeDataWindow.this, ZigbeeTempHumiSensor.class);
					startActivity(temp_humi_intent);
				}
				else if(sensor_type.equals(sensor_name[1]) == true){ //判断是否是光照传感器
					Intent illuminate_intent = new Intent(ZigbeeDataWindow.this, ZigbeeIlluminateSensor.class);
					startActivity(illuminate_intent);
				}
				else if(sensor_type.equals(sensor_name[2]) == true){  //判断是否是红外传感器
					Intent infrared_intent = new Intent(ZigbeeDataWindow.this, ZigbeeInfraredSensor.class);
					startActivity(infrared_intent);
				}
			}
		});*/
		adpt = new SimpleAdapter(getApplicationContext(), 
				listItem, 
				R.layout.sensor_data_xml,
				new String[]{"Image", "Name","Addr","Data"},
				new int[]{R.id.sensor_img_iv,R.id.sensor_type_tv,R.id.sensor_addr_data_tv,R.id.sensor_data_tv});
		ZigbeeDataWindow.this.lv.setAdapter(adpt);  //显示适配器
	}
	
	
	/**
	 * @author Administrator
	 * @brief 抽象函数，接收串口发来的数据包
	 * @param buffer 接收的缓存区
	 *        size   接收的大小
	 * @return void*/
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
//		Log.d("TAG", "size:" + size);
		if(size < 10 || buffer[0] != 0x7e) {
			if(dialog == null){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog = new AlertDialog.Builder(getApplicationContext());
						dialog.setTitle("警告！");
						dialog.setMessage("接收数据包错误，请检查串口！！！");
						dialog.setPositiveButton("Ok", null);
						dialog.show();
					}
				});
			}
			return;  //接收的数据包不完整则返回丢弃此次数据
		} else{
			byte[] buf = Arrays.copyOf(buffer, size);
			String bufferStr = sup.byteToHexString(buf);
			Log.d("GTA", "bufferStr:" + bufferStr);
			ModeBusStrParse modebus = new ModeBusStrParse(bufferStr);
			String sensorType = modebus.getSensorCategory()+modebus.getSensorType();//获取传感器类型
			int sensorImg = ModeBusStrParse.getSensorImg(sensorType);//获得传感器图片
			String sensorName = ModeBusStrParse.getSersorName(sensorType);//获得传感器名称
			String sensorAddr = modebus.getSensorAddr();//获得地址
			String sensorHexData = modebus.getSensorData(modebus.getSensorDataLength());//获得十六进制数据
			String sensorData = modebus.getSensorIntData(sensorType, sensorHexData);//获得传感器实际数据
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SensorImage", sensorImg);
			map.put("SensorName", sensorName);
			map.put("SensorAddr", sensorAddr);
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
			
			//为方便后续找到对应传感器需要将键值进行保存
			for(int x = 0; ; ++x){
				if(names[x].isEmpty() || names[x].equals(sensorType)){
					names[x] = sensorType;
					break;
				}
			}
			
			Message msg = myhandler.obtainMessage();
			myhandler.sendEmptyMessage(1);
		}
	}
	
	
	/**
	 * @author yanwen
	 * @brief 重新handleMessage函数，接收线程数据
	 * */
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			Log.d("GTA", "Handler");
			int[] sensorImg = new int[20];
			String[] sensorName = new String[20];
			String[] sensorAddr = new String[20];
			String[] sensorData = new String[20];
			for(int x = 0; x < sensorMap.size(); ++x){
				Map<String, Object> map = sensorMap.get(names[x]);
				sensorImg[x] = (Integer) map.get("SensorImage");
				sensorName[x] = (String) map.get("SensorName");
				sensorAddr[x] = (String) map.get("SensorAddr");
				sensorData[x] = (String) map.get("SensorData");
			}
			//定义SimpleAdapter进行数据显示
			listItem.clear();
			for(int x = 0; x < sensorMap.size(); ++x){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Image", sensorImg[x]);
				map.put("Name", sensorName[x]);
				map.put("Addr", sensorAddr[x]);
				map.put("Data", sensorData[x]);
				listItem.add(map);
			}
			adpt.notifyDataSetChanged();//更新ListView中的数据
			super.handleMessage(msg);
		}
	}

	/**
	 * onPause() Activity暂停时会调用此函数
	 */
	protected void onPause() {
		super.onPause();
		onDestroy();
	};
	/**
	 * Activity销毁时会调用此函数
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

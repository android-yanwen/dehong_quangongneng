package com.gtabox.zigbee;

import java.io.IOException;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.until.ModeBusStrParse;
import com.gtafe.until.SuportMethod;

public class ZigbeeDetailDataWindow extends SerialPortActivity{
	private String SensorType;
	private Handler handler;
	private ImageView sensor_icon_iv;
	private TextView sensor_name_tv, zigbee_addr_tv, zigbee_data_tv;
	/*
	public ZigbeeDetailDataWindow(String type) {
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zig_topo_detail_data_xml);
		sensor_icon_iv = (ImageView)findViewById(R.id.sensor_icon_iv);
		sensor_name_tv = (TextView)findViewById(R.id.sensor_name_tv);
		zigbee_addr_tv = (TextView)findViewById(R.id.zigbee_addr_tv);
		zigbee_data_tv = (TextView)findViewById(R.id.zigbee_data_tv);
		Intent intent = getIntent();
		SensorType = intent.getStringExtra("SensorType");
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				sensor_icon_iv.setImageResource(data.getInt("SensorImg"));
				sensor_name_tv.setText(data.getString("SensorName"));
				zigbee_addr_tv.setText(data.getString("SensorAddr"));
				zigbee_data_tv.setText(data.getString("SensorData"));
			}
		};
	}

	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		byte[] buf = Arrays.copyOf(buffer, size);
		SuportMethod sup = new SuportMethod();
		String bufferStr = sup.byteToHexString(buf);
		Log.d("GTA", "bufferStr:" + bufferStr);
		Log.d("GTA", "sensorType:" + SensorType);
		if(size < 10 || buffer[0] != 0x7e){
			return;
		}
		ModeBusStrParse modebus = new ModeBusStrParse(bufferStr);
		String sensorType = modebus.getSensorCategory()+modebus.getSensorType();//获取传感器类型
		if(SensorType.equals(sensorType)){
			int sensorImg = ModeBusStrParse.getSensorImg(sensorType);//获得传感器图片
			String sensorName = ModeBusStrParse.getSersorName(sensorType);//获得传感器名称
			String sensorAddr = modebus.getSensorAddr();//获得地址
			String sensorHexData = modebus.getSensorData(modebus.getSensorDataLength());//获得十六进制数据
			String sensorData = modebus.getSensorIntData(sensorType, sensorHexData);//获得传感器实际数据
			Message msg = handler.obtainMessage();
			Bundle data = new Bundle();
			data.putInt("SensorImg", sensorImg);
			data.putString("SensorName", sensorName);
			data.putString("SensorAddr", sensorAddr);
			data.putString("SensorData", sensorData);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	}
}

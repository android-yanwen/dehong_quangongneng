package com.gtabox.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.MainActivity;
import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.taiyuan_sensor.WebServiceThread;
import com.gtafe.until.ModeBusStrParse;
import com.gtafe.until.SuportMethod;

public class BluetoothWindow extends SerialPortActivity {
	private String[] sensor_img_arr, sensor_name_arr, sensor_data_arr;
	private ArrayAdapter<String> all_device_array;
	private Spinner all_device_sp;
	private ListView sensor_lv;
	private SimpleAdapter simple_adp;
	private Button testBtn, scanBtn, connectBtn, disconnectBtn, helpBtn;
	private TextView dispOrderTv;
	private TextWatcher textWatcher;
	private EditText selectedDeviceTv;
	private SuportMethod sup;
	private DealDataHandler myHandler;
	private BluetoothOrderClass order;
	private String selectedDevice = "";
	private ScrollView disp_order_sv;
	private ModeBusStrParse modbus;
	
	private List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adp;
	/**
	 * 实现构造函数*/
	public BluetoothWindow() {
//		array = new String[]{"0x01", "0x02", "0x03", "0x04", "0x05", "0x06", "0x07"};
		sensor_img_arr = new String[]{"图片"};
		sensor_name_arr = new String[]{"雨滴传感器"};
		sensor_data_arr = new String[]{"无雨滴"};
		sup = new SuportMethod();
		order = new BluetoothOrderClass();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_window_xml);
		testBtn = (Button)findViewById(R.id.test_order_btn);
		testBtn.setOnClickListener(new ButtonClickedListener());
		scanBtn = (Button)findViewById(R.id.scan_order_btn);
		scanBtn.setOnClickListener(new ButtonClickedListener());
		connectBtn = (Button)findViewById(R.id.connect_order_btn);
		connectBtn.setOnClickListener(new ButtonClickedListener());
		disconnectBtn = (Button)findViewById(R.id.disconnect_order_btn);
		disconnectBtn.setOnClickListener(new ButtonClickedListener());
		helpBtn = (Button)findViewById(R.id.bluetooth_help_btn);
		helpBtn.setOnClickListener(new ButtonClickedListener());
		dispOrderTv = (TextView)findViewById(R.id.disp_order_tv);
		dispOrderTv.addTextChangedListener(new TextWatcher() {  //监听TextView文本变化
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				scrollToButtom();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		selectedDeviceTv = (EditText)findViewById(R.id.device_disp_tv);
		myHandler = new DealDataHandler();
		
		all_device_sp = (Spinner)findViewById(R.id.scan_spinner);
/*		//将数据与适配器连接起来
		all_device_array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
		//将适配器添加到Spinner控件
		all_device_sp.setAdapter(all_device_array);*/
		//设置项选择监听事件
		all_device_sp.setOnItemSelectedListener(new DeviceSpinnerItemSelectedListener());
		
/*		//为ListView添加数据
		List<Map<String, String>> listItem = new ArrayList<Map<String,String>>();
		for(int i = 0; i < sensor_name_arr.length; ++i){
			Map<String, String> map = new HashMap<String, String>();
			map.put("Name", sensor_name_arr[i]);
			map.put("Data", sensor_data_arr[i]);
			listItem.add(map);
		}
		simple_adp = new SimpleAdapter(getApplicationContext(), 
				listItem, 
				R.layout.bluetooth_sensor_xml, 
				new String[]{"Name", "Data"}, 
				new int[]{R.id.sensor_name_tv, R.id.sensor_data_tv});
		sensor_lv = (ListView)findViewById(R.id.bluetooth_sensor_lv);
		sensor_lv.setAdapter(simple_adp);*/
		
		sensor_lv = (ListView)findViewById(R.id.bluetooth_sensor_lv);
		disp_order_sv = (ScrollView)findViewById(R.id.disp_order_sv);
		adp = new SimpleAdapter(
				getApplicationContext(), 
				listItem, 
				R.layout.bluetooth_sensor_xml, 
				new String[]{"SensorImage", "SensorName", "SensorData"}, 
				new int[]{R.id.sensor_img_iv, R.id.sensor_name_tv, R.id.sensor_data_tv});
		sensor_lv.setAdapter(adp);
	}
	
	/**
	 * @author 鄢文
	 * @name scrollToButtom
	 * @brief 自动移动到scrollview的底部
	 * @param void
	 * @return void*/
	private void scrollToButtom(){
		disp_order_sv.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				disp_order_sv.smoothScrollTo(0, dispOrderTv.getBottom());
			}
		});
	}
	
	
	/**
	 * 监听Spinner的各个选项
	 * */
	private class DeviceSpinnerItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int item_num,
				long arg3) {
			// TODO Auto-generated method stub
//			Log.d("GTA", arg0.toString());
//			Log.d("GTA", arg1.toString());
//			Log.d("GTA", Integer.toString(item_num));
//			Log.d("GTA", Integer.toString((int)arg3));
			selectedDevice = order.ReceiveBuffer[item_num];  //获取当前被选中的设备
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	//监听Button
	private class ButtonClickedListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.test_order_btn:
				order.setOrder(BluetoothOrderClass.TEST_ORDER);
				sendOrderToSerial(order.getOrder());
				break;
				
			case R.id.scan_order_btn:
				order.clearReceiveBuffer();
				order.setOrder(BluetoothOrderClass.SCAN_ORDER);
				sendOrderToSerial(order.getOrder());
				break;
				
			case R.id.connect_order_btn:
				//确保selectedDevice接收到数据，再执行以下操作
				if(selectedDevice.isEmpty() == false){
					order.setOrder(BluetoothOrderClass.CONNECT_DEVICE_ORDER + selectedDevice.substring(0, 1) + "\r\n");
					sendOrderToSerial(order.getOrder());
					selectedDeviceTv.setText(selectedDevice);
				}
				break;
				
			case R.id.disconnect_order_btn:
				order.setOrder(BluetoothOrderClass.AT_RESET_ORDER);
				sendOrderToSerial(order.getOrder());
				break;
			//点击了蓝牙帮助按钮，弹出蓝牙介绍的对话框
			case R.id.bluetooth_help_btn:
//				LayoutInflater inflater = getLayoutInflater();
//				View view = inflater.inflate(R.layout.bluetooth_help_dialog_xml, (ViewGroup)findViewById(R.id.bluetooth_help_ly));
//				TextView help_tv = (TextView)view.findViewById(R.id.help_tv);
//				help_tv.setText("蓝牙简单介绍");
				AlertDialog.Builder dialog = new AlertDialog.Builder(BluetoothWindow.this);
				dialog.setTitle("蓝牙简介");
				dialog.setMessage("    蓝牙是一种支持设备短距离通信（一般是10m之内）的无线电技术。"+
						"能在包括移动电话、PDA、无线耳机、笔记本电脑、相关外设等众多设备之间进行无线信息交换。"+
						"蓝牙的标准是IEEE802.15，工作在2.4GHz 频带，带宽为1Mb/s\n" +
						"    蓝牙分主从模式，采用一对一的连接方式，点击扫描按钮可搜索附近蓝牙设备，点击连接按钮" +
						"将和选中的从设备配对，配对成功可接收数据。");
//				dialog.setView(view);
//				dialog.setPositiveButton();
				dialog.setPositiveButton("知道了", null);
				dialog.show();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		byte[] buf = Arrays.copyOf(buffer, size);
		if(buf[0] != 0x7E){
			String bufferStr = new String(buf); //将接收到的字节数组转换为ASCII字符串
//			Log.d("GTA", bufferStr +","+ Integer.toString(size) );
			Message msg = myHandler.obtainMessage();
			msg.what = 1;
			msg.obj = bufferStr;
			myHandler.sendMessage(msg);
		}
		else{
			String bufStr = sup.byteToHexString(buf).toUpperCase();
			Message msg = myHandler.obtainMessage();
			msg.what = 2;
			msg.obj = bufStr;
			myHandler.sendMessage(msg);
		}
	}
	
	/**
	 * @author yanwen
	 * @name DealDataHandler
	 * @brief 处理串口接收发送而来的数据
	 * */
	private class DealDataHandler extends Handler{
		int index = 0;
		int point = 0;
		boolean flag = false;
		private String receiveBuffer = "";
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			//扫描并对蓝牙进行配对
			case 1:
				String str = msg.obj.toString();
				//判断发送的是搜索设备命令
				if(order.getOrder().equals(BluetoothOrderClass.SCAN_ORDER) == true){
					/*//收到的是起始命令符
					if(str.equals(BluetoothOrderClass.OK_ORDER) == true){
						flag = true;
						Log.d("GTA", "............start order");
					}
					//收到结束命令符
					else */if(str.equals(BluetoothOrderClass.SCAN_END_ORDER) == true){
						receiveBuffer += "E";  //追加一个结束符E
//						Log.d("GTA", "............receiveBuffer:" + receiveBuffer);
						while(true){
							String num = receiveBuffer.substring(point, point+1);
//							Log.d("GTA", "num:"+num);
							if(num.equals("E") == true){
								receiveBuffer = "";
								point = 0;
								break;
							}
							//判断是否为数字
							if(isNumeric(num) == true){
								order.ReceiveBuffer[index] = receiveBuffer.substring(point, point+16);
//								Log.d("GTA", "..........order.ReceiveBuffer[index]:"+order.ReceiveBuffer[index]);
								index++;
								point += 16;
							}
							else {
								point++;
							}
						}
						
						String[] buffer = new String[]{};
						buffer = Arrays.copyOf(order.ReceiveBuffer, index);
						ArrayAdapter all_device_array = new ArrayAdapter<String>(
								BluetoothWindow.this, 
								R.layout.simple_spinner_xml, 
								buffer);
						//将获取到的设备名通过适配器显示到Spinner控件上
						all_device_sp.setAdapter(all_device_array);
//						flag = false;
						index = 0;
//						Log.d("GTA", "............end order");
					}
					//收到设备名
					else {
//						Log.d("GTA", "............str:"+str);
//						if(receiveBuffer.isEmpty())
//							receiveBuffer = str;
//						else 
							receiveBuffer += str;
				/*		if(flag == true){
							for(index = 0; ;index++){
								if(order.ReceiveBuffer[index].isEmpty() == true){
									str = str.substring(5); //储存设备名
									order.ReceiveBuffer[index] = str;
									Log.d("GTA", "str:"+str);
									Log.d("GTA", order.ReceiveBuffer[index]);
									break;
								}
							}
						}*/
					}
				}
				//判断是否发送的是连接设备命令
				else if(selectedDevice.isEmpty() == false){//确保缓存区有数据
					if(order.getOrder().equals(BluetoothOrderClass.CONNECT_DEVICE_ORDER + 
							selectedDevice.substring(0, 1) + "\r\n") == true){
//						Log.d("GTA", selectedDevice);
//						if(selectedDevice.isEmpty() == false){
//						Log.d("GTA", ".....................str:"+str+"aasdagd");
							if(str.equals(BluetoothOrderClass.CONNECTING_ORDER + selectedDevice.substring(2)+"\r\n") == true){
								Toast.makeText(getApplicationContext(), "连接中(╯-╰)。。。。", Toast.LENGTH_LONG).show();
							}
							else if(str.equals(BluetoothOrderClass.CONNECTED_ORDER + selectedDevice.substring(2)+"\r\n") == true){
								Toast.makeText(getApplicationContext(), "已连接 ↖(^ω^)↗", Toast.LENGTH_SHORT).show();
							}
//						}
					}
				}
				dispOrderTv.append(str);//利用TextView显示返回的命令
				break;
			//处理传感器数据
			case 2:
				String data = msg.obj.toString();
//				Log.d("GTA", "..........data:"+data);
				
				ModeBusStrParse modebus = new ModeBusStrParse(data);
				String sensorType = modebus.getSensorCategory()+modebus.getSensorType();//获得传感器的类型
				int[] sensorImg = new int[]{ModeBusStrParse.getSensorImg(sensorType)};//获得对应传感器图片
				String[] sensorName = new String[]{ModeBusStrParse.getSersorName(sensorType)};//获得传感器名字
//				Log.d("GTA", "..........sensorName:"+sensorName[0]);
				String sensorHexData = modebus.getSensorData(modebus.getSensorDataLength());//获得传感器的十六进制数据
				String value = modebus.getSensorIntData(sensorType, sensorHexData);
				String[] sensorData = new String[]{value};//获得传感器显示的数据结果

				//将获得的数据上传至云服务器
				new WebServiceThread(MainActivity.WEBSERVICE, 
						Integer.parseInt(modebus.getSensorAddr(), 16), 
						Integer.parseInt(modebus.getSensorType(), 16), 
						MainActivity.SYSTEM_ID, 
						value).start();
				listItem.clear();
				for(int x = 0; x < sensorImg.length; x++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("SensorImage", sensorImg[x]);
					map.put("SensorName", "名字：\r\n"+sensorName[x]);
					map.put("SensorData", "数据：\r\n"+sensorData[x]);
					listItem.add(map);
				}
				adp.notifyDataSetChanged();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		/**
		 * 判断String是否为数字
		 * */
		public  boolean isNumeric(String str){ 
		    Pattern pattern = Pattern.compile("[0-9]*"); 
		    return pattern.matcher(str).matches();    
		 } 
	}
	/**
	 * @author yanwen
	 * @name sendOrderToSerial
	 * @brief 发送命令到串口
	 * @param bluetoothStr
	 * @return void
	 * */
	private void sendOrderToSerial(String bluetoothStr){
		String sendBuffer = bluetoothStr;
		byte[] buffer = sendBuffer.getBytes();  //将ASCII转换为字节数组
//		Log.d("GTA", sup.byteToHexString(buffer));
//		Log.d("GTA", new String(buffer));
		try {
			if(mOutputStream != null)
				mOutputStream.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

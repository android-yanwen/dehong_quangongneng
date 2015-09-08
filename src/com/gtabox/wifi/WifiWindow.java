/**
 * @author 鄢文
 * @name WifiWindow.java
 * @brief Wifi模块数据获取窗口，通过TCP/IP通信协议进行通信
 * @data 2015/05/15
 * */
package com.gtabox.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.MainActivity;
import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.WebServiceThread;
import com.gtafe.until.ModeBusStrParse;
import com.gtafe.until.SuportMethod;

public class WifiWindow extends Activity implements android.view.View.OnClickListener{
	private static final int LISTENER_DATA_FLAG = 1;
	private static final int CONNECT_NWK_FLAG = 2;
	private SuportMethod sup;
	private ListView wifi_data_lv;
/*	private int[] sensor_img = new int[]{R.drawable.temp_humi_img80_80, R.drawable.illuminate_img80_80, R.drawable.alcohol_img80_80};
	private String[] sensor_name = new String[]{"温度传感器", "雨滴传感器", "光照传感器"};
	private String[] sensor_data = new String[]{"29℃", "有雨滴", "1350"};
	private String[] node_nwk_type = new String[]{"客户端", "服务器", "客户端"};*/
	private EditText server_ip_et;
	private EditText server_port_et;
	private Button lunch_server_btn;
	private TextWatcher textWatcher;  //监听文本变化
//	private Socket socket = null;
	private Handler handler;
	private LunchServerThread myThread = null;
	private Socket[] socketArray = new Socket[10];
	private ServerSocket servSocket = null;
	private Map<String, Map<String, Object>> SensorMap = new HashMap<String, Map<String,Object>>();
	private String[] sensorTypesBuf; 
	private boolean threadFlag = false;

	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	SimpleAdapter adpt;
	public WifiWindow() {
		// TODO Auto-generated constructor stub
		sup = new SuportMethod();
		//变量初始化
		for(int x = 0; x < socketArray.length; ++x){
			socketArray[x] = null;
		}
		
		sensorTypesBuf = new String[socketArray.length];
		for(int x = 0; x < socketArray.length; ++x){
			sensorTypesBuf[x] = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_window_xml);
		lunch_server_btn = (Button)findViewById(R.id.lunch_server_btn);

		wifi_data_lv = (ListView)findViewById(R.id.wifi_data_lv);
		adpt = new SimpleAdapter(getApplicationContext(),
				list, 
				R.layout.wifi_sensor_data_xml, new String[]{"SensorImage", "SensorName", "SensorData"},
				new int[]{R.id.sensor_type_img_iv, R.id.sensor_name_tv, R.id.sensor_data_tv});
		wifi_data_lv.setAdapter(adpt);
		
		
		lunch_server_btn.setOnClickListener(this);
		
		//监听对话框的文本变化
		textWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
//				Log.d("GTA", "onTextChanged:"+s.toString());
//				Log.d("GTA", "start:"+Integer.toString(start));
//				Log.d("GTA", "before:"+Integer.toString(before));
//				Log.d("GTA", "count:"+Integer.toString(count));
				
				if(s.length() <= 0)return;
				if((Integer.parseInt(s.toString()) >= 65535) ||
						(Integer.parseInt(s.toString()) < 0)){
					Toast.makeText(getApplicationContext(), 
							"超出端口号设定范围，请重新设定。", 
							Toast.LENGTH_SHORT).show();
					server_port_et.setText(s.toString().substring(0, s.toString().length()-1));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				Log.d("GTA", "beforeTextChanged:"+s.toString());
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d("GTA", "afterTextChanged:"+s.toString());
//				s.delete(s.toString().length()-1, s.toString().length());
			}
		};
		
		/**
		 * 重写handlerMessage，处理线程发来的消息
		 * */
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LISTENER_DATA_FLAG:
//					String str = msg.obj.toString();
//					String str = Integer.toString((Integer) msg.obj);
//					Log.d("GTA", "str:" + str);
					int size = 0;
					Bundle bundle = msg.getData();
					size = bundle.getInt("Size", 0);
//					Log.d("GTA", "Size:"+String.valueOf(size));
					byte[] dataByte = bundle.getByteArray("Data");
					
//					Log.d("GTA", "dataByte:" + sup.byteToHexString(dataByte));
//					Log.d("GTA", "dataByteSize:"+String.valueOf(dataByte.length));
					
					byte[] byteArray = Arrays.copyOf(dataByte, size);
					String datastr = sup.byteToHexString(byteArray).toUpperCase();
					
					//判断是否为modebus协议数据包头7E
					if(datastr.substring(0, 2).equals(ModeBusStrParse.MODEBUS_DATA_HEAD)){
						ModeBusStrParse modebus = new ModeBusStrParse(datastr);
						String sensorTys = modebus.getSensorTypes();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("SensorImage", ModeBusStrParse.getSensorImg(sensorTys));
						map.put("SensorName", ModeBusStrParse.getSersorName(sensorTys));
//						Log.d("GTA", "...........SensorName:" + ModeBusStrParse.getSersorName(sensorTys));
						String length = modebus.getSensorDataLength();
						String value = modebus.getSensorIntData(sensorTys, modebus.getSensorData(length));
						map.put("SensorData", value);
						//将获得的数据上传至云服务器
						new WebServiceThread(MainActivity.WEBSERVICE, 
								Integer.parseInt(modebus.getSensorAddr(), 16), 
								Integer.parseInt(modebus.getSensorType(), 16), 
								MainActivity.SYSTEM_ID, 
								value).start();
						SensorMap.put(sensorTys, map);
						for(int x = 0; x < 10; ++x){
							if(sensorTypesBuf[x] == null || sensorTypesBuf[x].equals(sensorTys)){
								sensorTypesBuf[x] = sensorTys;
								break;
							}
						}
						
						list.clear();
						for(int x = 0; x < SensorMap.size(); x++){
							Map<String, Object> m = new HashMap<String, Object>();
							m = SensorMap.get(sensorTypesBuf[x]);
							int sensorImg = (Integer) m.get("SensorImage");
							String sensorNm = (String) m.get("SensorName");
							String sensorData = (String) m.get("SensorData");
							Map<String, Object> mp = new HashMap<String, Object>();
							mp.put("SensorImage", sensorImg);
							mp.put("SensorName", sensorNm);
							mp.put("SensorData", sensorData);
							list.add(mp);
						}
						adpt.notifyDataSetChanged();
					}
					break;
				case CONNECT_NWK_FLAG:
//					if(socket != null){
//						Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
//					}
//					else {
//						Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
//					}
				default:
					break;
				}
			};
		};
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		threadFlag = true;
		super.onResume();
	}

	/**
	 * @author yanwen
	 * @name onCreateOptionMenu
	 * @brief 选项菜单键，点击弹出选项菜单
	 * @param menu
	 * @return boolean*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//组号    选项号   排列顺序（小号在前）  选项卡标题
		menu.addSubMenu(0, 0, 0, "配置服务器");
		menu.addSubMenu(0, 1, 1, "关闭");
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * @author yanwen
	 * @name onOptionsItemSelected
	 * @brief 单击某个选项时挑到详细操作界面
	 * @param item
	 * @reture boolean*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.wifi_dialog_xml, (ViewGroup)findViewById(R.id.dialog));
			server_ip_et = (EditText)layout.findViewById(R.id.server_address_et);
			server_port_et = (EditText)layout.findViewById(R.id.server_port_et);
			server_port_et.addTextChangedListener(textWatcher);
			new AlertDialog.Builder(this).setTitle("配置").setView(layout)
			.setPositiveButton("确定", new DialogButtonListener())
			.setNegativeButton("取消",  new DialogButtonListener()).show();
			SharedPreferences sp = getSharedPreferences("com.gtafe.taiyuan_sensor_preferences", MODE_PRIVATE);
//			server_ip_et.setText(sp.getString("IP", "192.168.1.1"));
			server_ip_et.setText(getLocalHostIp());
			server_port_et.setText(sp.getString("PORT", "8000"));
			break;
		case 1:
			WifiWindow.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * @author yanwen
	 * @name DialogButtonListener
	 * @brief 监听对话框被点击事件
	 * */
	class DialogButtonListener implements OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch (which) {
			case -1:
				String ip = server_ip_et.getText().toString();
				String port = server_port_et.getText().toString();
				if(ip.equals("")==true){
					ip = "192.168.1.1";
				}
				if(port.equals("")==true){
					port = "8000";
				}
				SharedPreferences preferences = getSharedPreferences("com.gtafe.taiyuan_sensor_preferences", MODE_PRIVATE);
				SharedPreferences.Editor edit;
				edit = preferences.edit();
				edit.putString("IP", ip);
				edit.putString("PORT", port);
				edit.commit();
				Log.d("GTA", "点击了确定");
				break;
			case -2:
				Log.d("GTA", "点击了取消");
				break;
			default:
				break;
			}
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp = getSharedPreferences("com.gtafe.taiyuan_sensor_preferences", MODE_PRIVATE);
//		String dstAddress = sp.getString("IP", "0");
		String port = sp.getString("PORT", "8000");
		int dstPort = Integer.parseInt(port);
//		Log.d("GTA", Integer.toString(dstPort));
		/*new Thread(new StartConnectThread(dstAddress, dstPort)).start();*/
		if(myThread == null){
			myThread = new LunchServerThread(dstPort);
			myThread.start();
//			Log.d("GTA", "Current thread lunch");
		}
	}
/*	//启动网络连接的线程
	private class StartConnectThread implements Runnable{
		private String dstAddress;
		private int dstPort;
		public StartConnectThread(String dstAddress, int dstPort) {
			// TODO Auto-generated constructor stub
			this.dstAddress = dstAddress;
			this.dstPort = dstPort;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if(socket == null){
					socket = new Socket(dstAddress, dstPort); //连接到服务器
					new Thread(new ClientReadListener()).start();
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = handler.obtainMessage();
			msg.what = WifiWindow.CONNECT_NWK_FLAG;
			handler.sendMessage(msg);
		}
	}*/
	/**
	 * @author yanwen
	 * @brief 集成Runnable接口，监听数据
	 * */
	private class ClientReadListener implements Runnable{
//		private String str;
		private Socket socket;
		private int num;
		byte[] buffer = new byte[50];
		private BufferedReader br = null;
		
		private InputStream inputStream;
		public ClientReadListener(Socket s) {
			// TODO Auto-generated constructor stub
			socket = s;
			try {
//				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				inputStream = socket.getInputStream();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(threadFlag){
				try {
//					br.skip(1);//前进一个字节
					//阻塞
					/*if((str = br.readLine()) != null(num = br.read(buffer)) > 0){*/
					num = inputStream.read(buffer);
					if(num > 0 == true){
//						Log.d("GTA", ".........." + byte.class.);
//						byte[] buf = null;
//						br.skip(num);
//						Log.d("GTA", ".........." + sup.byteToHexString(buffer));
						Message message = handler.obtainMessage();
						message.what = WifiWindow.LISTENER_DATA_FLAG;
//						message.obj = str;
						Bundle bundle = new Bundle();
						bundle.putInt("Size", num);
//						bundle.putCharArray("Data", buffer);
						bundle.putByteArray("Data", buffer);
						message.setData(bundle);
						handler.sendMessage(message);
					}
/*					byte[] buf = new byte[100];
					if(is.read(buf) > 0){
						Log.d("GTA", ".........." + sup.byteToHexString(buf));
					}*/
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 监听客户端连接的线程
	 * */
	private class LunchServerThread extends Thread{
		private Socket so;
//		private ClientReadListener[] readListener;
//		private Thread[] readThread;
//		private int clientCnt = 0;
		public LunchServerThread(int port) {
			// TODO Auto-generated constructor stub
//			Log.d("GTA", "..........Lunch server thread....");
			try {
//				Log.d("GTA", "..........servSocket:" + servSocket);
				if(servSocket == null)
					servSocket = new ServerSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!Thread.interrupted()){
				if(servSocket != null){
					try {
//						Log.d("GTA", "...................");
						so = servSocket.accept();
//						readListener[clientCnt] = new ClientReadListener(so);
					/*	readThread[clientCnt] = new Thread(new ClientReadListener(so));
						readThread[clientCnt].start();*/
						new Thread(new ClientReadListener(so)).start();
//						clientCnt++;
//						Log.d("GTA", "...................." + so.toString());
						//存储所有连接上来的客户端Socket
						for(int i = 0; i < socketArray.length; i++){
							if(socketArray[i] == null || socketArray[i].equals(so)){
								socketArray[i] = so;
								break;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			super.run();
		}
		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
//			Log.d("GTA", "..........1111111110..........");
			try {
				//关闭全部的客户端Socket
				for(int i = 0; i < socketArray.length; i++){
					if(socketArray[i] != null){
//						Log.d("GTA", "..........socketArray[]:"+socketArray[i]);
						socketArray[i].close();
						socketArray[i] = null;
					}
				}
				servSocket.close();//关闭服务器端Socket
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.interrupt();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		Log.d("GTA", "..........onDestroy");
		if(myThread != null)
			myThread.interrupt();
		threadFlag = false;
		super.onDestroy();
	}
	
	
/*	private class ListAdapter extends BaseAdapter{
		private int[] imgRes;
		private Context context;
		public ListAdapter(int[] res, Context context) {
			// TODO Auto-generated constructor stub
			this.imgRes = res;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.imgRes.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Activity activity = (Activity) this.context;
			LinearLayout out = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.wifi_sensor_data_xml, null);
			ImageView iv = (ImageView) out.findViewById(R.id.sensor_type_img_iv);
			iv.setImageResource(imgRes[position]);
			return out;
		}
		
	}*/
	// 得到本机ip地址
    public String getLocalHostIp()
    {
        String ipaddress = "";
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements())
            {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements())
                {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                                    .getHostAddress()))
                    {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        }
        catch (SocketException e)
        {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;

    }
}

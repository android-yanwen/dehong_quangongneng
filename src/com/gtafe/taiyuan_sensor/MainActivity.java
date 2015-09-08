package com.gtafe.taiyuan_sensor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gtabox.bluetooth.BluetoothWindow;
import com.gtabox.gprs.GprsWindow;
import com.gtabox.ipv6.Ipv6Window;
import com.gtabox.rfid.RfidWindow;
import com.gtabox.wifi.WifiWindow;
import com.gtabox.zigbee.ZigbeeWindow;
import com.gtafe.taiyuan_sensor.CircleMenuLayout.OnMenuItemClickListener;

public class MainActivity extends Activity{
	static public String WEBSERVICE;
	static public int SYSTEM_ID = 1;
	final private int GROUP_MENU_0 = 0;
	final private int ITEM_MENU_0 = 0, ITEM_MENU_1 = 1;

	private CircleMenuLayout mCircleMenuLayout;
	private int[] icon = new int[]{R.drawable.bluetooth, R.drawable.gprs, R.drawable.ipv6,
			R.drawable.rfid, R.drawable.wifi, R.drawable.zigbee};
	private String[] text = new String[]{"蓝牙","GPRS","IPV6","RFID","WIFI","ZIGBEE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleMenuLayout = (CircleMenuLayout)findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(icon, text);
        
        mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void itemClick(View view, int pos) {
				// TODO Auto-generated method stub
//				Log.d("GTA", "View:"+view+"pos:"+Integer.toString(pos));
				SharedPreferences sp = getSharedPreferences("com.gtafe.taiyuan_sensor_preferences", MODE_PRIVATE);
				WEBSERVICE = sp.getString("WebAddress", "http://192.168.187.21:8080/Services.asmx");
				SYSTEM_ID = Integer.parseInt(sp.getString("SystemId", "1"));
				switch (pos) {
				case 0:
					Intent bluetooth_intent = new Intent(MainActivity.this, BluetoothWindow.class);
					startActivity(bluetooth_intent);
					break;
				case 1:
					Intent gprs_intent = new Intent(MainActivity.this, GprsWindow.class);
					startActivity(gprs_intent);
					break;
				case 2:
					Intent ipv6_intent = new Intent(MainActivity.this, Ipv6Window.class);
					startActivity(ipv6_intent);
					break;
				case 3:
					Intent rfid_intent = new Intent(MainActivity.this, RfidWindow.class);
					startActivity(rfid_intent);
					break;
				case 4:
					Intent wifi_intent = new Intent(MainActivity.this, WifiWindow.class);
					startActivity(wifi_intent);
					break;
				case 5:
					Intent zigbee_intent = new Intent(MainActivity.this, ZigbeeWindow.class);
					startActivity(zigbee_intent);
					break;
				default:
					break;
				}
				
			}

			@Override
			public void itemCenterClick(View view) {
				// TODO Auto-generated method stub
				
			}
		});
        
	}
	
	/**
	 * @author Administrator
	 * @brief 初始化选项菜单
	 * @param menu
	 * @return true or false*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    		          //组号          选项      顺序号（小号在钱）  显示的字幕
		menu.addSubMenu(GROUP_MENU_0, ITEM_MENU_0, 0, "串口设置");
		menu.addSubMenu(GROUP_MENU_0, ITEM_MENU_1, 1, "退出程序");
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @author Administrator
	 * @brief 响应被选中的菜单项
	 * @param item
	 * @return true or false*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case ITEM_MENU_0:
			Intent intent = new Intent(MainActivity.this, SerialPortPreferences.class);
			MainActivity.this.startActivity(intent);
			break;
		case ITEM_MENU_1:
			MainActivity.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}






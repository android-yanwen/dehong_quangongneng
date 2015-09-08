/**
 * @author 鄢文
 * @name ZigbeeWindow.java
 * @brief zigbee操作窗口，通过选项卡切换查看zigbee传感器的数据和zigbee拓扑结构图
 * @data 2015/05/07 
 * */

package com.gtabox.zigbee;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.gtafe.taiyuan_sensor.R;

public class ZigbeeWindow extends TabActivity{

	private TabHost tabhost;
	public ZigbeeWindow() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zigbee_window_xml);
		tabhost = getTabHost();
		addTabSpec(this, ZigbeeDataWindow.class, "Zigee传感器数据");
		addTabSpec(this, ZigbeeTopologicWindow.class, "Zigee拓扑结构");
		tabhost.setCurrentTab(0);
	}

	/**
	 * @author yanwen
	 * @name addTabSpac
	 * @brief 增加选项卡切换窗口
	 * @param context 上下文环境
	 *        cls  待跳转的类
	 *        str  所增加的选项卡的名称
	 * @reture null*/
	private void addTabSpec(Context context, Class<?> cls, String str){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(context, cls);
		TabSpec tabspac = tabhost.newTabSpec("zigbee");
		tabspac.setIndicator(str);
		tabspac.setContent(intent);
		tabhost.addTab(tabspac);
	}
}

package com.gtabox.rfid;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.gtafe.taiyuan_sensor.R;


public class RfidWindow extends TabActivity{
	public static TabHost tabhost;
	public RfidWindow() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rfid_window_xml);
		tabhost = getTabHost();
		addTabSpec(this, LrfidActivity.class, "125KHz");
		addTabSpec(this, HrfidActivity.class, "13.56MHz");
		addTabSpec(this, UrfidActivity.class, "900MHz");
		tabhost.setCurrentTab(1);
	}

	/**
	 * @author yanwen
	 * @name addTabSpec
	 * @brief 增加选项卡
	 * @param context  当前类
	 *        cls  需要跳转的类窗口
	 *        label 选项卡的名字
	 * @return void*/
	private void addTabSpec(Context context, Class<?> cls, CharSequence label){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(context, cls);
		TabSpec tabspec = tabhost.newTabSpec("RFID");
		tabspec.setIndicator(label);
		tabspec.setContent(intent);
		tabhost.addTab(tabspec);
		return;
	}
}

/**
 * @author 鄢文
 * @name GprsWindow.java
 * @brief GPRS模块获取当前位置和时间数据
 * @data 2015/05/13
 * */

package com.gtabox.gprs;

import android.os.Bundle;
import android.os.Handler;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;

public class GprsWindow extends SerialPortActivity{
	public GprsWindow() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gprs_wait_window_xml);
		Handler handler = new Handler();
		handler.postDelayed(thread, 3000);  //推入线程队列
	}
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * @author yanwen
	 * @name Runnable
	 * @brief 实现Runnable接口，2.5s后执行这个函数，更换界面布局*/
	private Runnable thread = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			setContentView(R.layout.gprs_window_xml);
		}
	};
}

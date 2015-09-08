package com.gtabox.rfid;
import java.io.IOException;
import java.util.Arrays;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.until.SuportMethod;

public class LrfidActivity extends SerialPortActivity  {

	private EditText low_id;
	private Button register;
	private Button unregister;
	private Button clear;

	private String serial_id;
//	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ContentValues values;
	SuportMethod suport;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lowrfid);
		low_id = (EditText) findViewById(R.id.low_id);
//		register = (Button) findViewById(R.id.register_card);
//		unregister = (Button) findViewById(R.id.unregister_card);
//		clear = (Button) findViewById(R.id.clear_low);

		suport = new SuportMethod();
//		dbHelper = new DatabaseHelper(LrfidActivity.this, "RFID");
//		db = dbHelper.getWritableDatabase();
//		values = new ContentValues();
//
//		register.setOnClickListener(this);
//		unregister.setOnClickListener(this);
//		clear.setOnClickListener(this);

	}
/*
	//实现按钮监听方法
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.register_card: // 注册卡号
			if (serial_id == null) {
				low_id.setText("没有卡可注册，请先刷卡");
			} else {
				Log.d("LrfidActivity", serial_id);
				values.put("serialid", serial_id);
				if (isHasID(serial_id)) {
					Toast.makeText(LrfidActivity.this, "该卡已注册，无需重复注册",
							Toast.LENGTH_SHORT).show();
				} else {
					db.insert("low_rfid", null, values);
					low_id.setText(serial_id + "\t(已注册)");
					Toast.makeText(LrfidActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.unregister_card: // 解除绑定
			if (serial_id == null) {
				low_id.setText("没有指定解绑卡，请先刷卡");
			} else {
				if (isHasID(serial_id)) {
					db.delete("low_rfid", "serialid=?",
							new String[] { serial_id });
					low_id.setText(serial_id + "\t(未注册)");
					Toast.makeText(LrfidActivity.this, "解绑成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LrfidActivity.this, "该卡没有注册，无需解绑",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.clear_low: // 清除数据
			low_id.setText(null);
			break;

		}
	}*/

	// 串口数据回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			private byte[] buff;

			@Override
			public void run() {
				System.out.println("收到消息");
				System.out.println("size= " + size);
				buff = Arrays.copyOf(buffer, size);
				System.out.println("buff= " + suport.byteToHexString(buff));
				if (size < 3) {
					buff = null;
					low_id.setText(null);
				} else {
					if (suport.byteToHexString(buff).length() >= 17) {
						serial_id = suport.byteToHexString(buff).substring(7,
								17);/*
						if (isHasID(serial_id)) {
							low_id.setText(serial_id + "\t(已注册)");
						} else {
							low_id.setText(serial_id + "\t(未注册)");
						}
						System.out.println("buffid= " + serial_id);*/
						low_id.setText(serial_id);
					}
				}
			}
		});
	}

/*	// 判断该卡是否已经注册
	private boolean isHasID(String serial) {
		boolean ret = false;
		Cursor cursor = db.query("low_rfid", new String[] { "serialid" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			String serialid = cursor.getString(cursor
					.getColumnIndex("serialid"));
			System.out.println("cursorid=" + serialid);
			if (serialid.equals(serial)) {
				ret = true;
				break;
			}
		}
		return ret;
	}*/

	// activity暂停时关闭I/O流
	@Override
	protected void onPause() {
		super.onPause();
		try {
			mOutputStream.close();
			mInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

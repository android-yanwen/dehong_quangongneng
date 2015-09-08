package com.gtabox.rfid;

import java.io.IOException;
import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.until.Command;
import com.gtafe.until.RfidCommand;
import com.gtafe.until.SuportMethod;

public class UrfidActivity extends SerialPortActivity implements
		OnClickListener, OnCheckedChangeListener {
	SuportMethod suport;

	private byte[] mBuffer;
	private RfidCommand com;
	private int Flag;
	String epcid;
	String block;

	private EditText module_info;
	private EditText tag_info;
	private EditText addrstart;
	private EditText wordlen;
	private EditText password;
	private EditText tag_data;
	private RadioGroup rg;
	private String addr, lendata, lenid, pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ultrarfid);

		suport = new SuportMethod();
		com = new RfidCommand();

		rg = (RadioGroup) findViewById(R.id.block_rg);
		Button read_module = (Button) findViewById(R.id.read_module);
		Button query_tag = (Button) findViewById(R.id.query_tag);
		Button read_tag = (Button) findViewById(R.id.read_ultra);
		Button write_tag = (Button) findViewById(R.id.write_ultra);

		module_info = (EditText) findViewById(R.id.module_info);
		tag_info = (EditText) findViewById(R.id.tag_info);
		tag_data = (EditText) findViewById(R.id.tag_data);
		addrstart = (EditText) findViewById(R.id.addr_start);
		wordlen = (EditText) findViewById(R.id.word_len);
		password = (EditText) findViewById(R.id.password);

		read_module.setOnClickListener(this);
		query_tag.setOnClickListener(this);
		read_tag.setOnClickListener(this);
		write_tag.setOnClickListener(this);
		rg.setOnCheckedChangeListener(this);
	}

	// 数据区域的选择
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.keep_section:
			block = "00";
			break;
		case R.id.epc_section:
			block = "01";
			break;
		case R.id.tid_section:
			block = "02";
			break;
		case R.id.user_section:
			block = "03";
			break;
		}
	}

	// 按钮监听实现方法
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.read_module: // 读取模块信息
			mBuffer = com.URFID_read_module();
			send(mBuffer);
			Flag = 1;
			break;
		case R.id.query_tag: // EPC查询
			mBuffer = com.URFID_query_tag();
			send(mBuffer);
			Flag = 2;
			break;
		case R.id.read_ultra: // 读取区域数据
			if (Init_data()) {
				mBuffer = com.URFID_read_tag(block, addr, lendata, pass, epcid);
				send(mBuffer);
				Flag = 3;
			}
			break;
		case R.id.write_ultra: // 写入区域数据
			if (Init_data()) {
				String writedata = tag_data.getText().toString().trim();
				if (writedata.length() == 0) {
					Toast.makeText(UrfidActivity.this, "请输入数据",
							Toast.LENGTH_SHORT).show();
				} else {
					if (writedata.length() < 16) {
						writedata = suport.addZeroForNum(writedata,
								4 * Integer.parseInt(lendata));
					}
					mBuffer = com.URFID_write_tagdata(block, addr, lenid,
							lendata, pass, epcid, writedata);
					send(mBuffer);
					Flag = 4;
				}
			}
			break;
		}
	}

	// 串口数据回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			private byte[] buff;
			String ret = null;

			@Override
			public void run() {
				System.out
						.println("-----------------------超高频卡收到消息-------------------------");
				System.out.println("size=" + size);
				buff = Arrays.copyOf(buffer, size);
				System.out.println("buff= " + suport.byteToHexString(buff));
				if (size < 4) {
					buff = null;
				} else {
					ret = suport.byteToHexString(buff).substring(6, 8);
					System.out.println("ret=" + ret);

					switch (Flag) {
					case 1:
						if (ret.equals("00".toString())) {
							String data = suport.byteToHexString(buff)
									.substring(8, 32);
							module_info.setText(data);
						} else {
							Toast.makeText(UrfidActivity.this, "读写模块信息读取失败",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 2:
						if (size < 16) {
							Toast.makeText(UrfidActivity.this, "未发现有效标签",
									Toast.LENGTH_SHORT).show();
						} else {
							if (ret.equals("01".toString())) {
								int len = Integer.parseInt(suport
										.byteToHexString(buff)
										.substring(12, 14), 16);
								epcid = suport.byteToHexString(buff).substring(
										14, 14 + 2 * len);
								tag_info.setText(epcid);
							}
						}
						break;
					case 3:
						if (ret.equals("00".toString())) {
							String data = suport.byteToHexString(buff)
									.substring(8, 2 * (buff.length - 2));
							tag_data.setText(data);
						} else {
							Toast.makeText(UrfidActivity.this, "读取错误",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 4:
						if (ret.equals("00".toString())) {
							Toast.makeText(UrfidActivity.this, "写入成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(UrfidActivity.this, "写入失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					Flag = 0;
				}
			}
		});
	}

	// 数据初始化
	public boolean Init_data() {
		boolean ret = true;
		if (epcid != null) {
			lenid = "" + epcid.length() / 4;
			if (lenid.length() % 2 != 0) {
				lenid = "0" + lenid;
			}
			if (block == null) {
				Toast.makeText(UrfidActivity.this, "请选择操作区", Toast.LENGTH_SHORT)
						.show();
				ret = false;
			}
			addr = addrstart.getText().toString().trim();
			lendata = wordlen.getText().toString().trim();
			pass = password.getText().toString().trim();
			if (addr.length() == 0 || lendata.length() == 0) {
				Toast.makeText(UrfidActivity.this, "请输入起始地址或读写字长",
						Toast.LENGTH_SHORT).show();
				ret = false;
			} else {
				if (addr.length() < 2)
					addr = suport.addZeroForNum(addr, 2);
				if (lendata.length() < 2)
					lendata = suport.addZeroForNum(lendata, 2);
			}

			if (pass.length() == 0) {
				pass = "00000000";
			} else if (pass.length() < 8) {
				pass = suport.addZeroForNum(pass, 8);
			}
		} else {
			Toast.makeText(UrfidActivity.this, "请先查询有效EPC", Toast.LENGTH_SHORT)
					.show();
			ret = false;
		}

		return ret;
	}

	// 发送指令
	public void send(byte[] by) {
		try {
			mOutputStream.write(by);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

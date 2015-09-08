package com.gtabox.rfid;

import java.io.IOException;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.until.Command;
import com.gtafe.until.RfidCommand;
import com.gtafe.until.SuportMethod;

public class HrfidActivity extends SerialPortActivity implements
		OnClickListener {
	SuportMethod suport;

	private String cardserial, writectrdata;
	private float float_balance;
	private int int_balance,temp;
	private byte[] mBuffer;
	private RfidCommand com;
	private int Flag;
	private EditText find_tv, prevent_tv, fix_tv, verity_in, read_tv, init_in,
			deduct_in, add_in, sec_id, readsec_tv, writesec_data, writesec_id,
			readctr_data, writectr_data;
	private TextView verity_tv, init_tv, deduct_tv, add_tv, writesec_tv,
			readctr_tv, wrtiectr_tv;
	private Button readbalance, initbalance, deductmoney, recharge, openwire,
			closewire, findcard, preventconflit, fixcard, veritypass, read_sec,
			write_sec, writectr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highrfid);

		suport = new SuportMethod();
		com = new RfidCommand();

		InitWidget();
	}

	// 实现按钮监听方法
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.open_wire: // 打开天线
			mBuffer = com.HRFID_open_wire();
			send(mBuffer);
			Flag = 1;
			break;
		case R.id.close_wire: // 关闭天线
			mBuffer = com.HRFID_close_wire();
			send(mBuffer);
			Flag = 2;
			break;
		case R.id.find_card: // 寻卡查询
			mBuffer = com.HRFID_find_card();
			send(mBuffer);
			Flag = 3;
			break;
		case R.id.prevent_conflit: // 防止冲突
			mBuffer = com.HRFID_prevent_conflit();
			send(mBuffer);
			Flag = 4;
			break;
		case R.id.fix_card: // 选定卡
			mBuffer = com.HRFID_fix_card(cardserial);
			if (suport.byteToHexString(mBuffer).substring(16, 24)
					.equals("00000000".toString())) {
				fix_tv.setText("没有卡可选");
			} else {
				send(mBuffer);
				Flag = 5;
			}
			break;
		case R.id.verity_pass: // 密钥验证
			String password = verity_in.getText().toString().trim();
			if (password.length() == 0) {
				password = suport.addZeroForNum("0", 12);
			} else if (password.length() > 0 && password.length() < 12) {
				password = suport.addZeroForNum(password, 12);
			}
			mBuffer = com.HRFID_Verity_password(password);
			send(mBuffer);
			Flag = 6;
			break;
		case R.id.read_balance: // 读取余额
			mBuffer = com.HRFID_read_balance();
			send(mBuffer);
			Flag = 7;
			break;
		case R.id.init_balance: // 初始化金额
			String initmoney = init_in.getText().toString().trim();
			if (initmoney.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请输入初始化金额",
						Toast.LENGTH_SHORT).show();
			} else {
				int i = (int)(Float.parseFloat(initmoney)*100);
				initmoney = Integer.toHexString(i);
				if (initmoney.length() > 0 && initmoney.length() < 8) {
					initmoney = suport.addZeroForNum(initmoney, 8);
				}
				initmoney = suport.LHtoHL(initmoney);
				mBuffer = com.HRFID_init_balance(initmoney);
				send(mBuffer);
//				Log.i("字节数组转十六进制字符串", new Suport_Method().byteToHexString(mBuffer));
				Flag = 8;
			}
			break;
		case R.id.deduct_balance: // 扣款付账
			if (int_balance == 0) {
				mBuffer = com.HRFID_read_balance();
				send(mBuffer);
				Flag = 7;
				temp = 1;
				break;
			}
			String deduction = deduct_in.getText().toString().trim();
			if (deduction.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请输入扣款金额",
						Toast.LENGTH_SHORT).show();
			} else {
				int i = (int)(Float.parseFloat(deduction)*100);
				if (i > int_balance) {
					Toast.makeText(HrfidActivity.this, "余额不足，扣款失败",
							Toast.LENGTH_SHORT).show();
				} else {
					deduction = Integer.toHexString(i);
					if (deduction.length() > 0 && deduction.length() < 8) {
						deduction = suport.addZeroForNum(deduction, 8);
					}
					deduction = suport.LHtoHL(deduction);
					System.out.println("deduction=" + deduction);
					mBuffer = com.HRFID_deduct_money(deduction);
					send(mBuffer);
					Flag = 50;
					temp = 0;
				}
			}
			break;
		case R.id.add_balance: // 充值
			String addMoney = add_in.getText().toString().trim();
			if (addMoney.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请输入充值金额",
						Toast.LENGTH_SHORT).show();
			} else {
				int i = (int)(Float.parseFloat(addMoney)*100);
				addMoney = Integer.toHexString(i);
				if (addMoney.length() > 0 && addMoney.length() < 8) {
					addMoney = suport.addZeroForNum(addMoney, 8);
				}
				addMoney = suport.LHtoHL(addMoney);
				mBuffer = com.HRFID_recharge(addMoney);
				send(mBuffer);
				Flag = 9;
			}
			break;
		case R.id.read_sec: // 读扇区内容
			String secid = sec_id.getText().toString().trim();
			if (sec_id.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请指定读取块地址",
						Toast.LENGTH_SHORT).show();
			} else {
				if (sec_id.length() < 2) {
					secid = suport.addZeroForNum(secid, 2);
				}
				mBuffer = com.HRFID_read_section(secid);
				send(mBuffer);
				Flag = 10;
			}
			break;
		case R.id.write_sec: // 写扇区内容
			String writesecid = writesec_id.getText().toString().trim();
			String writedata = writesec_data.getText().toString().trim();
			if (writesec_id.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请指定写入地址",
						Toast.LENGTH_SHORT).show();
			} else {
				if (writesecid.length() < 2) {
					writesecid = suport.addZeroForNum(writesecid, 2);
				}
				if (writedata.length() == 0) {
					Toast.makeText(HrfidActivity.this, "请输入写入扇区内容",
							Toast.LENGTH_SHORT).show();
					break;
				} else if (writedata.length() > 0 && writedata.length() < 32) {
					writedata = suport.addZeroForNum(writedata, 32);
				}
				mBuffer = com.HRFID_write_section(writesecid, writedata);
				send(mBuffer);
				Flag = 11;
			}
			break;
		// case R.id.readctr_sec: // 读扇区控制块
		// String ctrid = readctr_id.getText().toString().trim();
		// if (readctr_id.length() == 0) {
		// Toast.makeText(HrfidActivity.this, "请输入读取控制块的地址",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// if (readctr_id.length() < 2) {
		// ctrid = suport.addZeroForNum(ctrid, 2);
		// }
		// mBuffer = com.HRFID_read_control(ctrid);
		// send(mBuffer);
		// Flag = 12;
		// }
		// break;
		case R.id.writectr_sec: // 修改密钥A
			writectrdata = writectr_data.getText().toString().trim();

			if (writectrdata.length() == 0) {
				Toast.makeText(HrfidActivity.this, "请输入新密码", Toast.LENGTH_SHORT)
						.show();
				break;
			} else if (writectrdata.length() != 12) {
				// writectrdata = suport.addZeroForNum(writectrdata, 12);
				Toast.makeText(HrfidActivity.this, "输入的新密码不符合要求，请输入12位的新密码！",
						Toast.LENGTH_SHORT).show();
				break;
			}

			new AlertDialog.Builder(HrfidActivity.this)

			.setTitle("提示：")

			.setMessage("是否确定修改密码？")

			.setPositiveButton("确认", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mBuffer = com.HRFID_write_control("03", writectrdata);
					send(mBuffer);
					Flag = 13;
					dialog.dismiss();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();

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
						.println("-----------------------高频卡收到消息-------------------------");
				System.out.println("size=" + size);
				buff = Arrays.copyOf(buffer, size);
				System.out.println("buff= " + suport.byteToHexString(buff));
				if (size < 8) {
					buff = null;
				} else {
					ret = suport.byteToHexString(buff).substring(16, 18);
					System.out.println("ret = " + ret);
					switch (Flag) { // 根据Flag标志位判断是哪个按钮触发的监听方法
					case 1:
						if (ret.equals("00")) {
							Toast.makeText(HrfidActivity.this, "天线感应已开启",
									Toast.LENGTH_SHORT).show();
						} else if (ret.equals("01")) {
							Toast.makeText(HrfidActivity.this, "天线感应开启失败",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 2:
						if (ret.equals("00")) {
							Toast.makeText(HrfidActivity.this, "天线感应已关闭",
									Toast.LENGTH_SHORT).show();
						} else if (ret.equals("01")) {
							Toast.makeText(HrfidActivity.this, "天线感应关闭失败",
									Toast.LENGTH_SHORT).show();
						}
						break;
					case 3:
						if (ret.equals("00")) {

							String type = suport.byteToHexString(buff)
									.substring(18, 22);

							System.out.println("type= " + type);
							if (type.equals("0400")) {
								find_tv.setText("搜寻到卡  S50卡");
							}
							if (type.equals("0200")) {
								find_tv.setText("搜寻到卡  S70卡");
							}
						} else if (ret.equals("14")) {
							find_tv.setText("未搜寻到高频卡");
						}
						break;
					case 4:
						if (ret.equals("00")) {
							cardserial = suport.byteToHexString(buff)
									.substring(18, 26);
							prevent_tv.setText(cardserial);
						} else if (ret.equals("0a")) {
							prevent_tv.setText("防冲突失败");
						}
						break;
					case 5:
						if (ret.equals("00".toString())) {
							fix_tv.setText("选定卡序列号" + cardserial);
						} else if (ret.equals("0a".toString())) {
							fix_tv.setText("选卡失败");
						}
						break;
					case 6:
						if (ret.equals("00".toString())) {
							verity_tv.setText("验证成功");
						} else if (ret.equals("16".toString())) {
							verity_tv.setText("验证失败");
						}
						break;
					case 7:
						if (ret.equals("00".toString())) {
							if (suport.byteToHexString(buff).length() >= 26) {

								String balance = suport.byteToHexString(buff)
										.substring(18, 26);
								balance = suport.LHtoHL(balance);
								try {

									int_balance = Integer.parseInt(balance, 16);
									float_balance = int_balance/(float)100.0;          
									if (temp == 0) {

										read_tv.setText(float_balance
												+ "(十六进制字节码：" + balance + ")");
									}
								} catch (Exception e) {
									Toast.makeText(HrfidActivity.this,
											"余额异常，请重新初始化", Toast.LENGTH_SHORT)
											.show();
								}
							}
						} else if (ret.equals("17".toString()))
							read_tv.setText("未能成功读取卡余额");
						break;
					case 8:
						if (ret.equals("00".toString())) {
							init_tv.setText("初始化成功"
									+ init_in.getText().toString().trim());
						} else {
							init_tv.setText("初始化失败");
						}
						break;
					case 50:
						if (ret.equals("00".toString())) {
							deduct_tv.setText("扣除金额："
									+ deduct_in.getText().toString().trim());
						} else {
							deduct_tv.setText("扣款失败");
						}
						break;
					case 9:
						if (ret.equals("00".toString())) {
							add_tv.setText("充值成功");
						} else {
							add_tv.setText("充值失败");
						}
						break;
					case 10:
						if (ret.equals("00".toString())) {
							String block = suport.byteToHexString(buff)
									.substring(18, 50);
							readsec_tv.setText(block);
						} else {
							readsec_tv.setText("读扇区数据失败");
						}
						break;
					case 11:
						if (ret.equals("00".toString())) {
							writesec_tv.setText("对扇区写入内容成功");
						} else
							writesec_tv.setText("对扇区写入内容失败");
						break;
					case 12:
						if (ret.equals("00".toString())) {
							String control_data = suport.byteToHexString(buff)
									.substring(18, 50);
							readctr_data.setText(control_data);
							readctr_tv.setText("读控制块成功");
						} else {
							readctr_tv.setText("读控制块失败");
						}
						break;
					case 13:
						if (ret.equals("00".toString())) {
							wrtiectr_tv.setText("密码修改成功");
						} else
							wrtiectr_tv.setText("密码修改失败");
						break;
					}
					Flag = 0;
				}
			}
		});
	}

	// private boolean isMatches(String str) {
	// String temp;
	// for (int i = 0; i < str.length(); i++) {
	// temp = str.substring(i, i + 1);
	// if (!temp.matches("[a-f0-9A-F]")) {
	// return false;
	// }
	// break;
	// }
	// return true;
	// }

	// 发送指令
	public void send(byte[] by) {
		try {
			mOutputStream.write(by);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 对控件的初始化和设置监听方法
	private void InitWidget() {
		openwire = (Button) findViewById(R.id.open_wire);
		closewire = (Button) findViewById(R.id.close_wire);
		findcard = (Button) findViewById(R.id.find_card);
		preventconflit = (Button) findViewById(R.id.prevent_conflit);
		fixcard = (Button) findViewById(R.id.fix_card);
		veritypass = (Button) findViewById(R.id.verity_pass);
		readbalance = (Button) findViewById(R.id.read_balance);
		initbalance = (Button) findViewById(R.id.init_balance);
		deductmoney = (Button) findViewById(R.id.deduct_balance);
		recharge = (Button) findViewById(R.id.add_balance);
		read_sec = (Button) findViewById(R.id.read_sec);
		write_sec = (Button) findViewById(R.id.write_sec);
		// readctr = (Button) findViewById(R.id.readctr_sec);
		writectr = (Button) findViewById(R.id.writectr_sec);

		find_tv = (EditText) findViewById(R.id.find_ret);
		prevent_tv = (EditText) findViewById(R.id.prevent_ret);
		fix_tv = (EditText) findViewById(R.id.fix_ret);
		verity_in = (EditText) findViewById(R.id.pass_ret);
		verity_tv = (TextView) findViewById(R.id.verity_ret);
		read_tv = (EditText) findViewById(R.id.read_ret);
		init_in = (EditText) findViewById(R.id.init_money);
		init_tv = (TextView) findViewById(R.id.init_ret);
		deduct_in = (EditText) findViewById(R.id.deduct_money);
		deduct_tv = (TextView) findViewById(R.id.deduct_ret);
		add_in = (EditText) findViewById(R.id.add_money);
		add_tv = (TextView) findViewById(R.id.add_ret);
		sec_id = (EditText) findViewById(R.id.readsec_id);
		readsec_tv = (EditText) findViewById(R.id.readsec_ret);
		writesec_id = (EditText) findViewById(R.id.writesec_id);
		writesec_data = (EditText) findViewById(R.id.writesec_data);
		writesec_tv = (TextView) findViewById(R.id.write_ret);
		// readctr_id = (EditText) findViewById(R.id.readctr_id);
		// readctr_data = (EditText) findViewById(R.id.readctr_dataA);
		// readctr_tv = (TextView) findViewById(R.id.readctr_ret);
		writectr_data = (EditText) findViewById(R.id.writectr_data);
		// writectr_id = (EditText) findViewById(R.id.writectr_id);
		wrtiectr_tv = (TextView) findViewById(R.id.writectr_ret);

		openwire.setOnClickListener(this);
		closewire.setOnClickListener(this);
		findcard.setOnClickListener(this);
		preventconflit.setOnClickListener(this);
		fixcard.setOnClickListener(this);
		veritypass.setOnClickListener(this);
		readbalance.setOnClickListener(this);
		initbalance.setOnClickListener(this);
		deductmoney.setOnClickListener(this);
		recharge.setOnClickListener(this);
		read_sec.setOnClickListener(this);
		write_sec.setOnClickListener(this);
		// readctr.setOnClickListener(this);
		writectr.setOnClickListener(this);
	}

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
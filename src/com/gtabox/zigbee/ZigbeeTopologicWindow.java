/**
 * @author 鄢文
 * @name ZigbeeTopologicWindow.java
 * @brief 获取zigbee组网拓扑结构数据，通过循环遍历数据对比查找子、夫节点关系，并连线绘出拓扑图
 * @data 2015/08/10
 * */

package com.gtabox.zigbee;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.taiyuan_sensor.R;
import com.gtafe.taiyuan_sensor.SerialPortActivity;
import com.gtafe.until.ModeBusStrParse;
import com.gtafe.until.SuportMethod;


public class ZigbeeTopologicWindow extends SerialPortActivity implements OnTouchListener {

//	DrawView d;
	private RelativeLayout window_relatively;  //获得当前窗口的根布局
	private static final int NodeMaxNum = 4;  //节点最大数目（包括协调器）
	private MyNodeLayout[] nodeImgView;  //利用MyNodeLayout表示一个节点
	private RelativeLayout.LayoutParams[] nodeLayoutParams;
	private DrawView[] drawLines;
	private Button getTopoBtn;
	private TextView tv;
	private Handler handler;
	
	private Map<String, Map<String, Object>> SensorInfoMap = new HashMap<String, Map<String,Object>>();
	private boolean IsReceiveSerial = false;//是否接收串口数据标志位
//	private MyNodeLayout mylayout;
	
	public ZigbeeTopologicWindow() {
		// TODO Auto-generated constructor stub
		nodeImgView = new MyNodeLayout[NodeMaxNum];
		for(int x = 0; x < NodeMaxNum; ++x){
			nodeImgView[x] = null;
		}
		nodeLayoutParams = new RelativeLayout.LayoutParams[NodeMaxNum];
		drawLines = new DrawView[NodeMaxNum];
	}

	int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zigbee_topo_window_xml);
//		mylayout = (MyNodeLayout)findViewById(R.id.my_layout);
//		mylayout.setNodeImgAndText(R.drawable.coor_map, "协调器");
//		Log.d("TEST", "Width:" + mylayout.getWidth());
		
		
		window_relatively = (RelativeLayout)findViewById(R.id.linearLayout);
		getTopoBtn = (Button)findViewById(R.id.get_topo_btn);
		getTopoBtn.setOnClickListener(new GetTopoButtonListener());
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
//				window_relatively.removeView(tv);
//				allotImgView();
//				allotImgView(ReceiveSensorNum);
				for(int x = 0; x < ReceiveSensorNum+1; x++){
					if(nodeLayoutParams[x] == null){
						nodeImgView[x] = new MyNodeLayout(getApplicationContext(), null);
						nodeImgView[x].setOnTouchListener(ZigbeeTopologicWindow.this);
//						window_relatively.addView(nodeImgView[x]/*, nodeLayoutParams[x]*/);
						nodeLayoutParams[x] = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT
								);
						drawLines[x] = new DrawView(ZigbeeTopologicWindow.this);
						window_relatively.addView(drawLines[x]);
						//第一个定义为协调器，其它为终端节点
						if(x == 0){
							nodeLayoutParams[x].leftMargin = BlockStruct.BlockCoorStartXpos;
							nodeLayoutParams[x].topMargin = BlockStruct.BlockCoorStartYpos;
/*							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							params.leftMargin = BlockStruct.BlockCoorStartXpos;
							params.topMargin = BlockStruct.BlockCoorStartYpos;*/
							nodeImgView[x].setLayoutParams(nodeLayoutParams[x]);
							nodeImgView[x].setNodeImgAndText(R.drawable.coor_map, ModeBusStrParse.getSersorName(SensorType[x]));
							window_relatively.addView(nodeImgView[x]/*, params*/);
						}
						else{
							nodeLayoutParams[x].leftMargin = BlockStruct.BlockNodeStartXpos + (x*BlockStruct.BlockMarginRight);
							nodeLayoutParams[x].topMargin = BlockStruct.BlockNodeStartYpos;
/*							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							params.leftMargin = BlockStruct.BlockNodeStartXpos + (x*BlockStruct.BlockMarginRight);
							params.topMargin = BlockStruct.BlockNodeStartYpos;
							params.alignWithParent = true;*/
							nodeImgView[x].setLayoutParams(nodeLayoutParams[x]);
							nodeImgView[x].setNodeImgAndText(R.drawable.end_map, ModeBusStrParse.getSersorName(SensorType[x]));
							window_relatively.addView(nodeImgView[x]/*, params*/);
						}
						//这里监听每个view是否已经绘制完成
						ViewTreeObserver vto = nodeImgView[x].getViewTreeObserver();   
					    vto.addOnGlobalLayoutListener(new WidgetDrawListener(nodeImgView[x]));/*(new OnGlobalLayoutListener() {
					        @Override
					        public void onGlobalLayout() {
					        	nodeImgView[0].getViewTreeObserver().removeGlobalOnLayoutListener(null);
					        	Log.d("SIZE:", ".........." + nodeImgView[0].getHeight());
					        	Log.d("SIZE:", ".........." + nodeImgView[0].getWidth());
					        }
					    });*/
					}
				}
				//画线连接节点
//				connectNodeDrawLines();
//				connectNodeDrawLines(ReceiveSensorNum);
			};
		};
	}

	//这两个变量用于记录当按下屏幕控件的瞬间记录x y的位置
	int downPosX = 0, downPosY = 0;
//	boolean isDestroy = true;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int currentNodeView = 0;
//		Log.d("GTA", Integer.toString((int)event.getRawX()) + "," + Integer.toString((int)event.getRawY()));
		//查找当前按下的节点视图，并获得它对应的数组索引
		for(int x = 0; x < ReceiveSensorNum+1/*NodeMaxNum*/; x++){
			if(v.equals(nodeImgView[x]) == true){
				currentNodeView = x;
			}
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.d("GTA", "ACTION_DOWN");
			downPosX = (int) event.getRawX();
			downPosY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			Log.d("GTA", "ACTION_UP");
			int upPosX = (int)event.getRawX();
			int upPosY = (int)event.getRawY();
			if(Math.abs(upPosX-downPosX) < 10 && Math.abs(upPosY-downPosY) < 10){
				if(currentNodeView != 0){
//					isDestroy = false;
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), ZigbeeDetailDataWindow.class);
					intent.putExtra("SensorType", SensorType[currentNodeView]);
					Log.d("GTA", ModeBusStrParse.getSersorName(SensorType[currentNodeView]));
					startActivity(intent);
				}
				else {
					Toast.makeText(getApplicationContext(), "协调器节点不可跳转", Toast.LENGTH_SHORT)
					.show();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int movePosX = (int)event.getRawX();
			int movePosY = (int)event.getRawY();
//			Log.d("GTA", "downPosX:" + downPosX + "," + "downPosY:" + downPosY);
//			Log.d("GTA", "ACTION_MOVE" + "x:" + Math.abs(movePosX-downPosX) + ",y:" + Math.abs(movePosY-downPosY));
			//Math.abs()求一个数的绝对值，这里是判断上下左右移动的距离是否大于10，如果大于10则改变控件位置，否则控件不动
			if(Math.abs(movePosX-downPosX) > 10 || Math.abs(movePosY-downPosY) > 10){
				nodeLayoutParams[currentNodeView].leftMargin = (int)event.getRawX() - BlockStruct.BlockWidth/2;
				nodeLayoutParams[currentNodeView].topMargin = ((int)event.getRawY() - getTopoBtn.getHeight() - 75) - BlockStruct.BlockHeight/2;
				nodeImgView[currentNodeView].setLayoutParams(nodeLayoutParams[currentNodeView]);
			}
			else{
//				Log.d("GTA", "没有移动一定的距离，控件位置不改变");
			}
			break;
		case MotionEvent.ACTION_SCROLL:
			Log.d("GTA", "ACTION_SCROLL");
			break;
		default:
			break;
		}
//		connectNodeDrawLines();
		connectNodeDrawLines(ReceiveSensorNum);
		//注意返回改为true
		return true;
	}
	
	//储存节点的外形的相关属性
	private static class BlockStruct{
		public static final int BlockWidth = 50;
		public static final int BlockHeight = 50;
		public static int BlockCoorStartXpos = 350;
		public static int BlockCoorStartYpos = 50;
		public static int BlockNodeStartXpos = 100;
		public static int BlockNodeStartYpos = 200;
		public static int BlockMarginRight = 120;
		public static int BlockMarginButtom = -250;
		public void BlockStruct(){
		}
	}
	
	/**
	 * @author yanwen
	 * @name GetTopoButtonListener
	 * @breif 监听Button按钮，重写onClick方法
	 * */
	private class GetTopoButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.get_topo_btn:
/*				tv = new TextView(getApplicationContext());
				RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(300, 50);
				tvParams.leftMargin = 300;
				tvParams.topMargin = 50;
				tv.setLayoutParams(tvParams);
				tv.setTextSize(15);
				tv.setText("正在读取数据，亲稍候。。。");
				window_relatively.addView(tv);
				handler.postDelayed(thread, 5000);*/
				IsReceiveSerial = true;
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * @author yanwen
	 * @name allotImgBtn
	 * @brief 给MyNodeLayout分配内存，并指定布局大小
	 * @param void
	 * @return void
	 * */
	private void allotImgView(){
		for(int x = 0; x < NodeMaxNum; x++){
			if(nodeImgView[x] == null){
				nodeImgView[x] = new MyNodeLayout(getApplicationContext(), null);
				nodeImgView[x].setOnTouchListener(ZigbeeTopologicWindow.this);
				//设定视图的高度和宽度
				nodeLayoutParams[x] = new RelativeLayout.LayoutParams(BlockStruct.BlockWidth, BlockStruct.BlockHeight);	
				window_relatively.addView(nodeImgView[x]);
				drawLines[x] = new DrawView(ZigbeeTopologicWindow.this);
				window_relatively.addView(drawLines[x]);
			}
		}
	}
	private void allotImgView(int num){
		for(int x = 0; x < num+1; ++x){
			if(nodeImgView[x] == null){
				nodeImgView[x] = new MyNodeLayout(getApplicationContext(), null);
/*				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				nodeImgView[x].setLayoutParams(params);*/
/*				if(x == 0)
					nodeImgView[x].setNodeImgAndText(R.drawable.coor_map, "协调器节点");
				else
					nodeImgView[x].setNodeImgAndText(R.drawable.end_map, "温湿度传感器");*/
				nodeImgView[x].setOnTouchListener(ZigbeeTopologicWindow.this);
				//设定视图的高度和宽度
				nodeLayoutParams[x] = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT
						);
				window_relatively.addView(nodeImgView[x]/*, nodeLayoutParams[x]*/);
				drawLines[x] = new DrawView(ZigbeeTopologicWindow.this);
				window_relatively.addView(drawLines[x]);
			}			
		}
	}
	/**
	 * @author yanwen
	 * @name connectNodeDrawLines
	 * @brief 连接节点画线
	 * @param void
	 * @return void
	 * */
	private void connectNodeDrawLines(){
		RelativeLayout.LayoutParams coorPos = null;
		RelativeLayout.LayoutParams endPos = null;
		for(int x = 0; x < NodeMaxNum; ++x){
			if(x == 0){
//				coorPos = new RelativeLayout.LayoutParams(BlockStruct.BlockWidth,
//						BlockStruct.BlockHeight);
				coorPos = (RelativeLayout.LayoutParams) nodeImgView[x].getLayoutParams();
			}
			else{
//				endPos = new RelativeLayout.LayoutParams(BlockStruct.BlockWidth,
//						BlockStruct.BlockHeight);
				endPos = (RelativeLayout.LayoutParams) nodeImgView[x].getLayoutParams();
				
				drawLines[x].setLinePos(coorPos.leftMargin+BlockStruct.BlockWidth/2, 
						coorPos.topMargin+BlockStruct.BlockHeight/2, 
						endPos.leftMargin+BlockStruct.BlockWidth/2, 
						endPos.topMargin+BlockStruct.BlockHeight/2);
//				Log.d("GTA", Integer.toString(coorPos.leftMargin) + "," + Integer.toString(coorPos.topMargin));
				drawLines[x].invalidate();
			}
		}
	}
	private void connectNodeDrawLines(int num){
		RelativeLayout.LayoutParams coorPos = null;
		RelativeLayout.LayoutParams endPos = null;
		for(int x = 0; x < num+1; ++x){
			if(x == 0){
//				coorPos = new RelativeLayout.LayoutParams(BlockStruct.BlockWidth,
//						BlockStruct.BlockHeight);
				coorPos = (RelativeLayout.LayoutParams) nodeImgView[x].getLayoutParams();
			}
			else{
//				endPos = new RelativeLayout.LayoutParams(BlockStruct.BlockWidth,
//						BlockStruct.BlockHeight);
				endPos = (RelativeLayout.LayoutParams) nodeImgView[x].getLayoutParams();
				
				drawLines[x].setLinePos(
						coorPos.leftMargin+/*BlockStruct.BlockWidth*/ nodeImgView[0].getWidth()/2, 
						coorPos.topMargin+/*BlockStruct.BlockHeight*/ nodeImgView[0].getHeight()/2, 
						endPos.leftMargin+/*BlockStruct.BlockWidth*/ nodeImgView[x].getWidth()/2, 
						endPos.topMargin+/*BlockStruct.BlockHeight*/ nodeImgView[x].getHeight()/2
						);
//				Log.d("GTA", Integer.toString(coorPos.leftMargin) + "," + Integer.toString(coorPos.topMargin));
				drawLines[x].invalidate();
			}
		}
	}
	/**
	 * @author yanwen
	 * @brief 重写onDraw方法，实现界面绘图
	 * */
	private class DrawView extends View{

		private int startX = 0;
		private int startY = 0;
		private int stopX = 0;
		private int stopY = 0;
		private Paint paint;
		public DrawView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			paint = new Paint(Paint.DITHER_FLAG);  //创建画笔
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			canvas.drawLine(startX, startY, stopX, stopY, paint);
		}
		/**
		 * @author yanwen
		 * @name setLinePos
		 * @brief 设置线条的位置
		 * @param x 起始位置x
		 *        y 起始位置y
		 *        ex 结束位置x
		 *        ey 结束位置y
		 * @return void
		 * */
		public void setLinePos(int x, int y, int ex, int ey){
			startX = x;
			startY = y;
			stopX = ex;
			stopY = ey;
		}
	}
	
	private int ReceiveSensorNum = 0;
	private String[] SensorType = new String[50];
	//串口接收数据抽象函数
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		if(IsReceiveSerial){
			if(size < 10 || buffer[0] != 0x7e) return;//接收的数据包小于10个字节说明数据不完整，丢弃
			byte[] buf = Arrays.copyOf(buffer, size);
			SuportMethod sup = new SuportMethod();
			String bufferStr = sup.byteToHexString(buf);
			ModeBusStrParse modebus = new ModeBusStrParse(bufferStr);
			Map<String, Object> map = new HashMap<String, Object>();
			//得到传感器图片
			map.put("SensorImg", ModeBusStrParse.getSensorImg(modebus.getSensorTypes()));
			//得到传感器名字
			map.put("SensorName", ModeBusStrParse.getSersorName(modebus.getSensorTypes()));
			//得到传感器地址
			map.put("SensorAddr", modebus.getSensorAddr());
			//得到传感器数据
			map.put("SensorData", modebus.getSensorData(modebus.getSensorDataLength()));
			//将传感器数据等信息存入SensorInfoMap里面，相同的类型会覆盖
			SensorInfoMap.put(modebus.getSensorTypes(), map);
			//如果传感器数目有增加
			if(ReceiveSensorNum != SensorInfoMap.size()){
				ReceiveSensorNum = SensorInfoMap.size();//记录当前所存的传感器数目
				SensorType[0] = ModeBusStrParse.COORDINATOR_NODE;//数组0位置存放协调器
				SensorType[ReceiveSensorNum] = modebus.getSensorTypes();
				//发送handler消息
				handler.sendEmptyMessage(1);
			}
			else{
				//这里不必增加节点数目，只是更新现有节点的数据就行了
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Runnable thread = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
		}
	};
	
	protected void onResume() {
		super.onResume();
//		isDestroy = true;
	};
	/**
	 * onPause() Activity暂停时会调用此函数
	 */
	protected void onPause() {
		super.onPause();
//		if(isDestroy){
			onDestroy();
//		}
	};
	/**
	 * Activity销毁时会调用此函数
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * 视图监听，这里用于监听自定义布局是否已经绘制完成
	 * @author 鄢文
	 *
	 */
	private class WidgetDrawListener implements OnGlobalLayoutListener{
		private MyNodeLayout MyLayout;
		public WidgetDrawListener(Object obj) {
			MyLayout = (MyNodeLayout) obj;
		}
		/**
		 * 当视图绘制完成后会回调此函数，这里当控件绘制完成后进行连接
		 */
		@Override
		public void onGlobalLayout() {
			//移除上一次的监听
			MyLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//			MyLayout.getWidth();
//			MyLayout.getHeight();
//        	Log.d("SIZE:", "w.........." + MyLayout.getHeight());
//        	Log.d("SIZE:", "h.........." + MyLayout.getWidth());
			connectNodeDrawLines(ReceiveSensorNum);
		}
		
	}
	
	
}




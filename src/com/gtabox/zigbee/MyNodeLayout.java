/**
 * 继承ViewGroup自定义布局，根布局是垂直的线性布局，上面是ImageView，下面是TextView
 * @author 鄢文
 * @data 2015/08/11
 * @version v1.1
 */
package com.gtabox.zigbee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gtafe.taiyuan_sensor.R;

public class MyNodeLayout extends LinearLayout{
	private ImageView iv;
	private TextView tv;
/*	public MyNodeLayout(Context context) {
		super(context);
	}*/

	public MyNodeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		View v = LayoutInflater.from(context).inflate(R.layout.my_node_layout_xml, this);
		iv = (ImageView)v.findViewById(R.id.node_img_iv);
		tv = (TextView)v.findViewById(R.id.node_name_tv);
	}

	public void setNodeImgAndText(int resId, String text){
		tv.setText(text);
		iv.setImageResource(resId);
	}
/*	public void setNodeText(String text){
		tv.setText(text);
	}*/
}

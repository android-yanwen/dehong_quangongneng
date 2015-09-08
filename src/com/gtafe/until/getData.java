package com.gtafe.until;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class getData {
	SuportMethod sup = new SuportMethod();
	//解析温度
	public String DataTemp(byte[] by){
		String str1 = by[6]+"";
		String str2 = by[7]+"";
		int a1 = Integer.valueOf(str1.toString());
		double a2 = Double.valueOf(str2.toString()) / 100;
		double a = a1 + a2;
		DecimalFormat df = new DecimalFormat(".00");
		String str = df.format(a);
		return str;
	}
	//解析湿度
	public String DataHum(byte[] by){
		String str1 = by[4]+"";
		String str2 = by[5]+"";
		int a1 = Integer.valueOf(str1.toString());
		double a2 = Double.valueOf(str2.toString()) / 100;
		double a = a1 + a2;
		DecimalFormat df = new DecimalFormat(".00");
		String str = df.format(a);
		return str; 
	}
	
	//解析水滴
	public String DataWater(byte[] by){
		String str = ""+by[4];
//		System.out.println("str="+str);
		return str;
	}
	
	//解析光照
	public String DataLlum(byte[] by){
		String str1 = sup.byteToHexString(by).substring(8, 10); 
		String str2 = sup.byteToHexString(by).substring(10, 12); 
		int str = Integer.parseInt(str2+str1,16);
		return str+""; 
	}
	
	//解析震动
	public String DataVibration(byte[] by){
		String str = ""+by[4];
		return str;
	}
	
	//解析红外
	public String DataInfrared(byte[] by){
		String str = ""+by[4];
		return str;
	}
	
	//解析气体
	public String DataGas(byte[] by){
		String str = ""+by[4];
		return str;
	}
	//解析酒精
	public String DataAlcohol(byte[] by){
		String str = ""+by[4];
		return str;
	}
	//解析霍尔
	public String DataHall(byte[] by){
		String str = ""+by[4];
		return str;
	}
	
	//解析倾角
	public Map<String, String> DataDip_Angle(byte[] by){	
		String strx = sup.byteToHexString(by).substring(8, 12);
		String stry = sup.byteToHexString(by).substring(12, 16);
		String strz = sup.byteToHexString(by).substring(16, 20);
		String str1 = Integer.parseInt(strx, 16) + "";
		System.out.println("str1="+str1);
		String str2 = Integer.parseInt(stry, 16) + "";
		System.out.println("str2="+str2);
		String str3 = Integer.parseInt(strz, 16) + "";
		System.out.println("str3="+str3);
		Map<String,String> map = new HashMap<String, String>();
		map.put("strx", str1);
		map.put("stry", str2);
		map.put("strz", str3);
		return map;
	}
	
	//解析大气压
	public String DataPressure(byte[] by){
		String str1 = sup.byteToHexString(by).substring(22, 24); 
		String str2 = sup.byteToHexString(by).substring(20, 22); 
		String str3 = sup.byteToHexString(by).substring(18, 20); 
		String str4 = sup.byteToHexString(by).substring(16, 18); 
		long str = Integer.parseInt(str1+str2+str3+str4,16);
		return str+""; 
	}
	
	//解析大气温度
	public String DataTemp1(byte[] by){
		String str1 = sup.byteToHexString(by).substring(14, 16); 
		String str2 = sup.byteToHexString(by).substring(12, 14); 
		String str3 = sup.byteToHexString(by).substring(10, 12); 
		String str4 = sup.byteToHexString(by).substring(8, 10); 
		long str = Integer.parseInt(str1+str2+str3+str4,16);    
		return str+""; 
	}
	
	//解析超声波
	public String DataUltrasonic(byte[] by){
		String str = ""+by[4];
		return str;
	}
	
	//解析温度
	public String DataDS18b20(byte[] by){
//		String str1 = by[6]+"";
//		String str2 = by[7]+"";
//		int a1 = Integer.valueOf(str1.toString());
//		double a2 = Double.valueOf(str2.toString()) / 100;
//		double a = a1 + a2;
//		DecimalFormat df = new DecimalFormat(".00");
//		String str = df.format(a);
		String str1 = sup.byteToHexString(by).substring(8, 12);
		double aa = Integer.valueOf(str1,16) / 10;
		DecimalFormat df = new DecimalFormat(".0");
		String str = df.format(aa);
		return str;
	}
	
	public String DataPreSSure(byte[] by){
		String str1 = sup.byteToHexString(by).substring(8, 10); 
		String str2 = sup.byteToHexString(by).substring(10, 12); 
		long str = Integer.parseInt(str2+str1,16);
		return str + "";
	}
	
	//解析三轴加速
	public Map<String, String> DataAxis(byte[] by){	
		String strx = sup.byteToHexString(by).substring(8, 12);
		String stry = sup.byteToHexString(by).substring(12, 16);
		String strz = sup.byteToHexString(by).substring(16, 20);
		String str1 = Integer.parseInt(strx, 16) + "";
		String str2 = Integer.parseInt(stry, 16) + "";
		String str3 = Integer.parseInt(strz, 16) + "";
		String strx1 = sup.byteToHexString(by).substring(24, 28);
		String stry1 = sup.byteToHexString(by).substring(28, 32);
		String strz1 = sup.byteToHexString(by).substring(32, 36);
		String str4 = Integer.parseInt(strx1, 16) + "";
		String str5 = Integer.parseInt(stry1, 16) + "";
		String str6 = Integer.parseInt(strz1, 16) + "";
		Map<String,String> map = new HashMap<String, String>();
		map.put("strx", str1);
		map.put("stry", str2);
		map.put("strz", str3);
		map.put("strx1", str4);
		map.put("stry1", str5);
		map.put("strz1", str6);
		return map;
	}
}

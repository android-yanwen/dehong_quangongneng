package com.gtafe.until;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android_serialport_api.SerialPort;

public class SuportMethod {
	//char[]转换为byte[]
	public byte[] charsToBytes (char[] chars) {
	   Charset cs = Charset.forName ("UTF-8");
	   CharBuffer cb = CharBuffer.allocate (chars.length);
	   cb.put (chars);
	   cb.flip ();
	   ByteBuffer bb = cs.encode (cb);
	   return bb.array();
//		byte[] bt = new byte[50];
//		for(int x = 0 ; x < chars.length; x++){
//			bt[x] = (byte)chars[x];
//		}
//		return bt;
	}
	
	//字节数组转16进制字符串
    public String  byteToHexString(byte[] buffer){
    	StringBuilder  stringBuilder = new StringBuilder();
    	if(buffer == null || buffer.length <= 0){
    		return null;
    	}
    	for(int i = 0; i < buffer.length; i++){
    		int j = buffer[i] & 0xFF;
    		String str = Integer.toHexString(j);
    		if(str.length() < 2){
    			stringBuilder.append(0);
    		}
    		stringBuilder.append(str);
    	}
		return stringBuilder.toString();
    }  

	//16进制字符串转byte数组
    public byte[] hexStringToByteArray(String s) {
    	int len;
    	if(s == null){
    		return new byte[4];
    	}
    	else{
//    		if(s.length() % 2 == 0){
    			len = s.length();
//    		}else{
//    			len = s.length() + 1;
//    		}
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                                 + Character.digit(s.charAt(i+1), 16));
	        }
	        return data;
    	}
    }
    
    public byte XorByte(byte[] buf,int start,int end){
    	byte retBy = 0;
    	for(int i = start; i <= end; i++){
    		retBy = (byte) (retBy ^ buf[i]);
    		Log.d("ededeed","retby="+retBy);
    	}
		return retBy;
    }  
    /**
     * 补齐不足长度
     * @param strLength 长度
     * @param str 字符串
     * @return
     */
    public  String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
              sb = new StringBuffer();
              sb.append("0").append(str);// 左(前)补0
           // sb.append(str).append("0");//右(后)补0
              str = sb.toString();
              strLen = str.length();
        }
        return str;
    } 
    
    public SerialPort Choose_SerialPort(SharedPreferences sp,Editor editor,String device,String baudrate){
    	SerialPort mSerialPort = null ;
    	editor = sp.edit();        
		editor.putString("DEVICE", device);
		editor.putString("BAUDRATE", baudrate);
		editor.putString("SYSTEMID", "1");
		editor.commit();
//		Log.d("SP","DEVICE="+sp.getString("DEVICE", "none")+" BAUDRATE= "+sp.getString("BAUDRATE", "none"));
		try {
			mSerialPort = new SerialPort(new File(device),Integer.decode(baudrate) ,0);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mSerialPort;
    }
    
	//CRC16   校验位算法
	//CRC16
    public  int get_crc16 (byte[] bufData, int buflen, byte[] pcrc){
		int ret = 0;
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;

		if (buflen == 0){
			return ret;
		}
		for (i = 0; i < buflen; i++){			
			CRC ^= ((int)bufData[i] & 0x000000ff);
			for (j = 0; j < 8; j++){
				if ((CRC & 0x00000001) != 0){
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				}
				else{
					CRC >>= 1;
				}
			}
		}		
//		System.out.println("CRC"+Integer.toHexString(CRC));
		pcrc[0] = (byte)(CRC & 0x00ff);
		pcrc[1] = (byte)(CRC >> 8);
		return ret;
	}
	
	public boolean NoLength(String str,int len){
		boolean ret = false;
		if(str.length() < len){
			ret = true;
		}	
		return ret;
	}
	
	/**
	  * 判断字符串是否是整数
	  */
	 public  boolean isInteger(String value) {
	  try {
	   Integer.parseInt(value);
	   return true;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }
	 
		// 4个字节的十六进制字符串前低后高转前高后低
		public String LHtoHL(String str) {
			String str1 = str.substring(0, 2);
			String str2 = str.substring(2, 4);
			String str3 = str.substring(4, 6);
			String str4 = str.substring(6, 8);
			return str = str4 + str3 + str2 + str1;

		}
}

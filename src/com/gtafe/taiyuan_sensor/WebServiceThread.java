package com.gtafe.taiyuan_sensor;


import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import android.util.Log;

public class WebServiceThread extends Thread{
		private static final String NAMESPACE   = "http://tempuri.org/";
		private String  URL    =  null;
		private static final String UpdateRTData = "UpdateRTData";
		private int id;
		private int systemid ,sensorTypeId;
		private String value;
		private static String       SOAP_ACTION = "http://tempuri.org/UpdateRTData";
		public WebServiceThread (String URL,int id,int sensorTypeId,int systemid,String value){
			this.URL = URL;
			this.id = id;
			this.sensorTypeId = sensorTypeId;
			this.value = value;
			this.systemid = systemid;
		}
		public static String splitString(String str){
			String[] s1 = str.split("=");
			String str1 = s1[1];
			String[] s2 = str1.split(";");
			String str2 = s2[0];
			return str2;
		}
		@Override
		public void run(){
			if(URL == null){
				Log.i("tag", "URL == null");
				return;
			}
			  HttpTransportSE ht = new HttpTransportSE(URL);
			  ht.debug = true;
			  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			   SoapEnvelope.VER11);
			  SoapObject soapObject = new SoapObject(NAMESPACE, UpdateRTData);
//			  soapObject.addProperty("inductorId", id);
//			  soapObject.addProperty("strValue", value);
//			  soapObject.addProperty("schoolId", 1);
//			  soapObject.addProperty("systemId", 1);
			  soapObject.addProperty("sensorId", id);
			  soapObject.addProperty("sensorTypeId", sensorTypeId);
			  soapObject.addProperty("systemId", systemid);
			  soapObject.addProperty("collegeId", 1);
			  soapObject.addProperty("strValue", value);
			  
		 	  envelope.bodyOut = soapObject;
		      envelope.dotNet = true;
			  try {
//				  ht.call(NAMESPACE + UpdateRTData, envelope);
				  ht.call(SOAP_ACTION, envelope);
				  if (envelope.getResponse() != null){
//				    Log.i("tag", "杩炴帴鎴愬姛");
//				    SoapObject result = (SoapObject) envelope.bodyIn;
//			        String strValue = splitString(result.toString());
//			        Log.i("client", "strValue :   " + result.toString());
			      }
			  } catch (IOException e) {
			 	  e.printStackTrace();
			  } catch (XmlPullParserException e) {
				  e.printStackTrace();
			 }
		}
//		}
}
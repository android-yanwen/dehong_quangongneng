package com.gtabox.rfid;

import java.util.Arrays;

import com.gtafe.until.SuportMethod;

public class Command {
	SuportMethod sup = new SuportMethod();
	public byte[] guard_ctr(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0x7E;
		buffer[1] = (byte) 0x7E;
		buffer[2] = (byte) 0x01;
		buffer[3] = (byte) 0x05;
		buffer[4] = (byte) 0x00;
		buffer[5] = (byte) 0x01;
		buffer[6] = (byte) 0xFF;
		buffer[7] = (byte) 0x00;
		buffer[8] = (byte) 0xDD;
		buffer[9] = (byte) 0xFA;
		return buffer;
	}
	public byte[] HRFID_open_wire(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte) 0x06;
		buffer[3] = (byte) 0x00;
		buffer[4] = (byte) 0x00;
		buffer[5] = (byte) 0x00;
		buffer[6] = (byte) 0x0C;
		buffer[7] = (byte) 0x01;
		buffer[8] = (byte) 0x01;
		buffer[9] = (byte) 0x0C;
		return buffer;
	}
	public byte[] HRFID_close_wire(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x0C;
		buffer[7] = (byte)0x01;
		buffer[8] = (byte)0x00;
		buffer[9] = (byte)0x0D;
		return buffer;
	}
	public byte[] HRFID_find_card(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x01;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x52;
		buffer[9] = (byte)0x51;
		return buffer;
	}
	public byte[] HRFID_prevent_conflit(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x02;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x04;
		buffer[9] = (byte)0x04;
		return buffer;
	}
	public byte[] HRFID_fix_card(String serialid){
		byte[] buffer = new byte[13];
		byte[] by = sup.hexStringToByteArray(serialid);
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x09;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x03;
		buffer[7] = (byte)0x02;
		buffer[8] = by[0];
 		buffer[9] = by[1];
		buffer[10] = by[2];
		buffer[11] = by[3];
		buffer[12] = sup.XorByte(buffer, 6, 11);
		return buffer;
	}
	public byte[] HRFID_Verity_password(String serialid){
		byte[] buffer = new byte[17];
		byte[] by = sup.hexStringToByteArray(serialid);
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x0D;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x07;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x60;
		buffer[9] = (byte)0x03;
		buffer[10] = by[0];
		buffer[11] = by[1];
		buffer[12] = by[2];
		buffer[13] = by[3];
		buffer[14] = by[4];
		buffer[15] = by[5];
		buffer[16] = sup.XorByte(buffer, 6, 15);
		return buffer;
	}
	public byte[] HRFID_read_balance(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x0B;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x01;
		buffer[9] =  sup.XorByte(buffer, 6, 8);
		return buffer;
	}
	public byte[] HRFID_init_balance(String initbalance){
		System.out.println("initbalance"+initbalance);
		byte[] by = sup.hexStringToByteArray(initbalance);
		byte[] buffer = new byte[14];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x0A;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x0A;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x01;
		buffer[9] = by[0];
		buffer[10] = by[1];
		buffer[11] = by[2];
		buffer[12] = by[3];
		buffer[13] =  sup.XorByte(buffer, 6, 12);
		return buffer;
	}
	
	public byte[] HRFID_deduct_money(String deduction){
		byte[] by = sup.hexStringToByteArray(deduction);
		byte[] buffer = new byte[14];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x0A;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x0C;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x01;
		buffer[9] = by[0];
		buffer[10] = by[1];
		buffer[11] = by[2];
		buffer[12] = by[3];
		buffer[13] =  sup.XorByte(buffer, 6, 12);
		return buffer;
	}
	public byte[] HRFID_recharge(String money){
		System.out.println("money"+money);
		byte[] by = sup.hexStringToByteArray(money);
		byte[] buffer = new byte[14];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x0A;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x0D;
		buffer[7] = (byte)0x02;
		buffer[8] = (byte)0x01;
		buffer[9] = by[0];
		buffer[10] = by[1];
		buffer[11] = by[2];
		buffer[12] = by[3];
		buffer[13] =  sup.XorByte(buffer, 6, 12);
		return buffer;
	}
	public byte[] HRFID_read_section(String sec){
		System.out.println("sec"+sec);
		byte[] by = sup.hexStringToByteArray(sec);
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x08;
		buffer[7] = (byte)0x02;
		buffer[8] = by[0];
		buffer[9] =  sup.XorByte(buffer, 6, 8);
		return buffer;
	}
	public byte[] HRFID_write_section(String secid,String secdata){
		byte[] by = sup.hexStringToByteArray(secid);
		byte[] bydata = sup.hexStringToByteArray(secdata);
		byte[] buffer = new byte[26];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x16;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x09;
		buffer[7] = (byte)0x02;
		buffer[8] = by[0];
		buffer[9] = bydata[0];
		buffer[10] =  bydata[1];
		buffer[11] =  bydata[2];
		buffer[12] =  bydata[3];
		buffer[13] =  bydata[4];
		buffer[14] =  bydata[5];
		buffer[15] =  bydata[6];
		buffer[16] =  bydata[7];
		buffer[17] =  bydata[8];
		buffer[18] =  bydata[9];
		buffer[19] =  bydata[10];
		buffer[20] =  bydata[11];
		buffer[21] =  bydata[12];
		buffer[22] =  bydata[13];
		buffer[23] =  bydata[14];
		buffer[24] =  bydata[15];
		buffer[25] =  sup.XorByte(buffer, 6, 24);
		return buffer;
	}
	public byte[] HRFID_read_control(String sec){
		byte[] by = sup.hexStringToByteArray(sec);
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x06;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x08;
		buffer[7] = (byte)0x02;
		buffer[8] = by[0];
		buffer[9] =  sup.XorByte(buffer, 6, 8);
		return buffer;
	}
	public byte[] HRFID_write_control(String secid,String passA){
		byte[] by = sup.hexStringToByteArray(secid);
		byte[] bydata = sup.hexStringToByteArray(passA);
		byte[] buffer = new byte[26];
		buffer[0] = (byte) 0xAA;
		buffer[1] = (byte) 0xBB;
		buffer[2] = (byte)0x16;
		buffer[3] = (byte)0x00;
		buffer[4] = (byte)0x00;
		buffer[5] = (byte)0x00;
		buffer[6] = (byte)0x09;
		buffer[7] = (byte)0x02;
		buffer[8] = by[0];
		buffer[9] = bydata[0];
		buffer[10] = bydata[1];
		buffer[11] = bydata[2];
		buffer[12] = bydata[3];
		buffer[13] = bydata[4];
		buffer[14] = bydata[5];
		buffer[15] = (byte)0xFF;
		buffer[16] = (byte)0x07;
		buffer[17] = (byte)0x80;
		buffer[18] = (byte)0x69;
		buffer[19] = (byte)0xFF;
		buffer[20] = (byte) 0xFF;
		buffer[21] = (byte) 0xFF;
		buffer[22] = (byte) 0xFF;
		buffer[23] = (byte)0xFF;
		buffer[24] = (byte)0xFF;
		buffer[25] =  sup.XorByte(buffer, 6, 24);
		return buffer;
	}
	
	public byte[] URFID_read_module(){
		byte[] buffer = new byte[5];
		buffer[0] = (byte) 0x04;
		buffer[1] = (byte) 0xFF;
		buffer[2] = (byte)0x21;
		buffer[3] = (byte)0x19;
		buffer[4] = (byte)0x95;	
		return buffer;
	}
	public byte[] URFID_query_tag(){
		byte[] buffer = new byte[7];
		buffer[0] = (byte) 0x06;
		buffer[1] = (byte) 0x00;
		buffer[2] = (byte)0x01;
		buffer[3] = (byte)0x04;
		buffer[4] = (byte)0x00;	
		byte[] check = new byte[2];
		sup.get_crc16(Arrays.copyOf(buffer, 5), 5, check);
		buffer[5] = check[0];	
		buffer[6] = check[1];	
		return buffer;
	}
	public byte[] URFID_read_tag(String block,String addrstart,String wordlen,String pass,String epcid){
		byte[] by = sup.hexStringToByteArray(epcid);
		byte[] by1 = sup.hexStringToByteArray(block);
		byte[] by2 = sup.hexStringToByteArray(addrstart);
		byte[] by3 = sup.hexStringToByteArray(wordlen);
		byte[] bypass = sup.hexStringToByteArray(pass);
		byte[] buffer = new byte[21];	
		buffer[0] = (byte) 0x14;
		buffer[1] = (byte) 0x00;
		buffer[2] = (byte)0x02;
		buffer[3] = (byte)0x04;
		buffer[4] = by[0];	
		buffer[5] = by[1];	
		buffer[6] = by[2];	
		buffer[7] = by[3];	
		buffer[8] = by[4];	
		buffer[9] = by[5];	
		buffer[10] = by[6];
		buffer[11] = by[7];			
		buffer[12] = by1[0];
		buffer[13] = by2[0];
		buffer[14] = by3[0];	
		buffer[15] = bypass[0];
		buffer[16] = bypass[1];	
		buffer[17] = bypass[2];	
		buffer[18] = bypass[3];	
		byte[] check = new byte[2];
		sup.get_crc16(Arrays.copyOf(buffer, 19), 19, check);
		buffer[19] = check[0];	
		buffer[20] = check[1];	
		return buffer;	
	}
	
	public byte[] URFID_write_tagdata(String block,String addrstart,String idlen,String wordlen,
										String pass,String epcid,String data){
		int i;
		int epclen =2 * Integer.parseInt(idlen);
		int datalen =2 * Integer.parseInt(wordlen);
		String comlen = Integer.toHexString(12 + epclen + datalen);
		byte[] com = sup.hexStringToByteArray(comlen);
		byte[] byblock = sup.hexStringToByteArray(block);
		byte[] byaddr = sup.hexStringToByteArray(addrstart);
		byte[] bylendata = sup.hexStringToByteArray(wordlen);
		byte[] bylenid = sup.hexStringToByteArray(idlen);
		byte[] bypass = sup.hexStringToByteArray(pass);
		byte[] by = sup.hexStringToByteArray(epcid);
		byte[] bydata = sup.hexStringToByteArray(data);
		byte[] buffer = new byte[13 + epclen + datalen ];		
		buffer[0] = com[0];
		buffer[1] = (byte) 0x00;
		buffer[2] = (byte)0x03;
		buffer[3] = bylendata[0];
		buffer[4] = bylenid[0];	
		for(i = 0 ; i < epclen; i++){
			buffer[5 + i] = by[i];	
		}
		buffer[5 + epclen] = byblock[0];
		buffer[6 + epclen] = byaddr[0];	
		for(i = 0 ; i < datalen; i++){
			buffer[7 + epclen + i] = bydata[i];	
		}
		buffer[7 + epclen + datalen] = bypass[0];
		buffer[8 + epclen + datalen] = bypass[1];	
		buffer[9 + epclen + datalen] = bypass[2];	
		buffer[10 + epclen + datalen] = bypass[3];			
		byte[] check = new byte[2];
		sup.get_crc16(Arrays.copyOf(buffer, 11 + epclen + datalen), 11 + epclen + datalen, check);
		buffer[11 + epclen + datalen] = check[0];	
		buffer[12 + epclen + datalen] = check[1];	
		return buffer;
	}
	
	public byte[] steeing_forward(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0x7E;
		buffer[1] = (byte) 0x7E;
		buffer[2] = (byte) 0x01;
		buffer[3] = (byte) 0x05;
		buffer[4] = (byte) 0x00;
		buffer[5] = (byte) 0x01;
		buffer[6] = (byte) 0x00;
		buffer[7] = (byte) 0x00;
		buffer[8] = (byte) 0x9C;
		buffer[9] = (byte) 0x0A;

		return buffer;
	}
	
	public byte[] steeing_rever(){
		byte[] buffer = new byte[10];
		buffer[0] = (byte) 0x7E;
		buffer[1] = (byte) 0x7E;
		buffer[2] = (byte) 0x01;
		buffer[3] = (byte) 0x05;
		buffer[4] = (byte) 0x00;
		buffer[5] = (byte) 0x01;
		buffer[6] = (byte) 0xFF;
		buffer[7] = (byte) 0x00;
		buffer[8] = (byte) 0xDD;
		buffer[9] = (byte) 0xFA;

		return buffer;
	}
}

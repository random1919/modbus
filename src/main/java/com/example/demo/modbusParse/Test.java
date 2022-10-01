package com.example.demo.modbusParse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 功能描述
 *
 * @author: 杜莉莎
 * @date: 2022年09月25日 22:15
 */
public class Test {

    private static final char HexCharArr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String HexStr = "0123456789abcdef";


    public static void main(String[] ags) throws Exception {

        int length = 20;
        byte[] response =
                new byte[]
//                        {0, 3, 0, 0, 0, -89, 1, 3, -92, 50, 78, 51, 49, 56, 48, 49, 45, 0, 57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
                        {0, 4, 0, 0, 0, 63, 1, 3, 60, 52, 57, 65, 48, 83, 50, 53, 75, 0, 57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        {0, 3, 0, 0, 0, -89, 1, 3, -92, 50, 78, 51, 49, 56, 48, 49, 45, 0, 57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
//        {0, 4, 0, 0, 0, 63, 1, 3, 60, 52, 57, 65, 48, 83, 50, 53, 75, 0, 57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        {0, 3, 0, 0, 0, -89, 1, 3, -92, 50, 78, 51, 49, 56, 48, 49, 45, 0, 57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
//
//
        String res = "";
        for (int i = 5; i < response.length; i++) {
            if(Test.judge(response[i])){
                res = res + (char)response[i];
            }
        }
        System.out.println("res -- " + res);
//



        int[] result = new int[length];
        // 设备地址，功能码，数据长度校验
        if (response[1] == 03 || response[1] == 04) {
            // 获取结果
            short dataH = 0;
            short dataL = 0;
            for (int i = 0; i < length * 2; i += 2) {
                dataH = response[3 + i];
                dataH = (short) (dataH < 0 ? dataH + 256 : dataH);
                dataL = response[4 + i];
                dataL = (short) (dataL < 0 ? dataL + 256 : dataL);
                result[i / 2] = dataH * 256 + dataL;
            }
        }

        String temp = "";
        for (int i = 0; i < result.length; i++) {
            temp = temp + result[i] + "  ";
        }
        System.out.println("temp -- " + temp);




//
//        length = result.length;
//        byte[] dataBytes = new byte[length * 2];
        String val = new String();
//        for (int i = 0, j = 0; i < length * 2; i += 2, j++) {
//            dataBytes[i] = (byte) ((result[j]));
//            System.out.println((char)dataBytes[i]);
//            dataBytes[i + 1] = (byte) ((result[j]) >> 8);
//            System.out.println((char)dataBytes[i+1]);
//        }
//
//
//        for (byte dataByte : dataBytes) {
//            if (judge(dataByte)) {
//                val = val + (char) (dataByte);
//            }
//        }
//        System.out.println("val -- "+val);
//



        for (int i = 0, j = 0; i < result.length * 2; i += 2, j++) {
            byte dataByte = (byte) ((result[j]));
            if (Test.judge(dataByte)){
                val = val + (char) (dataByte);
            }
            dataByte = (byte) ((result[j]) >> 8);
            if (Test.judge(dataByte)){
                val = val + (char) (dataByte);
            }
        }

        System.out.println("val -- " + val);

    }




    public static Boolean judge(byte dataByte){
        if ((dataByte >= 35 && dataByte <= 57) || (dataByte >= 65 && dataByte <= 90) || (dataByte >= 97
                && dataByte <= 126)) {
            return true;
        }
        return false;
    }



    public static String byteArrToHex(byte[] btArr) {
        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = HexCharArr[bt>>>4 & 0xf];
            strArr[i++] = HexCharArr[bt & 0xf];
        }
        return new String(strArr);
    }



    public static byte[] hexToByteArr(String hexStr) {
        char[] charArr = hexStr.toCharArray();
        byte btArr[] = new byte[charArr.length / 2];
        int index = 0;
        for (int i = 0; i < charArr.length; i++) {
            int highBit = HexStr.indexOf(charArr[i]);
            int lowBit = HexStr.indexOf(charArr[++i]);
            btArr[index] = (byte) (highBit << 4 | lowBit);
            index++;
        }
        return btArr;
    }



    public static boolean isCrc16CheckOk(byte[] response, int len) {
        if (len>response.length) {
            return false;
        }
        //获取CRC16校验位
        int targetCrc16 = getCrc16(response, len-2);
        byte crc16H=(byte) ((targetCrc16 >> 8) % 256);
        byte crc16L=(byte) (targetCrc16 % 256);
        if (response[len-2]==crc16L&&response[len-1]==crc16H) {
            return true;
        }else {
            return false;
        }
    }


    public static int getCrc16(byte[] arr_buff, int len) {
        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        return crc;
    }


    public static String toHexArrString(byte[] bytes) {
        if (bytes==null) {
            return "null";
        }
        String msg="";
        for (int i = 0; i < bytes.length; i++) {
            msg+="0x"+String.format("%02x", bytes[i])+",";
        }
        return msg;
    }



    public static int[] splitAddress(String address){
        String addressStr = address.replaceAll("[A-Z]\\d?[A-Z]*", "");
        System.out.println(addressStr);
        if(addressStr.contains(".")){
            int[] addressArr= new int[2];
            String[] splitArr = addressStr.split("\\.");
            addressArr[0] = Integer.parseInt(splitArr[0].trim());
            addressArr[1] = Integer.parseInt(splitArr[1].trim());
            return addressArr;
        }else if (addressStr.contains("/")) {
            int[] addressArr= new int[2];
            String[] splitArr = addressStr.split("\\/");
            addressArr[0]= Integer.parseInt(splitArr[0].trim());
            addressArr[1] = Integer.parseInt(splitArr[1].trim());
            return addressArr;
        }else {
            int[] addressArr= new int[1];
            addressArr[0]=Integer.parseInt(addressStr.trim());
            return addressArr;
        }
    }

}

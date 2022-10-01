package com.example.demo.modbusParse;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ModbusTools {
    /**
     * modbus数据转对象
     *
     * @param data     串口数据
     * @param dataType 1代表16位读取2个byte数据，2代表32位读取4个byte数据
     */
    public static ModbusDataAnalyzeBean dataAnalyze(byte[] data, int dataType) {
        int readByteNum = 0;//一次要读取多少个byte
        if (dataType == 1) {
            readByteNum = 2;
        } else if (dataType > 1) {
            readByteNum = dataType * dataType;
        }

        ModbusDataAnalyzeBean modbusDataAnalyzeBean = new ModbusDataAnalyzeBean();
        modbusDataAnalyzeBean.setAddr(Integer.parseInt(getOctFromHexBytes(data, 0)));//获取地址
        modbusDataAnalyzeBean.setFuncode(Integer.parseInt(getOctFromHexBytes(data, 1)));//获取功能码
        modbusDataAnalyzeBean.setDataType(dataType);//数据类型
        int byteNum = Integer.parseInt(getOctFromHexBytes(data, 2));//统计有效byte数据个数

        ArrayList<Double> arrayListVlaue = new ArrayList();
        for (int n = 1; n < (byteNum / readByteNum) + 1; n++) {
            arrayListVlaue.add(Double.parseDouble(getOctFromHexBytes(data, 3 + readByteNum * (n - 1), 3 + readByteNum * n - 1)));//获取值
        }
        modbusDataAnalyzeBean.setValues(arrayListVlaue);//将取到的值存进返回对象
        return modbusDataAnalyzeBean;
    }

    /**
     * 对象转modbus数据
     *
     * @param modbusDataFormationBean
     * @return
     */

    public static byte[] data(ModbusDataFormationBean modbusDataFormationBean) {
        int readByteNum = 0;//一次要读取多少个byte
        if (modbusDataFormationBean.getDataType() == 1) {
            readByteNum = 2;
        } else if (modbusDataFormationBean.getDataType() > 1) {
            readByteNum = modbusDataFormationBean.getDataType() * modbusDataFormationBean.getDataType();
        }
        byte[] command = {};
        command = append(command, octInt2ByteArray(modbusDataFormationBean.getAddr(), 1));//设置地址
        command = append(command, octInt2ByteArray(modbusDataFormationBean.getFuncode(), 1)); //设置功能码
        command = append(command, octInt2ByteArray(modbusDataFormationBean.getPortNumber(), 2));//设置寄存器起始地址
        command = append(command, octInt2ByteArray(modbusDataFormationBean.getValue(), readByteNum));//设置数据值
        command = append(command, octInt2ByteArray(getCRC162Int(command, true), 2));// 设置CRC16校验

        return command;
    }


    /**
     * 取得十制数组的from~to位,并按照十六进制转化值
     *
     * @param data
     * @param from
     * @param to
     * @return
     */
    private static String getOctFromHexBytes(byte[] data, Object from, Object... to) {
        if (data != null && data.length > 0 && from != null) {
            try {
                byte[] value;
                int fromIndex = Integer.parseInt(from.toString());
                if (to != null && to.length > 0) {
                    int toIndex = Integer.parseInt(to[0].toString());
                    if (fromIndex >= toIndex || toIndex <= 0) {
                        value = Arrays.copyOfRange(data, fromIndex, fromIndex + 1);
                    } else {
                        value = Arrays.copyOfRange(data, fromIndex, toIndex + 1);
                    }
                } else {
                    value = Arrays.copyOfRange(data, fromIndex, fromIndex + 1);
                }
                if (value != null && value.length > 0) {
                    long octValue = 0L;
                    int j = -1;
                    for (int i = value.length - 1; i >= 0; i--, j++) {
                        int d = value[i];
                        if (d < 0) {
                            d += 256;
                        }
                        octValue += Math.round(d * Math.pow(16, 2 * j + 2));
                    }
                    return new Long(octValue).toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 十进制的字符串表示转成字节数组
     *
     * @param octString 十进制格式的字符串
     * @param capacity  需要填充的容量(可选)
     * @return 转换后的字节数组
     **/
    private static byte[] octInt2ByteArray(Integer oct, int... capacity) {
        return hexString2ByteArray(Integer.toHexString(oct), capacity);
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @param capacity  需要填充的容量(可选)
     * @return 转换后的字节数组
     **/
    private static byte[] hexString2ByteArray(String hexString, int... capacity) {
        hexString = hexString.toLowerCase();
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        int length = hexString.length() / 2;
        if (length < 1) {
            length = 1;
        }
        int size = length;
        if (capacity != null && capacity.length > 0 && capacity[0] >= length) {
            size = capacity[0];
        }
        final byte[] byteArray = new byte[size];
        int k = 0;
        for (int i = 0; i < size; i++) {
            if (i < size - length) {
                byteArray[i] = 0;
            } else {
                byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
                if (k + 1 < hexString.length()) {
                    byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
                    byteArray[i] = (byte) (high << 4 | low);
                } else {
                    byteArray[i] = (byte) (high);
                }
                k += 2;
            }
        }
        return byteArray;

    }

    /**
     * 连接字节流
     *
     * @return
     */
    private static byte[] append(byte[] datas, byte[] data) {
        if (datas == null) {
            return data;
        }
        if (data == null) {
            return datas;
        } else {
            return concat(datas, data);
        }
    }

    /**
     * 字节流拼接
     *
     * @param data 字节流
     * @return 拼接后的字节数组
     **/
    private static byte[] concat(byte[]... data) {
        if (data != null && data.length > 0) {
            int size = 0;
            for (int i = 0; i < data.length; i++) {
                size += data[i].length;
            }
            byte[] byteArray = new byte[size];
            int pos = 0;
            for (int i = 0; i < data.length; i++) {
                byte[] b = data[i];
                for (int j = 0; j < b.length; j++) {
                    byteArray[pos++] = b[j];
                }
            }
            return byteArray;
        }
        return null;
    }

    private static Integer getCRC162Int(byte[] bytes, Boolean flag) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
//	            CRC ^= (int) bytes[i];

            if (bytes[i] < 0) {
                CRC ^= (int) (bytes[i] + 256);
            } else {
                CRC ^= (int) bytes[i];
            }


            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) == 1) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //高低位转换，看情况使用（譬如本人这次对led彩屏的通讯开发就规定校验码高位在前低位在后，也就不需要转换高低位)
        if (flag) {
            CRC = ((CRC & 0x0000FF00) >> 8) | ((CRC & 0x000000FF) << 8);
        }
        return CRC;
    }
}


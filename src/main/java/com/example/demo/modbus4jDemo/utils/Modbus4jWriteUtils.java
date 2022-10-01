package com.example.demo.modbus4jDemo.utils;

import com.serotonin.modbus4j.Modbus;
import com.serotonin.modbus4j.msg.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * modbus4j写入数据
 *
 * @author xq
 */
public class Modbus4jWriteUtils {

    static Log log = LogFactory.getLog(Modbus4jWriteUtils.class);
    /**
     * 工厂。
     */
    static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * 获取tcpMaster
     *
     * @return
     * @throws ModbusInitException
     */
    public static ModbusMaster getMaster() throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(502);
        ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, false);
        tcpMaster.init();

        return tcpMaster;
    }


    /**
     * 写 [01 Coil Status(0x)]写一个 function ID = 5
     *
     * @param slaveId     slave的ID
     * @param writeOffset 位置
     * @param writeValue  值
     * @return 是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeCoil(int slaveId, int writeOffset, boolean writeValue)
            throws ModbusTransportException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        // 创建请求
        WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
        // 发送请求并获取响应对象
        WriteCoilResponse response = (WriteCoilResponse) tcpMaster.send(request);
        if (response.isException()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 写[01 Coil Status(0x)] 写多个 function ID = 15
     *
     * @param slaveId     slaveId
     * @param startOffset 开始位置
     * @param bdata       写入的数据
     * @return 是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeCoils(int slaveId, int startOffset, boolean[] bdata)
            throws ModbusTransportException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        // 创建请求
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
        // 发送请求并获取响应对象
        WriteCoilsResponse response = (WriteCoilsResponse) tcpMaster.send(request);
        if (response.isException()) {
            return false;
        } else {
            return true;
        }

    }

    /***
     * 写[03 Holding Register(4x)] 写一个 function ID = 6
     *
     * @param slaveId
     * @param writeOffset
     * @param writeValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegister(int slaveId, int writeOffset, short writeValue)
            throws ModbusTransportException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        // 创建请求对象
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
        WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }

    }




    /**
     * 写入[03 Holding Register(4x)]写多个 function ID=16
     *
     * @param slaveId     modbus的slaveID
     * @param startOffset 起始位置偏移量值
     * @param sdata       写入的数据
     * @return 返回是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegisters(int slaveId, int startOffset, short[] sdata)
            throws ModbusTransportException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        // 创建请求对象
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
        // 发送请求并获取响应对象
        ModbusResponse response = tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 写入数字类型的模拟量（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     *
     * @param slaveId
     * @param offset
     * @param value         写入值,Number的子类,例如写入Float浮点类型,Double双精度类型,以及整型short,int,long
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static void writeHoldingRegister(int slaveId, int offset, Number value, int dataType)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        // 类型
        BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
        tcpMaster.setValue(locator, value);
    }

    public static void main(String[] args) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//        writeStrToPlc(1,100,"test");


        try {
//            @formatter:off
//             测试01
//			boolean t01 = writeCoil(1, 0, true);
//            Boolean result = Modbus4jUtils.readCoilStatus(1, 100);
//            System.out.println("T01:" + result);


            // TODO: 2022/9/7   02写入问题
            // 测试02
//			boolean t02 = writeCoils(1, 0, new boolean[] { false, true, false});
//            Boolean v20 = Modbus4jUtils.readInputStatus(1, 0);
//            Boolean v21 = Modbus4jUtils.readInputStatus(1, 1);
//            Boolean v22 = Modbus4jUtils.readInputStatus(1, 2);
//			System.out.println("v20:" + v20);
//			System.out.println("v21:" + v21);
//			System.out.println("v22:" + v22);

            // 测试03
			short v = 4;
//			writeRegister(1, 0, v);
//            Number v30 = Modbus4jUtils.readHoldingRegister(1, 0, DataType.FOUR_BYTE_INT_SIGNED);
            //TWO_BYTE_INT_SIGNED - SIGNED 即显示一致
            Number v30 = Modbus4jUtils.readHoldingRegister(1, 100, DataType.TWO_BYTE_INT_SIGNED);
            System.out.println("v30:" + v30);

            // 测试04
            // TODO: 2022/9/7 04到底允许写入吗
//			boolean t04 = writeRegisters(1, 0, new short[] { -3, 3, 9 });
//            Number v40 = Modbus4jUtils.readHoldingRegister(1, 0, DataType.TWO_BYTE_INT_SIGNED);
//            Number v41 = Modbus4jUtils.readHoldingRegister(1, 1, DataType.TWO_BYTE_INT_SIGNED);
//            Number v42 = Modbus4jUtils.readHoldingRegister(1, 2, DataType.TWO_BYTE_INT_SIGNED);
//            System.o ut.println("v40:" + v40);
//            System.out.println("v41:" + v41);
//            System.out.println("v42:" + v42);
////            写模拟量
//            writeHoldingRegister(1, 0, 10.1f, DataType.FOUR_BYTE_FLOAT);

            //@formatter:on
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 测试程序
     *
     * @param slaveId
     * @param startOffset
     * @param value
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static boolean writeStrToPlc(int slaveId, int startOffset, String value) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 获取master
        ModbusMaster tcpMaster = getMaster();
        short[] sdata = SetString(value);
        // 创建请求对象
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
        // 发送请求并获取响应对象
        ModbusResponse response = tcpMaster.send(request);
        if (response.isException()) {
//            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }



    //工具类方法 setString（）主要进行了一下数据类型转换
    public static short[] SetString(String value) {
        byte[] bytesTemp = value.getBytes(StandardCharsets.UTF_8);
        byte[] bytes;
        if (bytesTemp.length % 2 > 0) {
            bytes = Arrays.copyOf(bytesTemp, bytesTemp.length + 1);
        } else {
            bytes = bytesTemp;
        }
        return bytesToShort(bytes);
    }

    /**
     * Byte数组转short数组
     *
     * @param bytes
     * @return
     */
    public static short[] bytesToShort(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;

    }

//    读取字符串
    /**
     *
     * @param slaveId
     * @param offset
     * @param dataType
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
//    public static String readHoldingRegisters(int slaveId, int offset, int dataType, int num)
//            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//        // 03 Holding Register类型数据读取
//        short[] data = new short[num];
//        for (int i = 0; i < num; i++) {
//            data[i] = writeHoldingRegister(slaveId, offset + i,dataType, dataType).shortValue();
//        }
//        try {
//            return GetString(data, 0, num);
//        } catch (UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

//    工具类方法getString()  类型转换
    /**
     *
     * @param src
     * @param start
     * @param len
     * @return
     */
    public static String GetString(short[] src, int start, int len) throws UnsupportedEncodingException
    {
        short[] temp = new short[len];
        for (int i = 0; i < len; i++)
        {
            temp[i] = src[i + start];
        }
        byte[] bytesTemp = shorts2Bytes(temp);
        for (int i = 0; i < bytesTemp.length; i++) {
            byte b = bytesTemp[i];
        }
        String str = new String(bytesTemp, "UTF-8");
        return str;
    }
    /**
     *
     * @param data
     * @return
     */
    public static byte [] shorts2Bytes(short [] data){
        byte[] byteValue = new byte[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byteValue[i * 2] = (byte) (data[i] & 0xff);
            byteValue[i * 2 + 1] = (byte) ((data[i] & 0xff00) >> 8);
        }
        return byteValue;
    }

}


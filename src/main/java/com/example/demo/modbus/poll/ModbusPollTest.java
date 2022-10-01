package com.example.demo.modbus.poll;


import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;


import java.net.InetAddress;

/**
 * 功能描述
 *
 * @author: 杜莉莎
 * @date: 2022年09月07日 11:22
 */
public class ModbusPollTest {


    public static void main(String[] args) {

        try {
            // 设置主机TCP参数
            TcpParameters tcpParameters = new TcpParameters();

            // 设置TCP的ip地址
            InetAddress address = InetAddress.getByName("127.0.0.1");

            // TCP参数设置ip地址
            // tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setHost(address);

            // TCP设置长连接
            tcpParameters.setKeepAlive(true);
            // TCP设置端口，这里设置是默认端口502
            tcpParameters.setPort(Modbus.TCP_PORT);

            // 创建一个主机
            ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);

            //从机地址 (站号)
            int slaveId = 1;
            //寄存器读取开始地址
            int offset = 0;
            //读取的寄存器数量
            int quantity = 1;

            try {
                if (!m.isConnected()) {
                    m.connect();// 开启连接
                }

//                m.writeMultipleCoils(slaveId,100,);
                m.writeSingleCoil(slaveId,100,true);
                boolean[] booleans = m.readCoils(slaveId, 100, 10);
                for (boolean aBoolean : booleans) {
                    System.out.println(aBoolean);
                }

//                // 读取对应从机的数据，readInputRegisters读取的写寄存器，功能码04
//                int[] registerValues = m.readHoldingRegisters(slaveId, offset, quantity);
//
//                // 控制台输出
//                for (int value : registerValues) {
//                    System.out.println("Address: " + offset++ + ", Value: " + value);
//                }

            } catch (ModbusProtocolException e) {
                e.printStackTrace();
            } catch (ModbusNumberException e) {
                e.printStackTrace();
            } catch (ModbusIOException e) {
                e.printStackTrace();
            } finally {
                try {
                    m.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}





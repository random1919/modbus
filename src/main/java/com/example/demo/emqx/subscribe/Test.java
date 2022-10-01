package com.example.demo.emqx.subscribe;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * 功能描述
 *
 * @author: 杜莉莎
 * @date: 2022年09月22日 14:18
 */
public class Test {

    public static void main(String[] args) {
        String userName = "admin";
        String password = "20001226a";
        String broker = "tcp://39.105.41.113:1883";
        String clientId = UUID.randomUUID().toString();
        MqttAsyncClient mqttAsyncClient;
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        MemoryPersistence persistence = new MemoryPersistence();

        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        //设置保留回话
        mqttConnectOptions.setCleanSession(false);
        //设置自动重连
        mqttConnectOptions.setAutomaticReconnect(true);

        try {
            mqttAsyncClient = new MqttAsyncClient(broker, clientId, persistence);
            mqttAsyncClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("报错");
        }
        System.out.println("已连接");


    }



}

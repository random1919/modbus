package com.example.demo.mqtt;


import lombok.Data;
import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.TimeUnit;

@Data
public class PushCallBack implements MqttCallback {

    public MqttAsyncClient mqttAsyncClient;

    public PushCallBack(MqttAsyncClient mqttAsyncClient){
        this.mqttAsyncClient = mqttAsyncClient;
    }
    /**
     * mqtt连接丢失时触发（不包括主动disconnect）
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("connectionLost");
        System.out.println(throwable.getMessage());
        new Thread(() -> {
            //开始重新连接
            System.out.println("start reconnection");
            try {
                while (mqttAsyncClient != null && !mqttAsyncClient.isConnected()) {
                    mqttAsyncClient.reconnect();
                    if (mqttAsyncClient.isConnected()) {
                        System.out.println("reconnect success");
                        break;
                    } else {
                        System.out.println("ltm mqtt client reconnect fail,after 5 secend reconnect");
                        TimeUnit.SECONDS.sleep(5);
                    }
                }
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * 收到订阅消息后调用
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("---------------------------");
        System.out.println("接收到的主题为：" + s);
        System.out.println("接收到的消息为：" + new String(mqttMessage.getPayload()));
    }

    /**
     * 发布消息完成后调用
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("---------------------------");
        System.out.println("广播完成");
    }
}

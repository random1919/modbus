package com.example.demo.mqtt;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttServerTest {
    //发布的主题
    public static final String TOPIC = "mqtt";

    public static void main(String[] args) throws InterruptedException {
        MqttServer mqttServer = new MqttServer();
        MqttMessage message = new MqttMessage();
        message.setQos(1);

        message.setPayload("第一次广播".getBytes());
        mqttServer.publish(TOPIC, message);
        Thread.sleep(1000);
        message.setPayload("第二次广播".getBytes());
        mqttServer.publish(TOPIC, message);
        Thread.sleep(1000);
        message.setPayload("第三次广播".getBytes());
        mqttServer.publish(TOPIC, message);
    }
}

/**
 * 发布方
 */
class MqttServer {
    //mqtt服务器默认的地址和端口号
    private static final String HOST = "tcp://39.105.41.113:1883";
    //连接MQTT的客户端ID，一般以唯一标识符表示
    private static final String CLIENTID = "server";
    //连接的用户名密码（非必需）
    private String userName = "admin";
    private String password = "20001226a";

//    private MqttClient mqttClient;
    private MqttAsyncClient mqttClient;

    //构造方法，启动mqttClient
    public MqttServer() {
        try {
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
//            mqttClient = new MqttClient(HOST, CLIENTID, new MemoryPersistence());
            mqttClient = new MqttAsyncClient(HOST, CLIENTID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(10);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            //定义回调函数
            mqttClient.setCallback(new PushCallBack(mqttClient));
            IMqttToken token = mqttClient.connect(options);
            while(!token.isComplete());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //发布主题
    public void publish(String topic, MqttMessage message) {
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException  e) {
            e.printStackTrace();
        }
    }
}


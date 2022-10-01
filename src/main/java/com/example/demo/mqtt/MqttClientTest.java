package com.example.demo.mqtt;


import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientTest {
    //订阅的主题
    public static final String TOPIC = "mqtt";

    public static void main(String[] args) {
        MyClient myClient = new MyClient();
        myClient.subscribe(TOPIC, 1);
    }
}

/**
 * 订阅方
 */
class MyClient {
    //mqtt服务器默认的地址和端口号
    public static final String HOST = "tcp://39.105.41.113:1883";

    //连接MQTT的客户端ID，一般以唯一标识符表示
    private static final String CLIENTID = "client";
    //连接的用户名密码（非必需）
    private String userName = "admin";
    private String password = "20001226a";

    private MqttAsyncClient mqttClient;
//    private MqttClient mqttClient;

    public  MyClient() {
        try {
            mqttClient = new MqttAsyncClient(HOST, CLIENTID, new MemoryPersistence());
//            mqttClient = new MqttClient(HOST, CLIENTID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(10);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            //定义回调函数
            mqttClient.setCallback(new PushCallBack(mqttClient));
            mqttClient.connect(options);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //订阅主题
    public void subscribe(String topic, int qos) {
        try {
            Thread.sleep(2000);
            mqttClient.subscribe(topic, qos);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

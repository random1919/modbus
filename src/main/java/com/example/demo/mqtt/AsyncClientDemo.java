package com.example.demo.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description: 多实例模式。
 * @author:dsy
 * @time: 2021/4/9 11:12
 */
@Slf4j
public class AsyncClientDemo {

    private String userName;
    private String password;
    private String topic;
    private int defaultQos = 1;
    // "tcp://mqtt.eclipse.org:1883";
    private String broker;
    //Every client instance that connects to an MQTT server must have a unique client identifier
    private String clientId;
    private boolean autoReconnect = true;
    //数据持久性保存方案，这个是保存在内存里面
    MemoryPersistence persistence = new MemoryPersistence();

    private MqttAsyncClient mqttAsyncClient;

    private MqttCallback mqttCallback;

    public class MyMqttCallback implements MqttCallback {

        // Notification that the connection to the server has broken: connectionLost.
        @Override
        public void connectionLost(Throwable throwable) {
            log.debug("connectionLost");
            log.debug(throwable.getMessage());
            new Thread(() -> {
                //开始重新连接
                log.error("start reconnection");
                try {
                    while (mqttAsyncClient != null && !mqttAsyncClient.isConnected()) {
                        mqttAsyncClient.reconnect();
                        if (mqttAsyncClient.isConnected()) {
                            log.info("ltm mqtt client reconnet success");
                            break;
                        } else {
                            log.info("ltm mqtt client reconnet fail,after 5 secend reconnect");
                            TimeUnit.SECONDS.sleep(5);
                        }
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
            if (mqttCallback != null) {
                mqttCallback.connectionLost(throwable);
            }
        }

        // Notification that a new message has arrived: messageArrived.
        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            log.debug("messageArrived");
            log.debug(s);
            if (mqttCallback != null) {
                mqttCallback.messageArrived(s, mqttMessage);
            }
        }

        // Notification that a message has been delivered to the server: deliveryComplete.
        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            log.debug("deliveryComplete");
            if (mqttCallback != null) {
                mqttCallback.deliveryComplete(iMqttDeliveryToken);
            }
        }
    }


    public synchronized void connect() {
        try {
            if (mqttAsyncClient != null) {
                if (mqttAsyncClient.isConnected()) {
                    mqttAsyncClient.disconnect();
                    mqttAsyncClient = null;
                }
            }
            if (clientId == null) {
                randomClientId();
            }
            mqttAsyncClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // everytime connect clean all message
            connOpts.setCleanSession(true);
            if (userName != null) {
                connOpts.setUserName(userName);
            }
            if (password != null) {
                connOpts.setPassword(password.toCharArray());
            }
            IMqttToken token = mqttAsyncClient.connect(connOpts, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("connect success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
            //注意下面代码，如果是调用connect之后立马订阅或者发送，那么必须要处理异步连接问题，可能client还没有连接上，你就调用了发送或者订阅方法，这个时候一定报错！！！
            while(!token.isComplete());
            } catch(MqttException e){
                e.printStackTrace();
            }

        }

        /*
         * @author:l
         * @time: 2021/4/9 15:52
         * @description:订阅某些主题，可以多次调用，订阅多个。
         **/
        public void subscribe (String topic,int qos){
            try {
                mqttAsyncClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void sendMeaage (String msg,int qos){
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos);
            try {
                mqttAsyncClient.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void sendMeaage (String msg){
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(defaultQos);
            try {
                mqttAsyncClient.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void disconnect () {
            try {
                mqttAsyncClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public String getUserName () {
            return userName;
        }

        public void setUserName (String userName){
            this.userName = userName;
        }

        public String getPassword () {
            return password;
        }

        public void setPassword (String password){
            this.password = password;
        }

        public String getTopic () {
            return topic;
        }

        public void setTopic (String topic){
            this.topic = topic;
        }

        public String getBroker () {
            return broker;
        }

        public void setBroker (String broker){
            this.broker = broker;
        }

        public void randomClientId () {
            clientId = UUID.randomUUID().toString();
        }


    }




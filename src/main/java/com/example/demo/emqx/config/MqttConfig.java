package com.example.demo.emqx.config;

import com.example.demo.emqx.client.MqttPushClient;
import org.springframework.boot.context.properties.ConfigurationProperties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqttConfig
 * @Description: TODO
 * @Author: liujianfu
 * @Date: 2021/08/16 14:43:39
 * @Version: V1.0
 **/
@Component
@ConfigurationProperties("spring.mqtt")
@Setter
@Getter
public class MqttConfig {

    @Autowired
    private MqttPushClient mqttPushClient;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接地址
     */
    private String hostUrl;
    /**
     * 客户Id
     */
    private String clientId;
    /**
     * 默认连接话题
     */
    private String defaultTopic;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 保持连接数
     */
    private int keepalive;



    @Bean
    public MqttPushClient getMqttPushClient() {
        mqttPushClient.connect(hostUrl, clientId, username, password, timeout, keepalive);
        // 以/#结尾表示订阅所有以test开头的主题
        mqttPushClient.subscribe(defaultTopic, 0);
        return mqttPushClient;
    }







}

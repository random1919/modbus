package com.example.demo.mqtt.tenant;

public interface TopicService {

    void defaultTopic(String tenantId);

    void createCustomTopic(String topicName,String describe);


}

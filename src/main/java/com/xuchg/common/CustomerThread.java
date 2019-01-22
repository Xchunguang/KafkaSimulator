package com.xuchg.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import com.xuchg.controller.KafkaController;
import com.xuchg.vo.KafkaConnectVO;

import javafx.application.Platform;

public class CustomerThread extends Thread{

	private KafkaConnectVO vo;
	
	private KafkaConsumer<String, String> consumer;
	private String inputTopic;
	
	public CustomerThread(KafkaConnectVO vo){
		super();
		this.vo = vo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void run() {
		String groupId = RandomUtil.getStr();
		inputTopic = vo.getTopic();
		String brokers = vo.getKafkaAddress() + ":" + vo.getPort();

		consumer = new KafkaConsumer(createConsumerConfig(brokers, groupId));
		
		//分配topic 某分区的offset
		Map<TopicPartition,OffsetAndMetadata> offsetMap = new HashMap<>();

		for(int i=0;i<vo.getPartNum();i++){
			TopicPartition curPart = new TopicPartition(inputTopic, i);
			OffsetAndMetadata curOffset = new OffsetAndMetadata(0);
			offsetMap.put(curPart,curOffset);
		}
		
		//提交offset信息
		consumer.commitSync(offsetMap);
		
		consumer.subscribe(Collections.singletonList(inputTopic));

        while (KafkaController.customerStart) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record: records) {
                String value = record.value();
                Platform.runLater(new Runnable(){

					@Override
					public void run() {
						try{
							KafkaController.jsObject.call("addMes", value);
						}catch(Exception e){
							//失败，停止消费
							KafkaController.customerStart = false;
						}
					}
                });
            }
            consumer.commitSync();
        }
	}
	
	private static Properties createConsumerConfig(String brokers, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }
}

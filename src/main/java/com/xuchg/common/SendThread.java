package com.xuchg.common;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.xuchg.controller.KafkaController;
import com.xuchg.vo.KafkaConnectVO;
import com.xuchg.window.MainWindow;

import javafx.application.Platform;

public class SendThread extends Thread{

	private KafkaConnectVO vo;
	
	public static AtomicInteger count = new AtomicInteger(0);
	
	public SendThread(KafkaConnectVO vo){
		super();
		this.vo = vo;
	}
	
	@Override
    public void run() {
		Properties props = new Properties();
		String server = vo.getKafkaAddress() + ":" + vo.getPort();
        props.put("bootstrap.servers", server);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        
        long curTime = System.currentTimeMillis();
        while(KafkaController.start) {
        	long innerTime = System.currentTimeMillis();
        	if(innerTime - curTime >= vo.getSendTime()) {
        		String value = vo.getSendInfo() ; 
        		if(value.indexOf("%d") >= 0){
        			value = value.replaceAll("%d", RandomUtil.getNum().toString());
        		}
        		if(value.indexOf("%s") >= 0){
        			value = value.replaceAll("%s", RandomUtil.getStr());
        		}
        		
        		//设定分区规则
        		int partation = count.get() % vo.getPartNum();
        		
            	ProducerRecord<String, String> record = new ProducerRecord<String, String>(vo.getTopic(),partation, count + "",value );
            	try {
            		
    				producer.send(record).get();
    				//总插入数目加1
    				count.getAndIncrement();
    				if(vo.getSendNum()!=-1&&count.get() == vo.getSendNum()) {
    					KafkaController.start = false;
    				}
    				String curValue = value;
    				Platform.runLater(new Runnable(){
						@Override
						public void run() {
							FileOper.getValue(curValue);
						}
    				});
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			} catch (ExecutionException e) {
    				e.printStackTrace();
    			}
            	
            	curTime = System.currentTimeMillis();
        	}
        	
        }
        count = new AtomicInteger(0);
        Platform.runLater(new Runnable(){
			@Override
			public void run() {
				MainWindow.stage.setTitle("kafka模拟器-已停止");
			}
		});
	}
}

package com.xuchg.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 链接信息自动保存
 * @author xuchg
 *
 */
@Getter
@Setter
public class KafkaConnectVO {

	private String kafkaAddress;
	
	private Integer port = 9092;
	
	private String topic;
	
	private Integer partNum = 1;
	
	private String sendInfo;
	
	private Integer sendNum = -1;
	
	private Integer sendTime = 1000;
	
}

package com.home.liuhao.other.activemq.config;


import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 
 * @author liuhao
 *
 */
@Configuration
public class ActiveMQConfig {
	
	@Bean
	public Queue queue() {
		return new ActiveMQQueue("liuhao.queue");
	}
	@Bean
	public Topic topic() {
		return new ActiveMQTopic("liuhao.topic");
	}

}

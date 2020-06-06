package com.home.liuhao.other.activemq.comment;

import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 消息 的 生产者
 * 
 * @author liuhao
 *
 */
@Component
@Order(4)
public class QueueProducer {
	/*
	 * @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装 private
	 * JmsMessagingTemplate jmsTemplate; // 发送消息，destination是发送到的队列，message是待发送的消息
	 * 
	 * @Scheduled(fixedDelay=3000)//每3s执行1次 public void sendMessage(Destination
	 * destination, final String message){ jmsTemplate.convertAndSend(destination,
	 * message); }
	 */

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private Queue queue;

	@Scheduled(fixedDelay = 3000) // 每3s执行1次
	public void send() {
		try {
			
			   MapMessage mapMessage = new ActiveMQMapMessage();
			   mapMessage.setString("info", "你还在睡觉");
			   
			   //this.jmsMessagingTemplate.convertAndSend(this.queue, mapMessage);
			   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }



}
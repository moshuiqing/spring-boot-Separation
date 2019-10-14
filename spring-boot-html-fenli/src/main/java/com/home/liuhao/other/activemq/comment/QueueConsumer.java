package com.home.liuhao.other.activemq.comment;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.springframework.core.annotation.Order;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消息的消费者
 * @author Administrator
 */
@Component
@Order(5)
public class QueueConsumer {  
    //使用JmsListener配置消费者监听的队列，其中Message是接收到的消息  
	@JmsListener(destination = "liuhao.queue")  
    public void receiveQueue(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			String info = mapMessage.getString("info");
			System.out.println(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
}

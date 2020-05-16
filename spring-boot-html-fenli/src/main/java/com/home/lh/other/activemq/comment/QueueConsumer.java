package com.home.lh.other.activemq.comment;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.springframework.core.annotation.Order;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.home.lh.util.Global;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息的消费者
 * @author Administrator
 */
@Component
@Order(5)
@Slf4j
public class QueueConsumer {  
	
	
	int num  = 100;
    //使用JmsListener配置消费者监听的队列，其中Message是接收到的消息  
	@JmsListener(destination = "liuhao.queue")  
    public void receiveQueue(Message message) {
		try {
			num--;
			MapMessage mapMessage = (MapMessage) message;
			String info = mapMessage.getString("info");
			log.info(info+"剩余:"+num);			
			if(num==0) {
				log.info("没有了！");
				Global.duiLeiHd.setStop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
}

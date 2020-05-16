package com.home.lh.other.activemq.comment;

import javax.jms.MapMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.home.lh.other.activemq.util.DuiLeiHd;
import com.home.lh.util.Global;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息 的 生产者
 * 
 * @author liuhao
 *
 */
@Component
@Order(4)
@Slf4j
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
	int i = 0;
	boolean flag = true;

	@Scheduled(fixedDelay = 3000) // 每3s执行1次
	public void send() {
		try {

			Global.setDuiLeiHd(new DuiLeiHd() {
				@Override
				public void setStop() {
					log.info("结束");
					flag=false;
				}
			});

			if (flag) {
				i++;
				MapMessage mapMessage = new ActiveMQMapMessage();
				mapMessage.setString("info", i + ":抢购中");
				//this.jmsMessagingTemplate.convertAndSend("liuhao.queue", mapMessage);
				//log.info("发送请求");

			}else {
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

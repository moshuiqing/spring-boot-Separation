package com.home.liuhao.other.activemq.comment;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统订阅发布
 * 
 * @author liuhao
 * 
 */
@Component
@Order(5)
@Slf4j
public class ApolloClient implements CommandLineRunner {

	@Value("${host}")
	public String host;
	
	@Value("${mqttName}")
	public String userName;
	
	@Value("${mqttPwd}")
	public String passWord;

	private static MqttClient client;
	// 本次测试订阅的主题：
	public static String topicStr;

	@Value("${topicStr}")
	public void setTopicStr(String topicStr) {
		ApolloClient.topicStr = topicStr;
	}

	@Override
	public void run(String... args) throws Exception {
		init();

	}

	public void init() throws MqttException {
		// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存
		if (client == null)
			client = new MqttClient(host, "CallbackClient", new MqttDefaultFilePersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
		// 这里设置为true表示每次连接到服务器都以新的身份连接
		options.setCleanSession(false);
		// 设置连接的用户名
		options.setUserName(userName);
		// 设置连接的密码
		options.setPassword(passWord.toCharArray());
		// 设置超时时间 单位为秒
		options.setConnectionTimeout(10);
		// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
		options.setKeepAliveInterval(20);
		// 回调
		client.setCallback(new MqttCallback() {
			public void messageArrived(String topicName, MqttMessage message) throws Exception {
				// subscribe后得到的消息会执行到这里面
				log.info("messageArrived----------");
				System.out.println(topicName + "---" + new String(message.getPayload(), "UTF-8"));
			}

			public void deliveryComplete(IMqttDeliveryToken token) {
				// publish后会执行到这里
				log.info("deliveryComplete---------" + token.isComplete());
			}

			public void connectionLost(Throwable cause) {
				// //连接丢失后，一般在这里面进行重连
				log.info("connectionLost----------");
				try {
					init();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		// 链接
		client.connect(options);
		// 订阅
		client.subscribe(topicStr, 2);    ///0  至多一次    1 至少一次  2 刚好一次
		// 取消订阅
		// client.unsubscribe(topicStr);
		
		
	}

	// 自己写发布消息的方法，然后循环调用。
	public static void PushMsg(String msg) throws UnsupportedEncodingException {
		MqttMessage m = new MqttMessage();
		m.setRetained(true);
		m.setPayload(msg.getBytes("UTF-8"));
		try {
			client.publish(topicStr, m);
			log.info("发布消息成功 -->" + msg);
		} catch (Exception e) {
			log.info("发布消息失败-->" + msg);
			e.printStackTrace();
		}

	}

}
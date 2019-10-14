/*package com.my.home.util.emalutil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

*//**
 * @author ai996 1多账号轮训发送
 *
 *//*
@Configuration
@EnableConfigurationProperties(MailProperties.class) // 讲bean转换为配置文件
public class EmailConfig extends JavaMailSenderImpl implements JavaMailSender {

	private ArrayList<String> usernameList;
	private ArrayList<String> passwordList;
	private int currentMailId = 0;

	private final MailProperties properties;

	public EmailConfig(MailProperties properties) {
		this.properties = properties;
		// 初始化账号
		if (usernameList == null) {
			usernameList = new ArrayList<>();
			String[] userNames = this.properties.getUsername().split(",");
			for (String user : userNames) {
				usernameList.add(user);
			}
		}
		// 初始化密码
		if (passwordList == null) {
			passwordList = new ArrayList<>();
			String[] passwords = this.properties.getPassword().split(",");
			if (passwords != null) {
				for (String pwd : passwords) {
					passwordList.add(pwd);
				}
			}
		}
	}

	@Override
	protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
		super.setUsername(usernameList.get(currentMailId));
		super.setPassword(passwordList.get(currentMailId));
		// 设置编码和各种参数
		super.setHost(this.properties.getHost());
		super.setDefaultEncoding(this.properties.getDefaultEncoding().name());
		super.setJavaMailProperties(asProperties(this.properties.getProperties()));

		// 轮询
		currentMailId = (currentMailId + 1) % usernameList.size();

	}

	private Properties asProperties(Map<String, String> source) {
		Properties properties = new Properties();
		properties.putAll(source);
		return properties;
	}

	@Override
	public String getUsername() {

		return usernameList.get(currentMailId);
	}

}
*/
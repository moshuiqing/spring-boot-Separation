package com.home.lh.other.emalutil;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EamilComment {
	
	
	private static final Logger logger = LoggerFactory.getLogger(EamilComment.class);


    @Autowired
    private JavaMailSender mailSender;//spring 提供的邮件发送类
    
    @Autowired
    private TemplateEngine templateEngine;  //模板引擎

    @Value("${mail.fromMail.addr}")
    private String from;
    
    /**
     * @param to
     * @param subject
     * @param content
     * 发送简单邮件
     */
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();//创建简单邮件消息
        message.setFrom(from);//设置发送人
        message.setTo(to);//设置收件人

        /* String[] adds = {"xxx@qq.com","yyy@qq.com"}; //同时发送给多人
        message.setTo(adds);*/

        message.setSubject(subject);//设置主题
        message.setText(content);//设置内容
        try {
            mailSender.send(message);//执行发送邮件
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
        }
    }
    
    
    /**
     * @param to
     * @param subject
     * @param content
     * 发送html格式邮件
     * @throws MessagingException 
     */
    public void sendHtmlEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();//创建一个MINE消息

        
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
       
    }
    
    
    /**
     * @param to
     * @param subject
     * @param content
     * @param filePath
     * 发送带附件的邮件
     */
    public void sendAttachmentsEmail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();//创建一个MINE消息

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);// true表示这个邮件是有附件的

            FileSystemResource file = new FileSystemResource(new File(filePath));//创建文件系统资源
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);//添加附件

            mailSender.send(message);
            logger.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送带附件的邮件时发生异常！", e);
        }

    }
    
    /**
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     * 发送带静态资源的邮件
     */
    public void sendInlineResourceEmail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));

            //添加内联资源，一个id对应一个资源，最终通过id来找到该资源
            helper.addInline(rscId, res);//添加多个图片可以使用多条 <img src='cid:" + rscId + "' > 和 helper.addInline(rscId, res) 来实现

            mailSender.send(message);
            logger.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送嵌入静态资源的邮件时发生异常！", e);
        }

    }
    
    public void sendTemplateMail(String email) {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("email", "刘浩");
        String emailContent = templateEngine.process("/liuhao/page/moban/test", context);

        System.out.println(emailContent);
        try {
			sendHtmlEmail(email,"主题：这是模板邮件",emailContent);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }











}

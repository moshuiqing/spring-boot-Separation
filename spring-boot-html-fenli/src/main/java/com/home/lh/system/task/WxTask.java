package com.home.lh.system.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.DocumentException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.home.lh.other.weixin.entity.WeixinGlobal;
import com.home.lh.other.weixin.util.WeiXinUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信参数定时获取类
 * @author liuhao
 *
 */
@Component("WxTask")
@Slf4j
public class WxTask {
	
	
			/*0 0 12 * * ?	每天12点触发
			0 15 10 ? * *	每天10点15分触发
			0 15 10 * * ?	每天10点15分触发
			0 15 10 * * ? *	每天10点15分触发
			0 15 10 * * ? 2005	2005年每天10点15分触发
			0 * 14 * * ?	每天下午的 2点到2点59分每分触发
			0 0/5 14 * * ?	每天下午的 2点到2点59分(整点开始，每隔5分触发)
			0 0/5 14,18 * * ?	每天下午的 2点到2点59分(整点开始，每隔5分触发)
			每天下午的 18点到18点59分(整点开始，每隔5分触发)
			0 0-5 14 * * ?	每天下午的 2点到2点05分每分触发
			0 10,44 14 ? 3 WED	3月分每周三下午的 2点10分和2点44分触发
			0 15 10 ? * MON-FRI	从周一到周五每天上午的10点15分触发
			0 15 10 15 * ?	每月15号上午10点15分触发
			0 15 10 L * ?	每月最后一天的10点15分触发
			0 15 10 ? * 6L	每月最后一周的星期五的10点15分触发
			0 15 10 ? * 6L 2002-2005	从2002年到2005年每月最后一周的星期五的10点15分触发
			0 15 10 ? * 6#3	每月的第三周的星期五开始触发
			0 0 12 1/5 * ?	每月的第一个中午开始每隔5天触发一次
			0 11 11 11 11 ?	每年的11月11号 11点11分触发(光棍节)*/
	
	
	
	//@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点整

	//@Scheduled(cron = "0 30 0 * * ?")//每天凌晨0点30分

	//@Scheduled(cron = "0 */60 * * * ?")//1小时处理一次
	@Scheduled(cron="0/10 * * * * ? ") //间隔1秒执行
	public void doJob() throws DocumentException, ClientProtocolException, IOException{
			
		WeixinGlobal global = WeiXinUtil.useGetGlobal();		
		long datetime = global.getDatetime();
		long now = System.currentTimeMillis();
		long value= (now-datetime)/1000;
		if(value>7000){
			log.info("重新获取微信的access_token和ticket");
			WeiXinUtil.setWeixinInfo();
		}else{
			//System.out.println("不需要从新获取,间隔为："+global.getAccess_token());
		}
		/*System.out.println(datetime);
		System.out.println(now);*/
		
	}

}

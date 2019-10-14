package com.home.liuhao.system.comment.wexinMsg;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.home.liuhao.other.weixin.elasticsearch.ReplyMsgSearch;
import com.home.liuhao.other.weixin.po.ReplyMsg;
import com.home.liuhao.other.weixin.service.ReplyMsgService;
import com.home.liuhao.system.po.SysUser;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(6)
@Slf4j
public class ReplyMsgComment implements CommandLineRunner {

	@Autowired
	private ReplyMsgService replyMsgService;

	@Autowired
	private ReplyMsgSearch replyMsgSearch;

	@Override
	public void run(String... args) throws Exception {
		log.info("初始化微信回答信息");

		new Thread() {
			public void run() {
				List<ReplyMsg> msgs = replyMsgService.simpleFound(new ReplyMsg());
				if (!msgs.isEmpty()) {
					for (int i = 0; i <= msgs.size(); i++) {
						msgs.get(i).setSort(i + 1);
					}

					replyMsgSearch.saveAll(msgs);

					log.info("保存成功！");

				}

			}
		}.start();

	}

}

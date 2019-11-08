package com.home.liuhao.system.comment.wexinMsg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.home.liuhao.other.weixin.elasticsearch.ReplyMsgSearch;
import com.home.liuhao.other.weixin.po.MsgStrInfo;
import com.home.liuhao.other.weixin.po.ReplyMsg;
import com.home.liuhao.other.weixin.service.ReplyMsgService;

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
				List<ReplyMsg> replyMsgs = replyMsgService.simpleFound(new ReplyMsg());

				if (replyMsgs.size() > 0) {
					List<MsgStrInfo> infos = new ArrayList<MsgStrInfo>();
					for (int i = 0; i < replyMsgs.size(); i++) {
						MsgStrInfo m = new MsgStrInfo();
						m.setId((i + 1));
						m.setAnswer(replyMsgs.get(i).getAnswer());
						m.setProblem(replyMsgs.get(i).getProblem());
						infos.add(m);
					}
					replyMsgSearch.saveAll(infos);
				}
				log.info("保存成功！");

			}
		}.start();

	}

}

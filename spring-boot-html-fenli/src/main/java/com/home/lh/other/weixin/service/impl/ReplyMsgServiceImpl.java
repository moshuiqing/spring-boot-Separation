package com.home.lh.other.weixin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.other.weixin.mapper.ReplyMsgMapper;
import com.home.lh.other.weixin.po.ReplyMsg;
import com.home.lh.other.weixin.service.ReplyMsgService;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.SimpleUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReplyMsgServiceImpl implements ReplyMsgService {

	@Autowired
	private ReplyMsgMapper msgMapper;

	@Override
	public Integer insert(ReplyMsg msg) {
		boolean flag = SimpleUtils.isEmpty(msg.getProblem(), msg.getAnswer());
		if (flag) {
			return -2;
		}
		return msgMapper.insert(msg);
	}

	@Override
	public Integer delete(ReplyMsg msg) {
		Integer result;
		if (msg.getId() == null) {
			result = -2;
		} else {

			try {
				msgMapper.delete(msg);
				result = 1;
			} catch (Exception e) {
				result = -1;
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public List<ReplyMsg> simpleFound(ReplyMsg msg) {
		log.debug("执行消息");
		log.debug("执行"+msgMapper);
		return msgMapper.simpleFound(msg);
	}

	@Override
	public Integer update(ReplyMsg msg) {

		return msgMapper.update(msg);
	}

	@Override
	public Integer pageCount(ReplyMsg msg) {

		return msgMapper.pageCount(msg);
	}

	@Override
	public List<ReplyMsg> pageFound(ReplyMsg msg, LayuiPage p) {
		return msgMapper.pageFound(msg, p.getStart(), p.getEnd());
	}

}

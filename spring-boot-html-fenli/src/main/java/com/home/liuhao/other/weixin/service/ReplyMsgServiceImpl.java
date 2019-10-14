package com.home.liuhao.other.weixin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.other.weixin.mapper.ReplyMsgMapper;
import com.home.liuhao.other.weixin.po.ReplyMsg;
import com.home.liuhao.util.LayuiPage;
import com.home.liuhao.util.systemutil.SimpleUtils;

@Service
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

package com.home.liuhao.other.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.other.chat.mapper.ChatNoticeMapper;
import com.home.liuhao.other.chat.po.ChatNotice;
import com.home.liuhao.other.chat.service.ChatNoticeService;

@Service
public class ChatNoticeServiceImpl implements ChatNoticeService {
	
	@Autowired
	private ChatNoticeMapper chatNoticeMapper;

	@Override
	public Integer insertNotice(ChatNotice notice) {
		
		return chatNoticeMapper.insertNotice(notice);
	}

	@Override
	public ChatNotice getNoticeByid(ChatNotice notice) {
		
		return chatNoticeMapper.getNoticeByid(notice);
	}

	@Override
	public List<ChatNotice> getMsgBox(String uid) {
		
		return chatNoticeMapper.getMsgBox(uid);
	}

	@Override
	public Integer updateNoticeRead(String id) {
		
		return chatNoticeMapper.updateNoticeRead(id);
	}

	@Override
	public Integer upUidRead(ChatNotice chatNotice) {
		
		return chatNoticeMapper.upUidRead(chatNotice);
	}

	@Override
	public Integer getNoReadByUid(String uid) {
		
		return chatNoticeMapper.getNoReadByUid(uid);
	}

	@Override
	public void insertsNotice(List<ChatNotice> list) {
		
		chatNoticeMapper.insertsNotice(list);
		
	}

}

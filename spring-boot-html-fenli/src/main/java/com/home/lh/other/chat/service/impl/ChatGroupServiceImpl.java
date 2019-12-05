package com.home.lh.other.chat.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.lh.other.chat.mapper.ChatGroupMapper;
import com.home.lh.other.chat.po.ChatGoup;
import com.home.lh.other.chat.po.ChatUser;
import com.home.lh.other.chat.po.GroupUser;
import com.home.lh.other.chat.service.ChatGroupService;
import com.home.lh.system.po.SysUser;
import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.SimpleUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatGroupServiceImpl implements ChatGroupService {

	@Autowired
	private ChatGroupMapper chatGroupMapper;

	@Override
	public List<ChatGoup> foundChatGroup(ChatUser chatUser) {

		return chatGroupMapper.foundChatGroup(chatUser);
	}

	@Override
	public List<ChatUser> foundGroupByid(String id) {

		return chatGroupMapper.foundGroupByid(id);
	}

	@Override
	public Integer getGroupCount(String id, String groupname) {

		return chatGroupMapper.getGroupCount(id, groupname);
	}

	@Override
	public List<ChatGoup> getPageGroup(String id, String groupname, LayuiPage page) {

		return chatGroupMapper.getPageGroup(id, groupname, page.getStart(), page.getEnd());
	}

	@Override
	public String getUidByGroupId(String id) {

		return chatGroupMapper.getUidByGroupId(id);
	}

	@Override
	public Integer addUserToGroup(String groupid, String userid) {

		return chatGroupMapper.addUserToGroup(groupid, userid);
	}

	@Override
	public ChatGoup getGroupInfoByid(String id) {

		return chatGroupMapper.getGroupInfoByid(id);
	}

	@Override
	public Integer refundGroup(GroupUser groupUser) {

		return chatGroupMapper.refundGroup(groupUser);
	}

	@Override
	public ChatGoup getChatGroupInfo(String id) {

		return chatGroupMapper.getChatGroupInfo(id);
	}

	@Override
	public Integer dissolution(String id) {

		return chatGroupMapper.dissolution(id);
	}

	@Override
	public List<ChatGoup> fondTopTen() {

		return chatGroupMapper.fondTopTen();
	}

	@Override
	@Transactional
	public JsonMap insertGroup(ChatGoup g,HttpSession session) {
		String id = UUID.randomUUID().toString();
		g.setId(id);
		Subject subject = SecurityUtils.getSubject();
		Object key = subject.getPrincipal();
		SysUser user = new SysUser();
		try {
			BeanUtils.copyProperties(user, key);
			g.setUserid(user.getId());
		} catch (Exception e) {
			log.info("错误");
		}
		boolean flag = SimpleUtils.isEmpty(g.getAvatar(), g.getContent(), g.getGroupname(), g.getUserid());
		if (flag) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}

		Integer num = chatGroupMapper.getByName(g.getGroupname());
		if (num > 0) {
			return SimpleUtils.addOruPdate(-3, null, "群名称已被使用！");
		}
		Integer result = chatGroupMapper.insertGroup(g);
		result = chatGroupMapper.addUserToGroup(id, g.getUserid());
		
		if(result>0) {
			String ids= (String) session.getAttribute("groups");
			ids=ids+id+",";
			session.setAttribute("groups", ids);
		}
		
		return SimpleUtils.addOruPdate(result, g, null);
	}

	@Override
	public Integer getByName(String groupname) {
		
		if(!groupname.isEmpty()) {
			
			return chatGroupMapper.getByName(groupname);
		}
		
		return -2;
	}

}

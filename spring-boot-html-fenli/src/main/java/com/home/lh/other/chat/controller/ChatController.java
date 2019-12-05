package com.home.lh.other.chat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.chat.po.ChatFriend;
import com.home.lh.other.chat.po.ChatGoup;
import com.home.lh.other.chat.po.ChatNotice;
import com.home.lh.other.chat.po.ChatRecord;
import com.home.lh.other.chat.po.ChatUser;
import com.home.lh.other.chat.po.FriendUser;
import com.home.lh.other.chat.po.GroupUser;
import com.home.lh.other.chat.service.ChatFriendService;
import com.home.lh.other.chat.service.ChatGroupService;
import com.home.lh.other.chat.service.ChatNoticeService;
import com.home.lh.other.chat.service.ChatUserService;
import com.home.lh.other.chat.util.FriendInfo;
import com.home.lh.system.po.Person;
import com.home.lh.system.po.SysUser;
import com.home.lh.system.service.SysUserService;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.SimpleUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/chatUser/")
@Api(value = "ChatController", tags = "聊天控制器")
@Slf4j
public class ChatController {

	@Autowired
	private ChatUserService chatUserService;

	@Autowired
	private ChatFriendService chatFriendpService;
	@Autowired
	private ChatGroupService chatGroupService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private ChatNoticeService chatNoticeService;

	@Value(value = "${webname}")
	private String webname;

	@Value(value = "${mrtx}")
	private String mrtx;

	@Value(value = "${qmrtx}")
	private String qmrtx;

	@Transactional
	@RequestMapping(value = "getChatUser", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取聊天用户", notes = "获取聊天用户")
	public JSONObject getChatUser() {
		JSONObject obj = new JSONObject();
		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		ChatUser chatUser = new ChatUser();
		Object key = subject.getPrincipal();
		SysUser user = new SysUser();
		try {
			BeanUtils.copyProperties(key, user);
			SysUser sysUser = new SysUser();
			sysUser.setId(user.getId());
			sysUser.setStatus("online");// 表示在线状态
			userService.update(sysUser);
			chatUser.setId(user.getId() + "");
		} catch (Exception e) {
			log.info("错误");
		}
		if (chatUser.getId() == null) {
			obj.put("code", 1);
			obj.put("msg", "缺少参数");
			obj.put("data", null);
			return obj;
		}

		Map<String, Object> map = new HashMap<>();
		try {
			List<ChatUser> chatUsers = chatUserService.foundChatUser(chatUser);
			List<ChatFriend> chatGroupFriends = chatFriendpService.foundChatFriend(chatUser.getId());
			List<ChatGoup> chatGoups = chatGroupService.foundChatGroup(chatUser);

			if (!chatUsers.isEmpty()) {
				for (ChatUser cu : chatUsers) {
					if (cu.getAvatar() == null || cu.getAvatar().equals("")) {
						cu.setAvatar(webname + mrtx);
					}
				}
			}
			map.put(Global.MINE, !chatUsers.isEmpty() ? chatUsers.get(0) : null);
			if (!chatGroupFriends.isEmpty()) {
				for (ChatFriend cf : chatGroupFriends) {
					if (!cf.getList().isEmpty()) {
						for (ChatUser cu : cf.getList()) {
							if (cu.getAvatar() == null || cu.getAvatar().equals("")) {
								cu.setAvatar(webname + mrtx);
							}
						}
					}

				}
			}
			map.put(Global.FRIEND, chatGroupFriends);
			if (!chatGoups.isEmpty()) {
				for (ChatGoup cg : chatGoups) {
					if (cg.getAvatar() == null || cg.getAvatar().equals("")) {
						cg.setAvatar(webname + qmrtx);
					}
				}
			}
			map.put(Global.GROUP, chatGoups);
			obj.put("code", 0);
			obj.put("msg", "");
			obj.put("data", map);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", 1);
			obj.put("msg", "错误");
			obj.put("data", null);
		}
		System.out.println(obj.toString());

		return obj;
	}

	/**
	 * 根据群id查询群成员
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getMembers", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据群id查询群成员", notes = "根据群id查询群成员")
	public String getMembers(String id) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		if (id == null || id.equals("")) {
			obj.put("code", 1);
			obj.put("msg", "系统错误");
			obj.put("data", null);
			return obj.toString();
		}
		List<ChatUser> list = chatGroupService.foundGroupByid(id);
		map.put("list", list);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("data", map);
		System.out.println(obj.toString());
		return obj.toString();

	}

	/**
	 * 获取用户状态
	 * 
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(value = "getUserStatus", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取用户状态", notes = "获取用户状态")
	public String getUserStatus(SysUser sysUser) {
		String status = "";
		List<SysUser> sysUsers = userService.simpleFound(sysUser);
		if (!sysUsers.isEmpty()) {
			String flag = sysUsers.get(0).getStatus();
			if (flag.equals("online")) {
				status = "1";
			} else {
				status = "0";
			}
		}
		return status;
	}

	/**
	 * 改变状态
	 * 
	 * @param sysUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "chageStatus", method = RequestMethod.POST)
	@ApiOperation(value = "改变状态", notes = "改变状态")
	public JsonMap chageStatus(SysUser sysUser) {

		Integer count = userService.update(sysUser);

		return SimpleUtils.addOruPdate(count, null, "");
	}

	/**
	 * 修改签名
	 * 
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(value = "changSign", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改签名", notes = "修改用户签名")
	public JsonMap changSign(SysUser sysUser) {
		Integer count = userService.update(sysUser);
		return SimpleUtils.addOruPdate(count, null, null);
	}

	/**
	 * 跳转聊天记录
	 * 
	 * @return
	 */
	@RequestMapping(value = "toRecord", method = RequestMethod.GET)
	@ApiOperation(value = "打开聊天记录", notes = "打开聊天记录")
	public String toRecord(String id, String type, Model m) {
		m.addAttribute("uid", id);
		m.addAttribute("type", type);
		return "/liuhao/page/chat/record/chatlog";
	}

	/**
	 * 获取聊天记录
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "getRecord", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取聊天记录", notes = "获取聊天记录")
	public JSONObject getRecord(ChatUser user) {
		JSONObject obj = new JSONObject();
		if (user.getType().equals("friend") || user.getType().equals("kefu")) {
			List<ChatUser> chatUsers = chatUserService.getRecord(user.getSendId(), user.getToId(), user.getType());
			obj.put("data", chatUsers);
			obj.put("msg", "");
			obj.put("code", 0);
		} else {
			List<ChatUser> chatUsers = chatUserService.getGroupRecord(user.getToId());
			obj.put("data", chatUsers);
			obj.put("msg", "");
			obj.put("code", 0);
		}

		return obj;
	}

	/**
	 * @param chatRecord
	 * @return 保存聊天记录
	 */
	@RequestMapping(value = "insertRecord", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存聊天记录", notes = "保存聊天记录")
	public JsonMap insertRecord(ChatRecord chatRecord) {
		Integer code = chatUserService.insertRecord(chatRecord);
		return SimpleUtils.addOruPdate(code, chatRecord, null);
	}

	/**
	 * 修改消息状态
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "upState", method = RequestMethod.POST)
	@ApiOperation(value = "修改消息为已读", notes = "修改消息为已读")
	public JsonMap updateState(String id) {

		Integer code = chatUserService.updateState(id);

		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param uid
	 * @return 实时获取好友状态
	 */
	@RequestMapping(value = "getFriendStatus", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getFriendStatus(String uid) {

		return null;

	}

	/**
	 * 查找好友页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toFindFriend", method = RequestMethod.GET)
	@ApiOperation(value = "进入查找好友页面", notes = "进入查找好友页面")
	public String toFindFriend() {
		return "/liuhao/page/chat/find/find";
	}

	/**
	 * @return 获取推荐好友
	 */
	@RequestMapping(value = "getRecom", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取推荐好友", notes = "获取推荐好友")
	public String getRecom(String userid) {
		List<ChatUser> chatUsers = chatUserService.getRecom(userid);
		JSONArray obj = JSONArray.fromObject(chatUsers);
		return obj.toString();
	}

	/**
	 * @return 获取推荐好友
	 */
	@RequestMapping(value = "getGroup", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取推荐好友", notes = "获取推荐好友")
	public String getGroup(String userid) {

		List<ChatGoup> chatGoups = chatGroupService.fondTopTen();
		JSONArray obj = JSONArray.fromObject(chatGoups);
		return obj.toString();
	}

	/**
	 * @param user
	 * @return 获取用户数量
	 */
	@RequestMapping(value = "getUserCount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取用户数量", notes = "获取用户数量")
	public String getUserCount(String uid, String type, String value) {
		JsonMap jm = new JsonMap();
		Map<String, Object> map = new HashMap<>();
		map.put("limit", 10);
		Integer result = null;
		if (type.equals("friend")) {
			result = chatUserService.getCount(uid, value);
			jm.setCode(0);
		} else if (type.equals("group")) {

			result = chatGroupService.getGroupCount(uid, value);
			jm.setCode(0);
		}
		if (result == null) {
			jm.setCode(-1);
			jm.setMsg("系统错误");
		}
		map.put("count", result);
		jm.setObject(map);
		return JSONObject.fromObject(jm).toString();
	}

	/**
	 * @param user
	 * @param page
	 * @return 分页查询参数
	 */
	@RequestMapping(value = "getPageChatUser", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "分页查询", notes = "分页查询")
	public String getPageChatUser(String uid, String type, String value, LayuiPage page) {
		page.setLimit(10);

		if (type.equals("friend")) {
			List<ChatUser> chatUsers = chatUserService.getPage(uid, value, page);
			JSONArray obj = JSONArray.fromObject(chatUsers);
			return obj.toString();

		} else if (type.equals("group")) {
			List<ChatGoup> chatGoups = chatGroupService.getPageGroup(uid, value, page);
			JSONArray obj = JSONArray.fromObject(chatGoups);
			return obj.toString();
		}
		return null;
	}

	/**
	 * @param notice
	 * @return 保存通知消息
	 */
	@RequestMapping(value = "getSendInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存通知消息", notes = "保存通知消息")
	public String getSendInfo(ChatNotice notice) {
		// 判断用户是否已经发送过请求通知
		notice.setRead("0");
		ChatNotice chatNotice = chatNoticeService.getNoticeByid(notice);
		if (chatNotice != null) {

			return JSONObject.fromObject(SimpleUtils.addOruPdate(-3, "", "您已发送过通知，请等待对方同意！")).toString();
		}

		Integer code = chatNoticeService.insertNotice(notice);

		return JSONObject.fromObject(SimpleUtils.addOruPdate(code, null, null)).toString();
	}

	/**
	 * @return 进入消息盒子页面
	 */
	@RequestMapping(value = "toMsgBox", method = RequestMethod.GET)
	@ApiOperation(value = " 进入消息盒子页面", notes = " 进入消息盒子页面")
	public String toMsgBox() {
		return "/liuhao/page/chat/msgbox/msgbox";
	}

	/**
	 * @param uid
	 * @return 获取用户未读的通知消息
	 */
	@RequestMapping(value = "getMsgBox", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取用户未读的通知消息", notes = "获取用户未读的通知消息")
	public String getMsgBox(String uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid == null || uid.equals("")) {
			map.put("code", -1);
			map.put("pages", 1);
			map.put("data", null);
		} else {
			List<ChatNotice> chatNotices = chatNoticeService.getMsgBox(uid);
			if (!chatNotices.isEmpty()) {
				for (ChatNotice cn : chatNotices) {
					if (cn.getUser() != null) {
						if (cn.getUser().getAvatar() == null || cn.getUser().getAvatar().equals("")) {
							cn.getUser().setAvatar(webname + mrtx);
						}
					}
				}
			}
			map.put("code", 0);
			map.put("pages", 1);
			map.put("data", chatNotices);
		}
		return JSONObject.fromObject(map).toString();
	}

	/**
	 * @param info
	 * @return 用户同意后改变消息状态，保存用户关系，并获取我的信息费通知对方
	 */
	@Transactional // 开启事物
	@RequestMapping(value = "changeMsgBox", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户同意后改变消息状态，保存用户关系，并获取我的信息通知对方", notes = "用户同意后改变消息状态，保存用户关系，并获取我的信息通知对方")
	public JsonMap changeMsgBox(FriendInfo info, String noticeId) {

		Integer code = -1;
		// 改变消息为已读
		code = chatNoticeService.updateNoticeRead(noticeId);
		FriendUser fu = new FriendUser();
		fu.setUserid(info.getFromuid());
		fu.setFriendid(info.getGroup());
		code = chatFriendpService.BindFriend(fu);// 绑定A至B
		fu.setUserid(info.getUid());
		fu.setFriendid(info.getFromgroup());
		code = chatFriendpService.BindFriend(fu);// 绑定B至A
		ChatUser chatUser = new ChatUser();
		chatUser.setId(info.getUid());
		List<ChatUser> chatUsers = chatUserService.foundChatUser(chatUser);
		if (chatUsers.isEmpty()) {
			return SimpleUtils.addOruPdate(-1, null, "系统错误！");
		} else {
			chatUser = chatUsers.get(0);
			chatUser.setGroup(info.getFromgroup());
		}
		return SimpleUtils.addOruPdate(code, chatUser, null);
	}

	/**
	 * 查询用户的未读消息数量
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "getNoReadMsgCount", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询用户的未读消息数量", notes = "查询用户的未读消息数量")
	public JsonMap getNoReadMsgCount(String userid) {

		Integer result = chatNoticeService.getNoReadByUid(userid);

		return SimpleUtils.addOruPdate(1, result, null);
	}

	/**
	 * @param notice 修改我未读的系统消息
	 */
	@RequestMapping(value = "upUidRead", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改我未读的系统消息", notes = "修改我未读的系统消息")
	public void upUidRead(ChatNotice notice) {
		chatNoticeService.upUidRead(notice);
	}

	/**
	 * @param noticeId
	 * @return 拒绝后修改状态
	 */
	@RequestMapping(value = "changeUserMsg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "拒绝后修改状态", notes = "拒绝后修改状态")
	public JsonMap changeUserMsg(String noticeId) {
		// 改变消息为已读
		Integer code = chatNoticeService.updateNoticeRead(noticeId);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param chatNotice
	 * @return 申请入群
	 */
	@RequestMapping(value = "applyGroup", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "申请入群", notes = "申请入群")
	public JsonMap applyGroup(ChatNotice chatNotice) {

		// 根据群id查询出群主
		String uid = chatGroupService.getUidByGroupId(chatNotice.getFrom_group());
		chatNotice.setUid(uid);

		chatNotice.setRead("0");
		ChatNotice Notice = chatNoticeService.getNoticeByid(chatNotice);
		if (Notice != null) {

			return SimpleUtils.addOruPdate(-3, "", "您已发送过通知，请等待对方同意！");
		}
		// 保存消息
		Integer code = chatNoticeService.insertNotice(chatNotice);
		return SimpleUtils.addOruPdate(code, uid, null);
	}

	/**
	 * @param fromgroup
	 * @param toid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "insertUserToGroup", method = RequestMethod.POST)
	@ApiOperation(value = "绑定用户和群", notes = "绑定用户和群")
	public JsonMap insertUserToGroup(String fromgroup, String toid) {

		Integer code = chatGroupService.addUserToGroup(fromgroup, toid);

		if (code > 0) {
			// 查询群的信息返回
			ChatGoup chatGoup = chatGroupService.getGroupInfoByid(fromgroup);
			return SimpleUtils.addOruPdate(code, chatGoup, null);
		}
		return SimpleUtils.addOruPdate(-3, null, "系统错误");

	}

	/**
	 * @param chatFriend
	 * @return 新增好友分组
	 */
	@RequestMapping(value = "insertFriendGroup", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增好友分组", notes = "新增好友分组")
	public JsonMap insertFriendGroup(ChatFriend chatFriend) {
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		chatFriend.setGoupUserid(user.getId() + "");
		if (chatFriend.getGroupname() == null || chatFriend.getGroupname().equals("")) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}
		Integer code = chatFriendpService.addChatFriend(chatFriend);
		return SimpleUtils.addOruPdate(code, chatFriend, null);
	}

	/**
	 * @param chatFriend
	 * @return 移除分组，并将分组里的好友移动到默认分组下
	 */
	@Transactional
	@ResponseBody
	@RequestMapping(value = "reomveChatFriend", method = RequestMethod.POST)
	@ApiOperation(value = "移除分组，并将分组里的好友移动到默认分组下", notes = "移除分组，并将分组里的好友移动到默认分组下")
	public JsonMap reomveChatFriend(ChatFriend chatFriend, String firstid) {
		List<FriendUser> friendUsers = chatFriendpService.getStrUserid(chatFriend.getId() + "");// 根据id查询
		if (!friendUsers.isEmpty()) {
			for (FriendUser f : friendUsers) {
				f.setFriendid(firstid);
			}
			try {
				chatFriendpService.addChatFriends(friendUsers);
			} catch (Exception e) {
				e.printStackTrace();
				return SimpleUtils.addOruPdate(-1, null, null);
			}
		}
		Integer code = chatFriendpService.removeChatFriend(chatFriend);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param chatFriend
	 * @return 重命名分组名
	 */
	@RequestMapping(value = "upChatFriendName", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "重命名分组名", notes = "重命名分组名")
	public JsonMap upChatFriendName(ChatFriend chatFriend) {
		Integer code = chatFriendpService.upChatFriendName(chatFriend);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 移动联系人至
	 * 
	 * @param friendUser
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping(value = "movChatFriend", method = RequestMethod.POST)
	@ApiOperation(value = "移动联系人至其他分组", notes = "移动联系人至其他分组")
	public JsonMap movChatFriend(FriendUser friendUser) {
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		// 删除
		Integer code = chatFriendpService.delFriendUid(user.getId() + "", friendUser.getUserid());
		if (code > 0) {
			code = chatFriendpService.BindFriend(friendUser);
		}
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 删除好友
	 * 
	 * @param friendUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delChatFriendUid", method = RequestMethod.POST)
	@ApiOperation(value = "删除好友", notes = "删除好友")
	public JsonMap delChatFriendUid(FriendUser friendUser) {
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		Map<String, String> map = new HashMap<String, String>();
		map.put("sendid", user.getId() + "");
		map.put("toid", friendUser.getUserid());
		// 删除
		Integer code = chatFriendpService.delFriendUid(user.getId() + "", friendUser.getUserid());
		if (code > 0) {
			code = chatFriendpService.delFriendUid(friendUser.getUserid(), user.getId() + "");
			ChatNotice chatNotice = new ChatNotice();
			chatNotice.setType("3");
			chatNotice.setUid(friendUser.getUserid());
			chatNotice.setContent(user.getUserName() + "已经不在是您的好友了！");
			getSendInfo(chatNotice);
		}
		return SimpleUtils.addOruPdate(code, JSONObject.fromObject(map), null);
	}

	/**
	 * 退出该群
	 * 
	 * @param groupUser
	 * @param groupname
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "refundGroup", method = RequestMethod.POST)
	@ApiOperation(value = "退出该群", notes = "退出该群")
	public JsonMap refundGroup(GroupUser groupUser, String groupname) {

		Integer code = chatGroupService.refundGroup(groupUser);

		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 根据id查询群信息 判断是否是群主
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getChatGroupInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据id查询群信息 判断是否是群主", notes = "根据id查询群信息 判断是否是群主")
	public JsonMap getChatGroupInfo(String id) {
		boolean flag;
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		ChatGoup chatGoup = chatGroupService.getChatGroupInfo(id);
		if (chatGoup.getUserid().equals(user.getId())) {
			flag = true;
		} else {
			flag = false;
		}
		return SimpleUtils.addOruPdate(1, flag, null);
	}

	/**
	 * @param id
	 * @return 解散群
	 */
	@Transactional
	@ResponseBody
	@RequestMapping(value = "dissolution", method = RequestMethod.POST)
	@ApiOperation(value = "解散群", notes = "解散群")
	public JsonMap dissolution(String id, String groupname) {
		Integer code = chatGroupService.dissolution(id);
		if (code > 0) {
			List<ChatUser> chatUsers = chatGroupService.foundGroupByid(id);
			List<ChatNotice> chatNotices = new ArrayList<>();
			if (!chatUsers.isEmpty()) {
				for (ChatUser c : chatUsers) {
					ChatNotice chatNotice = new ChatNotice();
					chatNotice.setType("3");
					chatNotice.setUid(c.getId());
					chatNotice.setContent(groupname + " 已被群主解散！");
					chatNotices.add(chatNotice);
				}
			}
			chatNoticeService.insertsNotice(chatNotices);
		}

		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 跳转好友资料或群页面
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "lookData", method = RequestMethod.GET)
	@ApiOperation("好友或 群资料")
	public String lookData(String id, String type, Model m) {
		String url = "";
		if (SimpleUtils.isEmpty(id, type)) {
			m.addAttribute("msg", "缺少参数");
		} else {
			if (type.equals("friend")) {
				// 好友资料
				Person p = chatUserService.fondFrendData(id);
				m.addAttribute("p", p);
				url = "/liuhao/page/chat/data/friendData";
			} else {
				// 群资料
				ChatGoup cg = chatGroupService.getGroupInfoByid(id);
				m.addAttribute("cg", cg);
				url = "/liuhao/page/chat/data/qundata";
			}

		}

		return url;
	}

	/**
	 * 创建群组
	 * 
	 * @param goup
	 * @return
	 */
	@RequestMapping(value = "insertGroup", method = RequestMethod.POST)
	@ResponseBody
	public JsonMap insertGroup(ChatGoup goup,HttpSession session) {
		return chatGroupService.insertGroup(goup,session);
	}

}

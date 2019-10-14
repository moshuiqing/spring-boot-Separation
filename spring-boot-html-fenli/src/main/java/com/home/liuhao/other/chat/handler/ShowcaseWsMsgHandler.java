package com.home.liuhao.other.chat.handler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.alibaba.fastjson.JSONObject;
import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.system.service.SysUserService;
import com.home.liuhao.util.Global;
import com.home.liuhao.util.systemutil.CloseImpl;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@TioServerMsgHandler
public class ShowcaseWsMsgHandler implements IWsMsgHandler {
	
	@Autowired
	private SysUserService service;
	



	@Override
	public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
			String clientip = httpRequest.getClientIp();
			String uid = httpRequest.getParam("id");// 用户id
			Tio.bindUser(channelContext, uid);
			channelContext.setAttribute("id", uid);
			log.info("收到来自{}的ws握手包\r\n{}", clientip, httpRequest.toString());
			return httpResponse;
	}

	@Override
	public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
			log.info("握手成功！");
			// 绑定到群组，后面会有群发
			String groups = httpRequest.getParam("groups");
			if (groups != null) {
				String[] groupIds = groups.split(",");
				for (int i = 0; i < groupIds.length; i++) {
					Tio.bindGroup(channelContext, groupIds[i]);
				}
			}

		
	}

	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		log.info("接收到bytes消息");
		
		return null;
	}

	
	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("onBeforeClose\r\n{}", channelContext);
		}
		
		if(Global.close!=null) {
			Global.close.removeCache();
		}
		
		
		WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
		if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
			int count = Tio.getAll(channelContext.tioConfig).getObj().size();
			String msg = channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线";
			log.info(msg);
			//用tio-websocket，服务器发送到客户端的Packet都是WsResponse
			//WsResponse wsResponse = WsResponse.fromText(msg, Global.CHARSET);
			//群发
			//Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);
			
			SysUser user = new SysUser();
			String uid =channelContext.getAttribute("id").toString();
			user.setId(uid);
			user.setStatus("offline");
			service.update(user);	
		}
		
		
		log.info("断开");
		
		
		return null;
	}

	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		 log.info("接收到文本消息："+text);
		 WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
			HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();// 获取websocket握手包
			if (log.isDebugEnabled()) {
				log.debug("握手包:{}", httpRequest);
			}

			log.info("收到ws消息:{}", text);

			if (Objects.equals("心跳内容", text)) {
				return null;
			}
			System.out.println(text);
			String type = null;
			String userid = null;

			if (text != null && !text.equals("心跳内容") && !text.equals("hello 连上了哦")) {
				JSONObject obj = JSONObject.parseObject(text);
				WsResponse wsResponsen = null;
				switch (obj.getString("type")) {
				case "chat":
					// 表示聊天
					JSONObject send = (JSONObject) obj.get("send");
					type = send.getString("type");
					userid = send.getString("toid");
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);

					if (type != null && type.equals("friend")) {
						// 指定发送
						Tio.sendToUser(channelContext.tioConfig, userid, wsResponsen);
					} else if (type != null && type.equals("group")) {
						Tio.sendToGroup(channelContext.tioConfig, userid, wsResponsen);
					}

					break;
				case "apply":
					type = obj.getString("usertype");
					userid = obj.getString("toid");
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);
					// 发送申请信息
					if (type != null && type.equals("friend")) {
						// 指定发送
						Tio.sendToUser(channelContext.tioConfig, userid, wsResponsen);
					} else if (type != null && type.equals("group")) {
						Tio.sendToGroup(channelContext.tioConfig, userid, wsResponsen);
					}

					break;
				case "systemagree":
					// 表示系统消息通知
					JSONObject send1 = (JSONObject) obj.get("send");
					type = send1.getString("type");
					userid = obj.getString("toid");
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);

					if (type != null && type.equals("friend")) {
						// 指定发送
						Tio.sendToUser(channelContext.tioConfig, userid, wsResponsen);
					}

					break;
				case "systemGroup":
					// 给群里发送系统消息
					String groupid = obj.getString("groupid");
					Tio.bindGroup(channelContext, groupid);
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);

					Tio.sendToGroup(channelContext.tioConfig, groupid, wsResponsen);

					break;
				case "delFriend":
					//删除好友通知
					userid = obj.getString("toid");
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);
					Tio.sendToUser(channelContext.tioConfig, userid, wsResponsen);
					break;
				case "systemGroupRefund":
					//接受群解散消息
					groupid = obj.getString("groupid");
					wsResponsen = WsResponse.fromText(text, Global.CHARSET);
					Tio.sendToGroup(channelContext.tioConfig, groupid, wsResponsen);
					break;
				case "unbindGroup":
					//解除绑定
					groupid = obj.getString("groupid");
					Tio.unbindGroup(groupid, channelContext);
					break;
				
				case "systemAddGroupId":	
					//新增群添加群id
					Tio.bindGroup(channelContext, obj.getString("groupid"));					
					break;

				}

			}

			// 返回值是要发送给客户端的内容，一般都是返回null
		return null;
	}

}

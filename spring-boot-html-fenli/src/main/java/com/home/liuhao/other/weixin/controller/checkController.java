package com.home.liuhao.other.weixin.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.liuhao.other.weixin.elasticsearch.ReplyMsgSearch;
import com.home.liuhao.other.weixin.entity.TextMessage;
import com.home.liuhao.other.weixin.po.ReplyMsg;
import com.home.liuhao.other.weixin.util.CheckoutUtil;
import com.home.liuhao.other.weixin.util.WeiXinUtil;
import com.home.liuhao.other.weixin.util.WxGlobal;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 刘浩 接收微信发来的信息
 */
@RequestMapping("/wx/")
@Controller
@Slf4j
public class checkController {

	@Autowired
	private ReplyMsgSearch replyMsgSearch;

	/**
	 * @param body
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "check", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String check(@RequestBody(required = false) String body, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String responseMessage = "success";
		// 判断是否是get请求
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		if (isGet) {// 进行验证
			String signature = request.getParameter("signature"); // 签名
			String timestamp = request.getParameter("timestamp"); // // 时间戳
			String nonce = request.getParameter("nonce"); // 随机数
			String echostr = request.getParameter("echostr"); // //随机字符创
			// ͨ验证签名
			if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {

				return echostr;
			}
		} else {

			log.info(body.toString());
			log.info("================================接收微信发来的信息=========================");
			SAXReader saxReader = new SAXReader();
			Document document = null;
			try {
				document = saxReader.read(new ByteArrayInputStream(body.toString().getBytes("UTF-8")));
			} catch (DocumentException e) {
				e.printStackTrace();
			}

			Map<String, String> map = WeiXinUtil.xmlToMap(document); // xml转map
			TextMessage textMessage = new TextMessage();
			String c = map.get("Content");
			textMessage.setFromUserName(map.get("ToUserName"));
			textMessage.setToUserName(map.get("FromUserName"));
			textMessage.setCreateTime(System.currentTimeMillis() + "");
			textMessage.setMsgType("text");
			String eventkey = map.get("EventKey");
			if (eventkey != null && !eventkey.equals("") && eventkey.length() > 8) {
				String qrscene = eventkey.substring(0, 7);
				if (qrscene.equals("qrscene")) {
					eventkey = eventkey.substring(8);
				}
				System.out.println(eventkey);
			}

			String flag;
			if (eventkey != null && eventkey.length() >= 38) {
				String redirect_uri = WxGlobal.yuming + WxGlobal.SMHD;
				redirect_uri = redirect_uri.replace("myCODE", eventkey);
				String url = WxGlobal.saoma.replace("SMHD", redirect_uri).replace("APPID", WxGlobal.APPID);
				textMessage.setContent("<a href='" + new String(url.getBytes(), "GBK") + "'>确定登录</a>");

			} else {
				textMessage = msgInfo(textMessage, eventkey, c);

			}
			responseMessage = WeiXinUtil.textMessageToXml(textMessage);
			return responseMessage;
		}
		return responseMessage;

	}

	private TextMessage msgInfo(TextMessage msg, String key, String c) {
		if (key != null && key.equals("autoChat")) {
			// 表示聊天
			List<ReplyMsg> replyMsgs = (List<ReplyMsg>) replyMsgSearch.findAll();
			String text = "欢迎光临!\n请回复以下数字:";
			String problem = "";
			for (ReplyMsg r : replyMsgs) {
				problem = r.getProblem();
				text += "\n" + r.getSort() + ":" + problem;
			}
			msg.setContent(text);
		} else if (c != null && c.equals("")) {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			boolean flag = pattern.matcher(c).matches();
			if (flag) {
				ReplyMsg replyMsg = replyMsgSearch.findReplyMsgBySort(Integer.parseInt(c));
				msg.setContent(replyMsg.getAnswer());
			}

		}

		return msg;

	}

}
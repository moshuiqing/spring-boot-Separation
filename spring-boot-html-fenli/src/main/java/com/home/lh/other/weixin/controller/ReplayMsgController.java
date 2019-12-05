package com.home.lh.other.weixin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.weixin.elasticsearch.ReplyMsgSearch;
import com.home.lh.other.weixin.po.MsgStrInfo;
import com.home.lh.other.weixin.po.ReplyMsg;
import com.home.lh.other.weixin.service.ReplyMsgService;
import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.SimpleUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/backsystem/replaymsg/")
@Api(value = "ReplayMsgController", tags = "微信信息回复控制层")
@Slf4j
public class ReplayMsgController {

	@Autowired
	private ReplyMsgService replyMsgService;

	@Autowired
	private ReplyMsgSearch replyMsgSearch;

	@RequestMapping(value = "toReplayMsg", method = RequestMethod.GET)
	@ApiOperation("进入微信信息页面")
	public String toReplayMsg() {
		log.info("进入微信信息页面");
		return "/liuhao/page/setting/replymsg/replymsg";
	}

	/**
	 * 分页查询
	 * 
	 * @param msg
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "pageFound", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation("分页查询")
	public String pageFound(ReplyMsg msg, LayuiPage p) {
		Integer count = replyMsgService.pageCount(msg);
		List<ReplyMsg> replyMsgs = replyMsgService.pageFound(msg, p);
		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", replyMsgs);
		return obj.toString();
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping(value = "addReply", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("新增")
	public JsonMap addReply(ReplyMsg msg) {
		Integer result = replyMsgService.insert(msg);
		return SimpleUtils.addOruPdate(result, null, null);

	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateReply", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("修改")
	public JsonMap updateReply(ReplyMsg msg) {
		Integer result = replyMsgService.update(msg);
		return SimpleUtils.addOruPdate(result, null, null);
	}

	/**
	 * 更新缓存数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateSearchReply", method = RequestMethod.POST)
	@ApiOperation("更新问题答案缓存数据")
	public JsonMap updateSearchReply() {
		Integer result = -1;
		try {
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
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SimpleUtils.addOruPdate(result, null, null);

	}

}

/**
 * /** 金鸽公司源代码，版权归金鸽公司所有。<br>
 * 项目名称 : LYGYZD
 */

package com.home.liuhao.other.liucheng.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.liuhao.other.liucheng.Globalvariable;
import com.home.liuhao.other.liucheng.po.Flow;
import com.home.liuhao.other.liucheng.po.FlowNode;
import com.home.liuhao.other.liucheng.service.FlowService;
import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.util.JsonMap;
import com.home.liuhao.util.LayuiPage;
import com.home.liuhao.util.systemutil.DateUtils;
import com.home.liuhao.util.systemutil.ShiroUtils;
import com.home.liuhao.util.systemutil.SimpleUtils;

import net.sf.json.JSONObject;

/**
 * 概要说明 : 流程. <br>
 * 详细说明 : 流程. <br>
 * 创建时间 : 2018年6月8日 上午9:18:01 <br>
 * 
 * @author by mfg
 */
@RequestMapping("/backsystem/flow/")
@Controller
public class FlowController {
	/**
	 * 接口
	 */
	@Autowired
	private FlowService flowService;

	/**
	 * 
	 * 概要说明 : 流程列表. <br>
	 * 详细说明 : 流程列表. <br>
	 *
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#flowList()
	 * @author by mfg @ 2018年6月8日, 上午9:22:40
	 */
	@RequestMapping("flowList")
	public String flowList() {
		return "/liuhao/page/setting/flow/flowList";
	}

	/**
	 * 
	 * 概要说明 : 查询流程. <br>
	 * 详细说明 : 查询流程. <br>
	 *
	 * @param f    0
	 * @param page 0
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#getflowList()
	 * @author by mfg @ 2018年6月8日, 上午9:33:17
	 */
	@RequestMapping("getflowList")
	@ResponseBody
	public String getflowList(Flow f, LayuiPage page) {
		JSONObject obj = new JSONObject();
		Integer count = flowService.pageCount(f);
		List<Flow> flows = flowService.pageFound(f, page);
		obj.put("count", count);
		obj.put("data", flows);
		obj.put("code", 0);
		obj.put("msg", "");
		return obj.toString();

	}

	/**
	 * 
	 * 概要说明 : 添加流程. <br>
	 * 详细说明 : 添加流程. <br>
	 *
	 * @param f       0
	 * @param session 0
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#addflow()
	 * @author by mfg @ 2018年6月8日, 上午10:31:01
	 */
	@RequestMapping(value = "addflow", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String addflow(Flow f, HttpSession session) {
		SysUser user = ShiroUtils.getUser();
		f.setCreater(user.getId());
		Integer result = flowService.insert(f);

		return SimpleUtils.backMsg(result);
	}

	/**
	 * 
	 * 概要说明 : 删除流程. <br>
	 * 详细说明 : 删除流程. <br>
	 *
	 * @param r       0
	 * @param session 0
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#delflow()
	 * @author by mfg @ 2018年6月11日, 上午11:13:43
	 */
	@RequestMapping(value = "delflow", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String delflow(Flow r, HttpSession session) {
		Integer result = flowService.delete(r);

		return SimpleUtils.backMsg(result);
	}

	/**
	 * 
	 * 概要说明 : 激活停用流程. <br>
	 * 详细说明 : 激活停用流程. <br>
	 *
	 * @param r       0
	 * @param session 0
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#updateflow()
	 * @author by mfg @ 2018年6月11日, 下午3:14:41
	 */
	@RequestMapping(value = "updateflow", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String updateflow(Flow r, HttpSession session) {
	
		SysUser sysUser = ShiroUtils.getUser();
		r.setCreater(sysUser.getId());
		Integer result = flowService.isEnble(r);
		return SimpleUtils.backMsg(result);
	}

	/**
	 * 概要说明 :进入设计界面 <br>
	 * 详细说明 :进入设计界面 <br>
	 *
	 * @param f 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#sheji()
	 * @author by liuhao @ 2018年7月19日, 下午2:59:59
	 */
	@RequestMapping("sheji")
	public String sheji(Flow f, Model m) {

		//String flowId = f.getFlowId()+"";
	/*	if (f == null || (flowId).equals("")) {
			return "/portal/flow/flowList";
		} else {*/
			m.addAttribute("flowId", f.getFlowId());

		//}

		String style = "";
		JsonMap reMap = new JsonMap();
		FlowNode fn = new FlowNode();
		fn.setFlowId(f.getFlowId() + "");
		List<FlowNode> flowProcesses = flowService.founNode(fn);

		for (FlowNode jds : flowProcesses) {
			style = jds.getStyle().replaceAll(",", ";").replace("{", "").replace("}", "").replaceAll("\"", "");
			jds.setStyle(style);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("total", flowProcesses.size());
		map.put("list", flowProcesses);

		reMap.setObject(map);
		System.out.println(map);

		m.addAttribute("remap", reMap);

		return "/liuhao/page/setting/flow/sheji/liucheng";
	}

	// //////////////////////////////////////////流程节点设计////////////////////////////////////////////

	/**
	 * 概要说明 : 新增节点 <br>
	 * 详细说明 : 新增节点 <br>
	 *
	 * @param l       实体
	 * @param session session
	 * @return ReturnMap 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#addJdx()
	 * @author by liuhao @ 2018年7月19日, 下午4:48:21
	 */
	@RequestMapping("addJdx")
	@ResponseBody
	public JsonMap addJdx(FlowNode l, HttpSession session) {
		
		SysUser sysUser = ShiroUtils.getUser();
		l.setCreater(sysUser.getId());
		JsonMap reMap = new JsonMap();
		String value = Globalvariable.DEFAULTYANGSHI.replaceAll("\\\\", "");
		JSONObject defaultYangShi = JSONObject.fromObject(value);
		defaultYangShi.put("left", l.getSetleft() + "px");
		defaultYangShi.put("top", l.getSettop() + "px");

		l.setStyle(defaultYangShi.toString());

		l.setProcessname(Globalvariable.LFPNAME);
		l.setCreater(sysUser.getId());
		Integer count = flowService.insertNode(l);

		FlowNode fn = new FlowNode();
		fn.setFlowId(l.getFlowId());
		fn.setId(l.getId());
		fn.setNodeCode((DateUtils.getStrDate2(new Date())) + (l.getId() + ""));
		Integer c = flowService.saveNodeByid(fn);

		if (count > 0 && c > 0) {
			l.setStyle(l.getStyle().replaceAll(",", ";").replace("{", "").replace("}", "").replaceAll("\"", ""));

			reMap.setCode(1);
			reMap.setObject(l);

		} else {
			reMap.setCode(-1);
		}
		return reMap;

	}

	/**
	 * 概要说明 : 批量保存 <br>
	 * 详细说明 : 批量保存 <br>
	 *
	 * @param flowId      流程id
	 * @param processinfo 节点信息
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#saveLfp()
	 * @author by liuhao @ 2018年7月19日, 下午5:35:45
	 */
	@RequestMapping(value = "saveLfp", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String saveLfp(String flowId, String processinfo, HttpSession session) {
		
		SysUser sysUser = ShiroUtils.getUser();

		List<FlowNode> list = new ArrayList<FlowNode>();
		System.out.println(processinfo);
		JSONObject obj = JSONObject.fromObject(processinfo);
		System.out.println(obj);
		String msg = "";
		String key = "";
		int left = 0;
		int top = 0;
		String pro = "";
		String pt = "";
		String style;
		FlowNode j = null;
		JSONObject object = null;
		FlowNode lfp = null;
		@SuppressWarnings("rawtypes")
		Iterator iterator = obj.keys();
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			JSONObject o = (JSONObject) obj.get(key);
			left = Integer.parseInt(o.getString("left"));
			top = Integer.parseInt(o.getString("top"));
			pro = (String) o.getString("process_to");
			pt = pro.replace("[", "").replace("]", "").replaceAll("\"", "");
			j = new FlowNode(Integer.parseInt(key), flowId);
			style = flowService.foundStyle(j);
			lfp = new FlowNode();
			if (style != null) {

				object = JSONObject.fromObject(style);
				object.put("left", left + "px");
				object.put("top", top + "px");
				lfp.setId(Integer.parseInt(key));
				lfp.setFlowId(flowId);
				lfp.setSetleft(left + "");
				lfp.setSettop(top + "");
				lfp.setProcessto(pt);
				lfp.setStyle(object.toString());
				lfp.setUpdater(sysUser.getId());

			}
			list.add(lfp);
		}
		try {
			flowService.saveFlowNode(list);
			msg = "1";
		} catch (Exception e) {
			msg = "2";
			e.printStackTrace();
		}

		return msg;

	}

	/**
	 * 概要说明 : 删除节点 <br>
	 * 详细说明 : 删除节点 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#deleteNode()
	 * @author by liuhao @ 2018年7月20日, 上午8:44:46
	 */
	@RequestMapping(value = "deleteNode", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String deleteNode(FlowNode fn) {

		Integer result = flowService.deleteNode(fn);

		return result + "";

	}

	/**
	 * 概要说明 : 打开属性 <br>
	 * 详细说明 :打开属性 <br>
	 *
	 * @param fn 实体
	 * @param m  数据盒子
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#toShuXin()
	 * @author by liuhao @ 2018年7月20日, 上午9:19:30
	 */
	@RequestMapping("shuxin")
	public String toShuXin(FlowNode fn, Model m) {
		m.addAttribute("flag", fn.getFlag());
		boolean result = SimpleUtils.isEmpty(fn.getFlag(), fn.getId() + "", fn.getFlowId());

		if (result) {

			m.addAttribute("msg", "-1");

		} else {

			FlowNode flowNode = flowService.foundNodeById(fn);
			FlowNode j = new FlowNode();
			j.setFlowId(fn.getFlowId());
			List<FlowNode> list = flowService.founNode(fn);
			String style = flowNode.getStyle();

			JSONObject obj = JSONObject.fromObject(style);
			obj.put("left", fn.getSetleft());
			obj.put("top", fn.getSettop());
			flowNode.setSetleft(fn.getSetleft());
			flowNode.setSettop(fn.getSettop());
			flowNode.setStyleWidth(obj.getString("width").replace("px", ""));
			flowNode.setStyleHeight(obj.getString("height").replace("px", ""));
			flowNode.setStyleColor(obj.getString("color"));

			for (FlowNode node : list) {
				if (node.getId() == (fn.getId())) {
					list.remove(node);
					break;
				}
			}

			if (flowNode.getProcessto() != null && !flowNode.getProcessto().equals("")) {
				String[] processtos = flowNode.getProcessto().split(",");
				for (FlowNode jdxs : list) {
					for (int i = 0; i < processtos.length; i++) {

						if ((jdxs.getId() + "").equals(processtos[i])) {
							jdxs.setState(1);
						}
					}

				}
			}
			m.addAttribute("lfp", flowNode);
			m.addAttribute("list", list);

		}
		return "/liuhao/page/setting/flow/sheji/shuxin";

	}

	/**
	 * 概要说明 : 保存 <br>
	 * 详细说明 : 保存 <br>
	 *
	 * @param node 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#saveNodeInfo()
	 * @author by liuhao @ 2018年7月20日, 上午11:38:45
	 */
	@RequestMapping(value = "saveNodeInfo", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String saveNodeInfo(FlowNode l, HttpSession session) {
		
		SysUser sysUser = ShiroUtils.getUser();
		l.setUpdater(sysUser.getId());
		if (l.getProcessto() == null) {
			l.setProcessto("");
		}

		FlowNode jdx = new FlowNode();
		if (l.getFlowId() == null || l.getFlowId().equals("") || l.getId() == null) {
			jdx.setMsg("缺少节点id和流程id");
		} else {
			JSONObject obj = JSONObject.fromObject(l.getStyle());
			obj.put("width", l.getStyleWidth() + "px");
			obj.put("height", l.getStyleHeight() + "px");
			obj.put("color", l.getStyleColor());
			obj.put("left", l.getSetleft() + "px");
			obj.put("top", l.getSettop() + "px");
			l.setStyle(obj.toString());
			jdx.setId(l.getId());
			Integer count = flowService.saveNodeByid(l);

			// System.out.println(l.getProcesstoCopy() + "**" + l.getStyle() + "**" +
			// l.getIcon());
			if (count > 0) {
				jdx.setProcessname(l.getProcessname());
				jdx.setProcessto(l.getProcessto());
				jdx.setStyle(l.getStyle().replace("\"", "").replace(",", ";").replace("{", "").replace("}", ""));
				jdx.setIcon(l.getIcon());

				jdx.setProcesstoCopy(l.getProcesstoCopy());

				jdx.setMsg("保存成功！");
			} else {
				jdx.setMsg("保存失败！");
			}

		}
		JSONObject jdxobj = JSONObject.fromObject(jdx);
		return jdxobj.toString();
	}

	/**
	 * 概要说明 : 保存连接线 <br>
	 * 详细说明 : 保存连接线 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#saveLianJieXian()
	 * @author by liuhao @ 2018年7月20日, 下午3:58:28
	 */
	@RequestMapping(value = "saveLianJieXian", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String saveLianJieXian(FlowNode fn, String proto) {

		FlowNode flowNode = flowService.foundNodeById(fn);

		String processto = flowNode.getProcessto();
		if (processto != null && processto.indexOf(proto) != -1) {
			return "2";
		}
		fn.setProcessto(processto + ',' + proto);
		Integer result = flowService.saveNodeByid(fn);

		return result + "";

	}

	/**
	 * 概要说明 : 查询备注 <br>
	 * 详细说明 : 查询备注 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#getRark()
	 * @author by liuhao @ 2018年7月23日, 上午11:35:01
	 */
	@RequestMapping(value = "getRark", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getRark(FlowNode fn) {
		FlowNode flowNode = flowService.foundNodeById(fn);
		if (flowNode != null && flowNode.getMark() != null && !flowNode.getMark().equals("")) {
			return flowNode.getMark();
		}
		return "没有备注！请设置！";

	}

	/**
	 * 概要说明 : 判断编码是否重复 <br>
	 * 详细说明 : 判断编码是否重复<br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#foundCode()
	 * @author by liuhao @ 2018年7月23日, 上午11:34:54
	 */
	@RequestMapping(value = "foundCode", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String foundCode(FlowNode fn) {

		List<FlowNode> flowNodes = flowService.foundCode(fn);
		if (flowNodes.size() == 0) {

			return "1";
		} else if (flowNodes.size() == 1 && flowNodes.get(0).getId() == fn.getId()) {
			return "1";
		} else {
			return "-1";
		}

	}

	// /////////////////////////

	/**
	 * 概要说明 : 进入流程字典页面 <br>
	 * 详细说明 : 进入流程字典页面 <br>
	 *
	 * @param flowId 流程id
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#proDictionary()
	 * @author by liuhao @ 2018年7月23日, 下午2:42:55
	 */
	@RequestMapping("proDictionary")
	public String proDictionary(String flowId, Model m) {

		m.addAttribute("flowId", flowId);
		return "/liuhao/page/setting/flow/lczidian";

	}

	/**
	 * 概要说明 : 分页查询流程节点 <br>
	 * 详细说明 : 分页查询流程节点 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#listNode()
	 * @author by liuhao @ 2018年7月23日, 下午2:58:37
	 */
	@RequestMapping("listNode")
	@ResponseBody
	public String listNode(FlowNode fn, LayuiPage page) {
		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		if (fn.getFlowId() == "") {
			obj.put("msg", "参数为空");
		}
		Integer count = flowService.foundCount(fn);
		List<FlowNode> flowNodes = flowService.foundPage(fn, page);
		obj.put("count", count);
		obj.put("data", flowNodes);
		return obj.toString();
	}

	/**
	 * 概要说明 : 根据id查询<br>
	 * 详细说明 : 根据id查询 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#getNodeById()
	 * @author by liuhao @ 2018年7月24日, 上午8:20:51
	 */
	@RequestMapping("getNodeById")
	@ResponseBody
	public FlowNode getNodeById(FlowNode fn) {

		FlowNode flowNode = flowService.foundNodeById(fn);

		return flowNode;
	}

	/**
	 * 概要说明 : 字典新增或修改 <br>
	 * 详细说明 : 字典新增或修改 <br>
	 *
	 * @param fn 实体
	 * @return String 类型返回值说明
	 * @see com.jinge.portal.controller.FlowController#zidianInsert()
	 * @author by liuhao @ 2018年7月23日, 下午5:47:46
	 */
	@RequestMapping(value = "zidianInsert", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String zidianInsert(FlowNode fn, HttpSession session) {
		
		SysUser sysUser = ShiroUtils.getUser();

		Integer result = null;
		if (fn.getId() != null && !fn.getId().equals("")) {
			fn.setUpdater(sysUser.getId());
			result = flowService.saveNodeByid(fn);
		} else {
			fn.setCreater(sysUser.getId());
			fn.setSetleft(Globalvariable.LEFT);
			fn.setSettop(Globalvariable.TOP);
			String value = Globalvariable.DEFAULTYANGSHI.replaceAll("\\\\", "");
			JSONObject defaultYangShi = JSONObject.fromObject(value);
			defaultYangShi.put("left", fn.getSetleft() + "px");
			defaultYangShi.put("top", fn.getSettop() + "px");
			fn.setStyle(defaultYangShi.toString());
			result = flowService.insertzdNode(fn);

		}

		if (fn.getPd().equals("2")) {
			// 默认编码
			FlowNode f = new FlowNode();
			f.setId(fn.getId());
			f.setFlowId(fn.getFlowId());
			fn.setNodeCode((DateUtils.getStrDate2(new Date())) + (fn.getId() + ""));
			flowService.saveNodeByid(fn);
		}

		return SimpleUtils.backMsg(result);

	}
}

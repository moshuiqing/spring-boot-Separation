package com.home.lh.other.compression.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.compression.CompressionVideo;
import com.home.lh.other.compression.util.Screenshots;
import com.home.lh.system.po.Syslog;
import com.home.lh.system.service.SysLogService;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.ExportExcelUtil;

import io.swagger.annotations.ApiOperation;

/**
 * 压缩
 * 
 * @author liuhao
 *
 */
@Controller
@RequestMapping("/ys/")
public class YaSuoVideo {

	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private CompressionVideo compressionVideo;

	@RequestMapping(value = "toYaSuo", method = RequestMethod.GET)
	@ApiOperation("进入视频转码界面")
	public String toYaSuo() {
		return "/liuhao/page/setting/systemtool/ysvideo";

	}

	@RequestMapping(value = "ysVideo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("视频转码界面")
	public JsonMap ysVideo(String path) {
		JsonMap jm = new JsonMap();

		String hz = path.substring(path.lastIndexOf(".") + 1, path.length());
		if (hz.equalsIgnoreCase("mp4")) {
			jm.setMsg("不需要转码");
		} else {
			String savePath = path.substring(0, path.lastIndexOf("/") + 1);
			String rename = "zm" + UUID.randomUUID() + ".mp4";
			String putfile = Global.ftpPath + path;
			String outfile = Global.ftpPath + savePath + rename;
			boolean flag = compressionVideo.processMp4(putfile, outfile, "rotate=90");
			if (flag) {
				jm.setObject(savePath + rename);
				jm.setCode(1);
				jm.setMsg("转码成功！");
			} else {
				jm.setCode(-1);
				jm.setMsg("转码失败");
			}
		}
		return jm;
	}

	/**
	 * 视频截图
	 * 
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "spJieTu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("视频截图")
	public JsonMap spJieTu(String path) {
		JsonMap jm = new JsonMap();
		String jdlj = Global.ftpPath + path;

		try {
			String imgurl = path.substring(0, path.lastIndexOf("/") + 1);
			imgurl = Screenshots.getThumbnai(jdlj, imgurl);
			jm.setObject(imgurl);
			jm.setCode(1);
			jm.setMsg("视频截图成功！");

		} catch (IOException e) {
			jm.setCode(-1);
			jm.setMsg("截图失败");
			e.printStackTrace();
		}
		return jm;
	}

	/**
	 * 视频压缩
	 * 
	 * @param url
	 * @return
	 */
	@RequestMapping(value = "spYs", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("视频压缩")
	public JsonMap spYs(String path) {

		JsonMap jm = new JsonMap();
		String savePath = path.substring(0, path.lastIndexOf("/") + 1);
		String rename = "ys" + UUID.randomUUID() + ".flv";
		String putfile = Global.ftpPath + path;
		String outfile = Global.ftpPath + savePath + rename;
		boolean flag = compressionVideo.spys(putfile, outfile);
		if (flag) {
			jm.setObject(savePath + rename);
			jm.setCode(1);
			jm.setMsg("压缩成功！");
		} else {
			jm.setCode(-1);
			jm.setMsg("压缩失败");
		}
		return jm;

	}

	/**
	 * 下载
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value="xiaZai",method=RequestMethod.GET)
	@ApiOperation("下载")
	public void xiaZai(HttpServletResponse response) {

		// 生成表格
		JsonMap jm = new JsonMap();
		try {
			ExportExcelUtil<Syslog> excelUtil = new ExportExcelUtil<>();
			LayuiPage page = new LayuiPage();
			page.setLimit(50);
			page.setPage(1);
			List<Syslog> syslogs = sysLogService.pageFound(new Syslog(), page);
			String[] array = { "主键id", "请求地址", "请求方法", "操作id", "耗时", "是否异常","操作人","操作时间" };
			String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
			String headStr = "attachment; filename=\"" + fileName + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM"); // octet-stream 自动匹配文件类型
			response.setHeader("Content-Disposition", headStr); // 激活下载框
			 OutputStream out = response.getOutputStream();
			 out = response.getOutputStream();
	        
			excelUtil.exportExcel("菜单", array, syslogs, out, excelUtil.EXCEl_FILE_2007);
			
		} catch (IOException e) {
			jm.setCode(-1);
			e.printStackTrace();
		}
		

	}

}

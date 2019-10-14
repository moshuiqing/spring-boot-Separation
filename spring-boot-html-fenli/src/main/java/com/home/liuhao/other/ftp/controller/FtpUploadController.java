package com.home.liuhao.other.ftp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.PathFormat;
import com.home.liuhao.other.ftp.po.FilePath;
import com.home.liuhao.other.ftp.util.Ftp;
import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.util.Global;
import com.home.liuhao.util.JsonMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * @author 刘浩
 * 
 */
@RequestMapping("/ftpup/")
@RestController
@Api(value = "ftpUploadController", tags = "ftp文件上传控制器")
public class FtpUploadController {


	@Autowired
	private EhCacheManager ehCacheManager;

	/**
	 * @param request
	 * @return 普通文件上传
	 */
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@ApiOperation(value = "ftp普通文件上传", notes = "ftp普通文件上传")
	public JsonMap uploadFile(HttpServletRequest request, MultipartFile file) {
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();
		JsonMap reMap = new JsonMap();
		if (!flag) {
			reMap.setMsg("ftp登录失败！");
			reMap.setCode(-1);
			return reMap;
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		String path;
		try {
			Integer index = Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.ftpScource+ filePath.getWebFilePath()[index];
		} catch (Exception e) {
			String filename = UUID.randomUUID().toString();
			path = Global.ftpScource+"/web/default/" + filename;
		}

		try {
			if (file != null) {
				String name = file.getOriginalFilename();
				String hz = name.substring(name.lastIndexOf("."), name.length());
				String uploadPath = PathFormat.parse(path, name);
				String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
				String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
				ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
				reMap.setMsg("上传成功！");
				reMap.setCode(1);
				reMap.setObject(upPath+filename);
			}
		} catch (IOException e) {
			e.printStackTrace();
			reMap.setCode(-1);
			reMap.setMsg("上传失败");
		}finally {
			ftp.ftpLogOut();
		}

		return reMap;
	}
	
	/**
	 * 聊天上传图片
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value="layImUpImg",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="聊天上传图片",notes="聊天上传图")
	public String layImUpImg(HttpServletRequest request, MultipartFile file) {
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();

		JSONObject obj = new JSONObject();
		if (!flag) {
			obj.put("msg","ftp登录失败！" );
			obj.put("code", -1);
			return obj.toString();
		}
		
		String path = Global.context.getAttribute("ftpScource")+Global.LAYIMUPIMGPATH;
		String fn = UUID.randomUUID().toString();
		path=path+fn;
		try {
			if (file != null) {
				String name = file.getOriginalFilename();
				String hz = name.substring(name.lastIndexOf("."), name.length());
				String uploadPath = PathFormat.parse(path, name);
				String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
				String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
				ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
				String url=upPath+filename;
				Map<String, String> map = new HashMap<>();
				map.put("src", url);
				obj.put("msg","上传成功！" );
				obj.put("code", 0);
				obj.put("data", map);
				return obj.toString();
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("msg","上传失败！" );
			obj.put("code", -1);
			return obj.toString();
		}finally {
			ftp.ftpLogOut();
		}
		return null;
		
		
		
	
		
	}
	
	/**
	 * 聊天上传文件
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value="layImUpFile",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="聊天上传文件",notes="聊天上传文件")
	public String layImUpFile(HttpServletRequest request, MultipartFile file) {
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();

		JSONObject obj = new JSONObject();
		if (!flag) {
			obj.put("msg","ftp登录失败！" );
			obj.put("code", -1);
			return obj.toString();
		}
		
		String path = Global.context.getAttribute("ftpScource")+Global.LAYIMUPFILEPATH;
		String fn = UUID.randomUUID().toString();
		path=path+fn;
		try {
			if (file != null) {
				String name = file.getOriginalFilename();
				String hz = name.substring(name.lastIndexOf("."), name.length());
				String uploadPath = PathFormat.parse(path, name);
				String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
				String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
				ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
				String url=upPath+filename;
				Map<String, String> map = new HashMap<>();
				map.put("src", url);
				map.put("name", name);
				obj.put("msg","上传成功！" );
				obj.put("code", 0);
				obj.put("data", map);
				return obj.toString();
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("msg","上传失败！" );
			obj.put("code", -1);
			return obj.toString();
		}finally {
			ftp.ftpLogOut();
		}
		return null;
		
		
		
	
		
	}
	
	
	
	/**
	 * @param request
	 * @return 文件上传
	 */
	@RequestMapping(value = "upFile", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "ftp文件上传", notes = "ftp文件上传")
	public JsonMap upFile(HttpServletRequest request, @RequestParam("file") MultipartFile file,String INDEXPATH) {
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();
		JsonMap br = new JsonMap();
		if (!flag) {
			br.setMsg("ftp登录失败！");
			br.setCode(-1);
			return br;
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		String path;
		try {
			Integer index = Integer.parseInt(INDEXPATH);//Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.context.getAttribute("ftpScource") + filePath.getWebFilePath()[index];
		} catch (NumberFormatException e) {
			String filename = UUID.randomUUID().toString();
			path = "/web/default/" + filename;
		}

		try {
			if (file != null) {
				String name = file.getOriginalFilename();
				String hz = name.substring(name.lastIndexOf("."), name.length());
				String uploadPath = PathFormat.parse(path, name);
				String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
				String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
				ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
				br.setMsg("上传成功！");
				br.setCode(1);
				br.setObject(upPath+filename);
			}
		} catch (IOException e) {
			e.printStackTrace();
			br.setCode(-1);
			br.setMsg("上传失败");
		}finally {
			ftp.ftpLogOut();
		}

		return br;
	}
	


	
	
	
	
	
	
	
	
	
	

	
}

package com.home.lh.other.ftp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.baidu.ueditor.PathFormat;
import com.home.lh.other.compression.util.Screenshots;
import com.home.lh.other.ftp.po.FilePath;
import com.home.lh.other.ftp.util.Ftp;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;

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
		Cache<String, Object> sysCache = ehCacheManager.getCache(Global.FILEPATH);
		String path;
		try {
			Integer index = Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.ftpScource+ filePath.getWebFilePath()[index];
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return 文件上传 android
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
		Cache<String, Object> sysCache = ehCacheManager.getCache(Global.FILEPATH);
		String path;
		try {
			Integer index = Integer.parseInt(INDEXPATH);//Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.context.getAttribute("ftpScource") + filePath.getMobileFilePath()[index];
		} catch (NumberFormatException e) {
			String filename = UUID.randomUUID().toString();
			path = "/phone/default/" + filename;
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
	
	
	/**
	 * 批量上传 android
	 * @param files
	 * @param INDEXPATH
	 * @return
	 */
	@RequestMapping(value = "batchUpload", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "批量文件上传android", notes = "批量文件上传android")
	public JsonMap batchUpload(HttpServletRequest request,@RequestParam("index")String INDEXPATH) {
		MultipartRequest multipartRequest = (MultipartRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("file");
		
		List<String> urls = new ArrayList<String>();
		
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();
		JsonMap br = new JsonMap();
		if (!flag) {
			br.setMsg("ftp登录失败！");
			br.setCode(-1);
			return br;
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache(Global.FILEPATH);
		String path;
		try {
			Integer index = Integer.parseInt(INDEXPATH);//Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.context.getAttribute("ftpScource") + filePath.getMobileFilePath()[index];
		} catch (NumberFormatException e) {
			String filename = UUID.randomUUID().toString();
			path = "/web/default/" + filename;
		}
		
		
		try {
			for(MultipartFile file:files) {
					String name = file.getOriginalFilename();
					String hz = name.substring(name.lastIndexOf("."), name.length());
					String uploadPath = PathFormat.parse(path, name);
					String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
					String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
					ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
					urls.add(upPath+filename);
			}
			
			br.setMsg("上传成功！");
			br.setCode(1);
			br.setObject(urls);		
		} catch (IOException e) {
			e.printStackTrace();
			br.setCode(-1);
			br.setMsg("上传失败");
		}finally {
			ftp.ftpLogOut();
		}

		return br;
		
	}
	

	/**
	 * 批量上传
	 * @param request  请求
	 * @param file  文件
	 * @param INDEXPATH  保存地址
	 * @return
	 */
	@RequestMapping(value = "appChatVideo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "android聊天上传视频取封面", notes = "android聊天上传视频取封面")
	public JsonMap appChatVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file,String INDEXPATH) {
		Ftp ftp = new Ftp();
		boolean flag = ftp.ftpLogin();
		JsonMap br = new JsonMap();
		if (!flag) {
			br.setMsg("ftp登录失败！");
			br.setCode(-1);
			return br;
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache(Global.FILEPATH);
		String path;
		try {
			Integer index = Integer.parseInt(INDEXPATH);//Integer.parseInt(request.getParameter(Global.INDEXPATH));
			FilePath filePath = (FilePath) sysCache.get(Global.FILEPATHKEY);
			path = Global.context.getAttribute("ftpScource") + filePath.getMobileFilePath()[index];
		} catch (NumberFormatException e) {
			String filename = UUID.randomUUID().toString();
			path = "/phone/default/" + filename;
		}

		try {
			if (file != null) {
				String name = file.getOriginalFilename();
				String hz = name.substring(name.lastIndexOf("."), name.length());
				if(!hz.equalsIgnoreCase(".mp4")) {					
					br.setCode(-1);
					br.setMsg("只支持mp4格式");
					return br;
				}
				
				String uploadPath = PathFormat.parse(path, name);
				String filename = uploadPath.substring(uploadPath.lastIndexOf("/") + 1, uploadPath.length()) + hz;
				String upPath = uploadPath.substring(0, uploadPath.lastIndexOf("/") + 1);
				ftp.uploadFileToFtp(file.getInputStream(), filename, upPath);
				String voideUrl=upPath+filename;
				/////////////////////////////截图//////////////////////////
				
				if(hz.equalsIgnoreCase(".mp4")) {
					String jdlj = Global.ftpPath + voideUrl;
					String imgurl = voideUrl.substring(0, voideUrl.lastIndexOf("/") + 1);
					imgurl = Screenshots.getThumbnai(jdlj, imgurl);
					voideUrl=voideUrl+","+imgurl;
				}
			
				
				///////////////////////////////////////////
				br.setMsg("上传成功！");
				br.setCode(1);
				br.setObject(voideUrl);
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

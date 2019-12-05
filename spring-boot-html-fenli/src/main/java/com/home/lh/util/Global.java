package com.home.lh.util;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.home.lh.other.activemq.util.DuiLeiHd;
import com.home.lh.util.systemutil.CloseImpl;

@Component
public class Global {
	


	
	public static final String CHARSET = "utf-8";
	
	/**
	 * 上传路劲缓存key
	 */
	public final static String FILEPATHKEY="FTPFILEPATH";
	
	public final static String FILEPATH="filePath";
	/**
	 * 系统菜单
	 */
	public final static String MODULESCACHKEY="module";
	
	public final static String BIGMENU="bigmenu";
	
	public final static String MENUS="menus";
	
	public final static String ALLMENUS="allmenus";
	
	/**
	 * 标志用户类型
	 */
	public static  final String loginType="loginType";
	
	public static  final String web="web";
	public static  final String app="app";
	public static  final String back="system";
	/**
	 * 后台用户sessionkey
	 */
	public static String sysUser="user";
	
	//前台用户sessionkey
	public static final String webUser="webuser"; 
	
	public static final String appUser = "appUser";
	
	public static final String BLOOENKEY= "blooenkey";
	
	/**
	 * 系统大菜单缓存key
	 */
	public final static String USERBIGMENU="userbigmenu";
	
	/**
	 * 全局变量
	 */
	public static ServletContext context;
	
	////////////////////////////////////////////
	///ftp/////
	/**
	 * ftp的ip地址
	 */

	public static String ftpip;

	/**
	 * ftp的端口
	 */

	public static int ftpport;

	/**
	 * ftp的用户名
	 */

	public static String ftpuser;

	/**
	 * ftp的用户名
	 */

	public static String ftppwd;
	
	/**
	 * js端配置文件夹路劲key
	 */
	public final static String INDEXPATH="INDEX";
	
	/**
	 * ftp上传文件地址
	 */
	
	public static String ftpScource;
	
	
	/**
	 * ftp绝对路径
	 */
	public static String ftpPath;
	
	@Value("${ftpPath}")
	public void setFtpPath(String ftpPath) {
		Global.ftpPath = ftpPath;
	}

	@Value("${ftpScource}")
	public  void setFtpScource(String ftpScource) {
		Global.ftpScource = ftpScource;
	}

	@Value("${ftphost}")
	public  void setFtpip(String ftphost) {
		Global.ftpip = ftphost;
	}

	@Value("${ftpport}")
	public  void setFtpport(int ftpport) {
		Global.ftpport = ftpport;
	}

	@Value("${ftpname}")
	public  void setFtpuser(String ftpname) {
		Global.ftpuser = ftpname;
	}

	@Value("${ftppwd}")
	public  void setFtppwd(String ftppwd) {
		Global.ftppwd = ftppwd;
	}
	//////////////////////////////////////////////////////////////////////////
	/////聊天//////////////////////
	
	////////////////////////////////////////////////////////////////////////////聊天 layim/////////////////////////
	
	public final static String MINE= "mine";
	public final static String FRIEND= "friend";
	public final static String GROUP= "group";
	
	
	/**
	 * 聊天图片地址
	 */
	public final static String LAYIMUPIMGPATH="imgPath/";
	
	/**
	 * 聊天文件地址
	 */
	public final static String LAYIMUPFILEPATH="filePath/";
	

	/**
	 * 算法类型
	 */
	public static String type;

	/**
	 * 迭代次数
	 */
	public static Integer iterations;
	

	@Value("${type}")
	public  void setType(String type) {
		Global.type = type;
	}
	@Value("${iterations}")
	public  void setIterations(Integer iterations) {
		Global.iterations = iterations;
	}
	
	////////////////////////全局单例用户
	
	public static CloseImpl close;
	
	public static  void  setClose(CloseImpl close) {
		Global.close = close;
	}
	
	public static DuiLeiHd duiLeiHd;

	public static void setDuiLeiHd(DuiLeiHd duiLeiHd) {
		Global.duiLeiHd = duiLeiHd;
	}
	
	
	



}

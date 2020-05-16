package com.home.lh.util.systemutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.home.lh.util.JsonMap;



/**
 * 概要说明 : 简化代码 <br>
 * 详细说明 : 简化代码 <br>
 * 创建时间 : 2018年10月30日 下午8:08:17 <br>
 * 
 * @author by liuhao
 */
public class SimpleUtils {
	
	
	/**  
     * 概要说明 : 返回信息 <br>
     * 详细说明 : 返回信息 <br>
     *
     * @param result 判断值
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.SimpleUitl#backMsg()
     * @author  by liuhao @ 2018年7月17日, 下午2:04:43 
     */
    public static String backMsg(Integer result)
    {

        String msg = "";
        if (result > 0)
        {
            msg = "操作成功！";
        }
        else if (result == -2)
        {
            msg = "名称重复，请更换！";
        }
        else if (result == -3)
        {
            msg = "缺少参数";
        }
        else
        {
            msg = "操作失败！";
        }

        return msg;
    }

	
	
	/**
	 * 概要说明 : 判断是否为空 <br>
	 * 详细说明 : 判断是否为空 <br>
	 *
	 * @param strings 字符串数组
	 * @return boolean 类型返回值说明
	 * @see com.jinge.portal.utils.SimpleUtils#isEmpty()
	 * @author by liuhao @ 2018年10月26日, 上午9:56:24
	 */
	public static boolean isEmpty(String... strings) {

		for (String s : strings) {
			if (StringUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 简化
	 * 
	 * @param code  1 :操作成功  ***-1 ：操作失败  ****-2 ：缺少参数 ******-3 ：自定义提示信息  -4：系统信息    （其他 ：系统错误）
	 * @param obj
	 * @param msg
	 * @return
	 */
	public static JsonMap addOruPdate(Integer code, Object obj, String msg) {
		JsonMap jp = new JsonMap();
		if (code == 1) {
			jp.setCode(1);
			if(StringUtils.isEmpty(msg)) {
				msg="操作成功！";
			}
			jp.setMsg(msg);
			jp.setObject(obj);
		} else if (code == -1) {
			jp.setCode(-1);			
			jp.setMsg("操作失败！");
			jp.setObject(obj);
		} else if (code == -2) {
			jp.setCode(-2);
			jp.setMsg("缺少参数！");
			jp.setObject(obj);
		} else if (code == -3) {
			jp.setCode(code);
			jp.setMsg(msg);
			jp.setObject(obj);
		} else if(code==-4){
			jp.setCode(code);
			jp.setMsg(msg);
			jp.setObject(obj);
		}else{
			jp.setMsg("系统错误");
		}

		return jp;

	}

	/**
	 * 概要说明 : 返回指定区间的随机数 <br>
	 * 详细说明 : 返回指定区间的随机数 <br>
	 *
	 * @param max
	 * @param min
	 * @return Integer 类型返回值说明
	 * @see com.jinge.portal.utils.SimpleUtils#getRadmin()
	 * @author by liuhao @ 2018年10月29日, 下午2:58:55
	 */
	public static Integer getRadmin(Integer max, Integer min) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;

	}

	/**
	 * 概要说明 : 返回时间差天数 <br>
	 * 详细说明 : 返回时间差天数 <br>
	 *
	 * @param date 时间
	 * @param dt   时间字符串
	 * @return
	 * @throws ParseException Integer 类型返回值说明
	 * @see com.jinge.portal.utils.SimpleUtils#backDay()
	 * @author by liuhao @ 2018年10月31日, 下午1:36:00
	 */
	public static Integer backDay(Date date, String dt) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date2 = dateFormat.parse(dt);

		/* 天数差 */
		long d2 = date2.getTime();
		long d1 = date.getTime();
		int days = (int) ((d2 - d1) / (1000 * 60 * 60 * 24));
		// System.out.println("两个时间之间的天数差为：" + days);
		return days;
	}

	/**
	 * 概要说明 : 获取ip <br>
	 * 详细说明 : * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 参考文章： http://developer.51cto.com/art/201111/305181.htm
	 * 
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
	 * 
	 * 用户真实IP为： 192.168.1.110 <br>
	 *
	 * @param request
	 * @return String 类型返回值说明
	 * @see com.jinge.system.utils.SysUtil#getIpAddress()
	 * @author by wangwei @ 2017年7月10日, 下午2:22:22
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获得6位数随机数
	 * @return
	 */
	public static String getSixSjs() {
		int sj = (int)((Math.random()*9+1)*100000);
		return sj+"";

	}
	


}

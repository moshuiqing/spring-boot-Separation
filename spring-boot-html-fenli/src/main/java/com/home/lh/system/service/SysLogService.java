package com.home.lh.system.service;

import java.util.List;

import com.home.lh.system.po.Syslog;
import com.home.lh.util.LayuiPage;


public interface SysLogService {
	
	/**
	 * 新增
	 * @param syslog 系统日志
	 * @return
	 */
	Integer insert(Syslog syslog);
	

	/**
	 * @param s  实体
	 * @param page  分页参数
	 * @return
	 */
	List<Syslog> pageFound(Syslog s,LayuiPage page);
	
	/**
	 * 查询数量
	 * @param s
	 * @return
	 */
	Integer pageCount(Syslog s);


}

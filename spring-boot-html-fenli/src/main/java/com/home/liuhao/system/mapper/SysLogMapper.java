package com.home.liuhao.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.home.liuhao.system.po.Syslog;


/**
 * @author ai996
 * 日志dao
 */
@Mapper
public interface SysLogMapper {
	
	/**
	 * 新增
	 * @param syslog 系统日志
	 * @return
	 */
	Integer insert(Syslog syslog);
	
	/**
	 * 分页查询
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
	List<Syslog> pageFound(@Param("s")Syslog s,@Param("start")Integer start,@Param("end")Integer end);
	
	/**
	 * 查询数量
	 * @param s
	 * @return
	 */
	Integer pageCount(@Param("s")Syslog s);

}

package com.home.lh.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.system.mapper.SysLogMapper;
import com.home.lh.system.po.Syslog;
import com.home.lh.system.service.SysLogService;
import com.home.lh.util.LayuiPage;


@Service
public class SysLogServiceImpl implements SysLogService {

	/**
	 * dao注入
	 */
	@Autowired
	private SysLogMapper sysLogMapper;

	@Override
	public Integer insert(Syslog syslog) {

		return sysLogMapper.insert(syslog);
	}

	@Override
	public List<Syslog> pageFound(Syslog s, LayuiPage page) {

		return sysLogMapper.pageFound(s, page.getStart(), page.getEnd());
	}

	@Override
	public Integer pageCount(Syslog s) {

		return sysLogMapper.pageCount(s);
	}

}

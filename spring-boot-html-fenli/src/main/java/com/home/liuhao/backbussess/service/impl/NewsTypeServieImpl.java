package com.home.liuhao.backbussess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.backbussess.mapper.NewsTypeMapper;
import com.home.liuhao.backbussess.po.NewsType;
import com.home.liuhao.backbussess.service.NewsTypeService;
import com.home.liuhao.util.LayuiPage;

@Service
public class NewsTypeServieImpl implements NewsTypeService{
	
	@Autowired
	private NewsTypeMapper newsTypeMapper;

	@Override
	public List<NewsType> simpleFound(NewsType newsType) {
		
		return newsTypeMapper.simpleFound(newsType);
	}

	@Override
	public Integer update(NewsType newsType) {
		
		return newsTypeMapper.update(newsType);
	}

	@Override
	public Integer delete(NewsType newsType) {
	
		return newsTypeMapper.delete(newsType);
	}

	@Override
	public Integer pageCount(NewsType newsType) {
		
		return newsTypeMapper.pageCount(newsType);
	}

	@Override
	public List<NewsType> pageFound(NewsType newsType, LayuiPage p) {
	
		return newsTypeMapper.pageFound(newsType, p.getStart(), p.getEnd());
	}

	@Override
	public Integer insert(NewsType newsType) {
		
		return newsTypeMapper.insert(newsType);
	}

}

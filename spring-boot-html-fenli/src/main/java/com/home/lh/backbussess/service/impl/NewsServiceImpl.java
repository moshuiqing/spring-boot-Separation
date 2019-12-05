package com.home.lh.backbussess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.backbussess.mapper.NewsMapper;
import com.home.lh.backbussess.po.News;
import com.home.lh.backbussess.service.NewsService;
import com.home.lh.util.LayuiPage;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsMapper newsMapper;

	@Override
	public Integer insert(News n) {

		return newsMapper.insert(n);
	}

	@Override
	public Integer update(News n) {

		return newsMapper.update(n);
	}

	@Override
	public Integer delete(News n) {

		return newsMapper.delete(n);
	}

	@Override
	public Integer pageCount(News n) {

		return newsMapper.pageCount(n);
	}

	@Override
	public List<News> simpleFound(News n) {

		return newsMapper.simpleFound(n);
	}

	@Override
	public List<News> pageFound(News n, LayuiPage p) {

		return newsMapper.pageFound(n, p.getStart(), p.getEnd());
	}

	@Override
	public List<News> foundTop() {
		
		return newsMapper.foundTop();
	}

	@Override
	public Integer deletes(String[] ids) {		
		try {
			newsMapper.deletes(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

}

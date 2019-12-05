package com.home.lh.backbussess.service;

import java.util.List;

import com.home.lh.backbussess.po.News;
import com.home.lh.util.LayuiPage;

public interface NewsService {

	Integer insert(News n);
	
	Integer update(News n);
	
	Integer delete(News n);
	
	Integer pageCount(News n);
	
	List<News> simpleFound(News n);
		
	List<News> pageFound(News n,LayuiPage p);
	
	List<News> foundTop();
	
	Integer deletes(String[] ids);
}

package com.home.lh.backbussess.service;

import java.util.List;

import com.home.lh.backbussess.po.NewsType;
import com.home.lh.util.LayuiPage;

public interface NewsTypeService {

	List<NewsType> simpleFound(NewsType newsType);

	Integer update(NewsType newsType);

	Integer delete(NewsType newsType);

	Integer pageCount(NewsType newsType);
	
	Integer insert(NewsType newsType);
	
	List<NewsType> pageFound(NewsType newsType,LayuiPage p);

}

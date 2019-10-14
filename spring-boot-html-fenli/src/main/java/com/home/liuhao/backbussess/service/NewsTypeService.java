package com.home.liuhao.backbussess.service;

import java.util.List;

import com.home.liuhao.backbussess.po.NewsType;
import com.home.liuhao.util.LayuiPage;

public interface NewsTypeService {

	List<NewsType> simpleFound(NewsType newsType);

	Integer update(NewsType newsType);

	Integer delete(NewsType newsType);

	Integer pageCount(NewsType newsType);
	
	Integer insert(NewsType newsType);
	
	List<NewsType> pageFound(NewsType newsType,LayuiPage p);

}

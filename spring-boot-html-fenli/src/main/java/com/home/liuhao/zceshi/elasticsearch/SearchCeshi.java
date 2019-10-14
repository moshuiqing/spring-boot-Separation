package com.home.liuhao.zceshi.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;

import com.home.liuhao.backbussess.po.News;

public class SearchCeshi {
	
	@Autowired
	private MyElasticSearch elasticSearch;
	
	
	
	public void test1() {
		
		News n = new News();
		n.setTitle("1111");
		elasticSearch.save(n);
		
		elasticSearch.findByTitleLike("1111");
	
	}

}

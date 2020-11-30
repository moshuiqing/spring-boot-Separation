package com.home.lh.zceshi.elasticsearch;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.home.lh.backbussess.po.News;

public class SearchCeshi {
	
	@Autowired
	private MyElasticSearch elasticSearch;
	
	
	
	public void test1() {
		
		News n = new News();
		n.setTitle("1111111111111");
		elasticSearch.save(n);
		
		elasticSearch.findByTitleLike("1111");
		
	}

}

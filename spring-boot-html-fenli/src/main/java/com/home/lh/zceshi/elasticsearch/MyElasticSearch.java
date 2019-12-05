package com.home.lh.zceshi.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.lh.backbussess.po.News;


/**
 * 全文搜索引擎
 * @author liuhao
 *
 */
@Repository
public interface MyElasticSearch extends ElasticsearchRepository<News, String> {
	
	public List<News> findByTitleLike(String title);

}

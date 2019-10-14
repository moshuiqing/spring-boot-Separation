package com.home.liuhao.zceshi.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.liuhao.backbussess.po.News;


/**
 * 全文搜索引擎
 * @author liuhao
 *
 */
@Repository
public interface MyElasticSearch extends ElasticsearchRepository<News, String> {
	
	public List<News> findByTitleLike(String title);

}

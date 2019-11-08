package com.home.liuhao.other.weixin.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.liuhao.other.weixin.po.MsgStrInfo;

/**
 * 全文搜索引擎 微信回复信息
 * @author liuhao
 *
 */
@Repository
public interface ReplyMsgSearch extends ElasticsearchRepository<MsgStrInfo, Integer> {
	
	
	
	
	
	

}

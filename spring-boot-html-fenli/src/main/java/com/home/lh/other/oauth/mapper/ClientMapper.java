package com.home.lh.other.oauth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.home.lh.other.oauth.po.Client;



/**
 * @author ai996
 * 授权客户端dao
 */
@Mapper
public interface ClientMapper {
	
	/**
	 * info:普通查询
	 * @param c
	 * @return
	 */
	List<Client> simpleFound(Client c);

}


/*package com.home.liuhao;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.sl.usermodel.ObjectMetaData.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;
import org.tio.websocket.starter.EnableTioWebSocketServer;

import com.home.lh.SpringBootHtmlApplication;
import com.home.lh.backbussess.po.News;
import com.home.lh.zceshi.elasticsearch.MyElasticSearch;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootHtmlApplication.class)
public class SpringBootHtmlApplicationTests {
	
	@Autowired
	private MyElasticSearch elasticSearch;


	@Test
	public void contextLoads() {
		
		List<News> list = new ArrayList<News>();
		for(int i=0;i<10;i++) {
			News n = new News();
			n.setTitle("震惊了"+i);
			n.setAuthor("uc小编"+i);
			n.setId(i);
			n.setTypeName("第三方第三方士大夫"+i);
			n.setContent("换个房间规范化鼓风机官方警告方进入天涯热土"+i);
			list.add(n);
		}
		
		elasticSearch.saveAll(list);
		
		
			
	
		
	}

}
*/

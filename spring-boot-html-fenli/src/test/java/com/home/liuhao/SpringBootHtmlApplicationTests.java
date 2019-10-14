package com.home.liuhao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.home.liuhao.other.emalutil.EamilComment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootHtmlApplicationTests {

	
	@Autowired
	private EamilComment eamilComment;
	
	@Test
	public void contextLoads() {
		
		//eamilComment.sendTemplateMail("javaliuhao@126.com");
		
	}

}

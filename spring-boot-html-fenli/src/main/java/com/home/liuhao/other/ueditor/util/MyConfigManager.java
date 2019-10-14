package com.home.liuhao.other.ueditor.util;



import java.io.BufferedReader;
import java.io.IOException;






import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONObject;
	
	public final class MyConfigManager {
	  

	    private JSONObject jsonConfig = null;
	    private static MyConfigManager manager;

	    private MyConfigManager() throws IOException {
	        this.initEnv();
	    }

	    public static MyConfigManager getInstance() {
	        if (manager != null) {
	            //ueditor每次实例化都会获取配置文件，单列模式，防止一直new对象
	            return manager;
	        }
	        try {
	            manager = new MyConfigManager();
	            return manager;
	        } catch (Exception var4) {
	        	var4.printStackTrace();
	            return null;
	        }
	    }

	    public boolean valid() {
	        return this.jsonConfig != null;
	    }

	    public JSONObject getAllConfig() {
	        if (this.jsonConfig != null)
	            return this.jsonConfig;
	        else {
	            JSONObject state=new JSONObject();
	            //将“配置文件初始化失败”转成unicode编码返回
	            state.put("state","\\u914d\\u7f6e\\u6587\\u4ef6\\u521d\\u59cb\\u5316\\u5931\\u8d25");
	            return state;
	        }
	    }

	    private void initEnv() throws IOException {
	        String configContent = this.readFile();
	        try {
	            JSONObject e = JSONObject.parseObject(configContent);
	            this.jsonConfig = e;
	        } catch (Exception var4) {
	            this.jsonConfig = null;
	        }

	    }

	    private String readFile() throws IOException {
	        StringBuilder builder = new StringBuilder();
	       
	        Resource resource = new ClassPathResource("config.json");
	        try {
	            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
	            BufferedReader bfReader = new BufferedReader(reader);
	            String tmpContent = null;

	            while ((tmpContent = bfReader.readLine()) != null) {
	                builder.append(tmpContent);
	            }
	          
	            bfReader.close();
	        } catch (UnsupportedEncodingException var6) {
	            
	            ;
	        }
	        return this.filter(builder.toString());
	    }

	    private String filter(String input) {
	        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	    }
	}


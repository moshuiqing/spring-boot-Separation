package com.home.liuhao.system.lister;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Value;

import com.home.liuhao.util.Global;



@WebListener
public class BckLister implements ServletContextListener {
	
	
	
	@Value(value = "${webip}")
	private String webip;
	
	@Value(value="${webname}")
	private String webname;
	
	@Value(value="${ftpScource}")
	private String ftpScource;
	
	@Value(value="${tcpPort}")
	private String tcpPort;
	
	@Value(value="${tcpIp}")
	private String tcpIp;
	


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Global.context = sce.getServletContext();// 获取全局上下文servletcontext
		ServletContext c = Global.context;
		//系统启动时执行
		c.setAttribute("webip", webip);
		c.setAttribute("webname", webname);
		c.setAttribute("ftpScource", ftpScource);
		c.setAttribute("tcpPort", tcpPort);
		c.setAttribute("tcpIp", tcpIp);
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//系统销毁时执行
		System.out.println("系统关闭");

		
	}
	
	


}

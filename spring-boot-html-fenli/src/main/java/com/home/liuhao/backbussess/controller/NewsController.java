package com.home.liuhao.backbussess.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.home.liuhao.backbussess.po.News;
import com.home.liuhao.backbussess.po.NewsType;
import com.home.liuhao.backbussess.service.NewsService;
import com.home.liuhao.backbussess.service.NewsTypeService;
import com.home.liuhao.util.JsonMap;
import com.home.liuhao.util.LayuiPage;
import com.home.liuhao.util.systemutil.SimpleUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/backsystem/news/")
@Api(value = "NewsController", tags = "新闻后台管理")
public class NewsController {

	/**
	 * 新闻类型
	 */
	@Autowired
	private NewsTypeService newsTypeService;

	/**
	 * 新闻
	 */
	@Autowired
	private NewsService newsService;

	/**
	 * @return 跳转新闻类型页
	 */
	@RequestMapping(value = "toNewsType", method = RequestMethod.GET)
	@ApiOperation(value = "跳转新闻类型页面", notes = "跳转新闻类型页面")
	public String toNewsType() {

		return "/liuhao/page/backbusiness/news/newstype";
	}

	/**
	 * @param newsType
	 * @param p
	 * @return 分页 查询
	 */
	@RequestMapping(value = "foundPageNewsType", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "分页查询新闻类型", notes = "分页查询新闻类型")
	public String foundPageNewsType(NewsType newsType, LayuiPage p) {

		JSONObject obj = new JSONObject();
		Integer count = newsTypeService.pageCount(newsType);
		List<NewsType> newsTypes = newsTypeService.pageFound(newsType, p);
		obj.put("count", count);
		obj.put("data", newsTypes);
		obj.put("code", 0);
		obj.put("msg", "");

		return obj.toString();
	}

	/**
	 * 新增
	 * 
	 * @param newsType
	 * @return
	 */
	@RequestMapping(value = "addNewsType", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增新闻类型", notes = "新增新闻类型")
	public JsonMap addNewsType(NewsType newsType) {
		boolean flag = SimpleUtils.isEmpty(newsType.getTypeName());
		if (flag) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}
		Integer count = newsTypeService.pageCount(newsType);// 判断是否重名
		Integer code = 0;
		if (count > 0) {
			code = -3;
		} else {
			code = newsTypeService.insert(newsType);
		}
		return SimpleUtils.addOruPdate(code, null, "该类型已存在！");
	}

	/**
	 * 修改
	 * 
	 * @param newsType
	 * @return
	 */
	@RequestMapping(value = "updateNewsType", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改新闻类型", notes = "修改新闻类型")
	public JsonMap updateNewsType(NewsType newsType) {

		Integer code = -2;
		Integer count = 0;
		if (newsType.getTypeName() != null && !newsType.getTypeName().equals("")) {
			if (newsType.getTypeName().equals(newsType.getCopytypeName())) {
				return SimpleUtils.addOruPdate(1, newsType, null);
			} else {
				count = newsTypeService.pageCount(newsType);// 判断是否重名
			}

			if (count > 0) {
				return SimpleUtils.addOruPdate(-3, null, "该类型已存在！");
			} else {

				code = newsTypeService.update(newsType);

			}
		}
		return SimpleUtils.addOruPdate(code, null, null);

	}

	/**
	 * @param newsType
	 * @return 删除类型
	 */
	@RequestMapping(value = "deleteNewsType", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除新闻类型", notes = "删除新闻类型")
	public JsonMap deleteNewsType(NewsType newsType) {

		Integer code = newsTypeService.delete(newsType);

		return SimpleUtils.addOruPdate(code,null, null);
	}

	///////////////////////////////////////////////////////// 新闻////////////////////////////////////////////////
	/**
	 * @return 跳转新闻管理页
	 */
	@RequestMapping(value = "toNews", method = RequestMethod.GET)
	@ApiOperation(value = "跳转新闻管理页", notes = "跳转新闻管理页")
	public String toNews(Model m) {

		List<NewsType> newsTypes = newsTypeService.simpleFound(new NewsType());
		m.addAttribute("nts", newsTypes);
		return "/liuhao/page/backbusiness/news/news";
	}

	/**
	 * 跳转新增
	 * 
	 * @return
	 */
	@RequestMapping(value = "addToNews", method = RequestMethod.GET)
	@ApiOperation(value = "跳转新增页面", notes = "跳转新增页面")
	public String addToNews(Model m) {
		List<NewsType> newsTypes = newsTypeService.simpleFound(new NewsType());
		m.addAttribute("nts", newsTypes);
		return "/liuhao/page/backbusiness/news/addOrUpdate";
	}

	/**
	 * 跳转修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateToNews", method = RequestMethod.GET)
	@ApiOperation(value = "跳转修改页面", notes = "跳转修改页面")
	public String updateToNews(News n, Model m) {
		List<NewsType> newsTypes = newsTypeService.simpleFound(new NewsType());
		m.addAttribute("nts", newsTypes);
		List<News> list = newsService.simpleFound(n);
		if(!list.isEmpty()) {
			m.addAttribute("ns",list.get(0) );			
		}
		return "/liuhao/page/backbusiness/news/addOrUpdate";
	}

	/**
	 * @param n
	 * @param p
	 * @return 分页查询
	 */
	@RequestMapping(value = "foundPageNews", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "分页查询新闻", notes = "分页查询新闻")
	public String foundPageNews(News n, LayuiPage p) {
		JSONObject obj = new JSONObject();
		Integer count = newsService.pageCount(n);
		List<News> news = newsService.pageFound(n, p);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", news);
		return obj.toString();
	}
	
    /**
     * info:新增新闻
     * @param n
     * @return
     */
	@RequestMapping(value="addNews",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="新增新闻",notes="新增新闻")
    public JsonMap addNews(News n) {
    	boolean flag = SimpleUtils.isEmpty(n.getAuthor(),n.getContent(),n.getTitle(),n.getTypeid());
    	if(flag) {
    		return SimpleUtils.addOruPdate(-2, null,null);
    	}
    	Integer code = newsService.insert(n);
		return SimpleUtils.addOruPdate(code, null, null);   	
    }
	
	/**
	 * info:修改新闻
	 * @param n
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateNews",method=RequestMethod.POST)
	@ApiOperation(value="修改新闻",notes="修改新闻")
	public JsonMap updateNews(News n) {
		boolean flag = SimpleUtils.isEmpty(n.getAuthor(),n.getContent(),n.getTitle(),n.getTypeid(),n.getId()+"");
    	if(flag) {
    		return SimpleUtils.addOruPdate(-2, null,null);
    	}
		Integer code = newsService.update(n);
		return SimpleUtils.addOruPdate(code, null, null);
		
	}	
	
	/**
	 * info:删除新闻
	 * @param n
	 * @return
	 */
	@RequestMapping(value="deleteNews",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="删除新闻",notes="删除新闻")
	public JsonMap deleteNews(String ids) {
		boolean flag = SimpleUtils.isEmpty(ids);
		if(flag) {
			SimpleUtils.addOruPdate(-3, null, "请选择要删除的新闻");
		}
		String [] newsIds = ids.split(",");
		Integer code = newsService.deletes(newsIds);		
		return SimpleUtils.addOruPdate(code, null, null);
		
	}
}

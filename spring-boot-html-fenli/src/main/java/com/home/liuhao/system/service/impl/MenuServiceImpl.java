package com.home.liuhao.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.home.liuhao.system.mapper.BigMenuMapper;
import com.home.liuhao.system.mapper.MenuMapper;
import com.home.liuhao.system.mapper.ModelMapper;
import com.home.liuhao.system.mapper.RoleMapper;
import com.home.liuhao.system.po.BigMenu;
import com.home.liuhao.system.po.Menu;
import com.home.liuhao.system.po.Module;
import com.home.liuhao.system.po.Role;
import com.home.liuhao.system.service.MenuService;
import com.home.liuhao.util.Global;
import com.home.liuhao.util.LayuiTree;
import com.home.liuhao.util.Menus;
import com.home.liuhao.util.systemutil.SimpleUtils;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EhCacheManager ehCacheManager;

	@Autowired
	private BigMenuMapper bigMenuMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public void cacheMenuModule() {
		List<Module> modules = modelMapper.simepleFound();
		// 存入缓存
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");

		sysCache.put(Global.MODULESCACHKEY, modules);
		List<BigMenu> bgm = bigMenuMapper.simpleFound();
		sysCache.put(Global.BIGMENU, bgm);
		List<LayuiTree> LayuiTrees = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<BigMenu> bigMenus = (List<BigMenu>) sysCache.get(Global.BIGMENU);
		for (BigMenu bmu : bigMenus) {
			List<LayuiTree> data = new ArrayList<>();
			LayuiTree layuiTree = new LayuiTree();
			layuiTree.setTitle(bmu.getTitle());
			layuiTree.setMpName(bmu.getMpname());
			layuiTree.setIcon(bmu.getIcon());
			layuiTree.setSort(bmu.getSort());
			layuiTree.setJibie("1");
			layuiTree.setSpread(true);
			layuiTree.setId(bmu.getId());
			Menus ms = new Menus();
			ms.setMpName(bmu.getMpname());
			List<Menus> menuss = menuMapper.simpleFound(ms);
			for (Menus m : menuss) {
				List<LayuiTree> list1 = new ArrayList<>();
				LayuiTree xtree = new LayuiTree();
				xtree.setTitle(m.getTitle());
				xtree.setId(m.getId()+"");
				xtree.setIcon(m.getIcon());
				xtree.setSort(m.getSort());
				xtree.setMpName(m.getMpName());
				xtree.setSpread(true);
				xtree.setHref(m.getHref());
				xtree.setJibie("2");
				Menus men = new Menus();
				men.setParent(m.getId() + "");
				List<Menus> list = menuMapper.simpleFound(men);
				for (Menus mn : list) {
					LayuiTree le = new LayuiTree();
					le.setTitle(mn.getTitle());
					le.setId(mn.getId()+"");
					le.setIcon(mn.getIcon());
					le.setChildren(null);
					le.setHref(mn.getHref());
					le.setJibie("3");
					le.setSort(mn.getSort());
					list1.add(le);
				}
				xtree.setChildren(list1);
				data.add(xtree);
			}

			layuiTree.setChildren(data);
			LayuiTrees.add(layuiTree);

		}
		sysCache.put(Global.ALLMENUS, LayuiTrees);

	}

	@Override
	public List<Menus> simpleFound(Menus m) {

		return menuMapper.simpleFound(m);
	}

	@Override
	public Integer deleteMenus(Menu m) {
		Integer flag = 0;
		if (m.getId() == null) {
			flag = -2;
		} else {

			try {
				menuMapper.deleteMenus(m);
				flag = 1;
			} catch (Exception e) {
				flag = -1;
				e.printStackTrace();
			}
		}

		return flag;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Integer insertMenus(Menu m,String roleId) {		
		Integer result=0;
		boolean flag = SimpleUtils.isEmpty(m.getIcon(),m.getMpname(),m.getTitle(),m.getSort()+"");
		if(flag) {
			result=-2;
		}else {
			
			result= menuMapper.insertMenus(m);
			// 将新增的id放入角色里面
			Role role = new Role();
			role.setId(roleId);
			List<Role> roles = roleMapper.simpleFoundRole(role);
			role = roles.get(0);
			role.setMenuid(role.getMenuid() + "," + m.getId());
			result = roleMapper.updateRole(role);
		}
		return result;
	}

	@Override
	public Integer updateMenus(Menu m) {		
		Integer result = 0;		
		try {
			result = menuMapper.updateMenus(m);
		} catch (Exception e) {
			result = -2;
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer countNum(Menu m) {		
		return menuMapper.countNum(m);
	}

}

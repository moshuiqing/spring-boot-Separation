package com.home.lh.system.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.home.lh.system.mapper.BigMenuMapper;
import com.home.lh.system.mapper.RoleMapper;
import com.home.lh.system.po.BigMenu;
import com.home.lh.system.po.Role;
import com.home.lh.system.service.BigMenuService;
import com.home.lh.util.systemutil.SimpleUtils;

@Service
public class BigMenuServiceImpl implements BigMenuService {

	@Autowired
	private BigMenuMapper bigMenuMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<BigMenu> simpleFound() {
		return bigMenuMapper.simpleFound();
	}

	@Override
	public Integer pageCount(BigMenu b) {

		return bigMenuMapper.pageCount(b);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Integer insertMenu(BigMenu b, String roleId) {
		b.setId(UUID.randomUUID().toString());
		Integer result = 0;
		boolean flag = SimpleUtils.isEmpty(b.getMpname(), b.getTitle(), b.getIcon());
		if (flag) {
			result = -2;
		}else {
			result= bigMenuMapper.insertMenu(b);
			// 将新增的id放入用户里面
			Role role = new Role();
			role.setId(roleId);
			List<Role> roles = roleMapper.simpleFoundRole(role);
			role = roles.get(0);
			role.setBigmenuid(role.getBigmenuid() + "," + b.getId());
			result = roleMapper.updateRole(role);
		}

		
		return result;
	}

	@Override
	public Integer updateMenu(BigMenu b) {
		boolean flag = SimpleUtils.isEmpty(b.getId() + "");
		if (flag) {
			return -2;
		}
		return bigMenuMapper.updateMenu(b);
	}

	@Override
	public Integer deleteMenu(BigMenu b) {
		Integer num = 0;
		boolean flag = SimpleUtils.isEmpty(b.getId() + "");
		if (flag) {
			num = -2;
		} else {
			try {
				bigMenuMapper.deleteMenu(b);
				num = 1;
			} catch (Exception e) {
				num = -1;
				e.printStackTrace();
			}
		}
		return num;

	}

}

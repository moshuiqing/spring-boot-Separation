package com.home.liuhao.system.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.system.mapper.RoleMapper;
import com.home.liuhao.system.po.Role;
import com.home.liuhao.system.service.RoleService;
import com.home.liuhao.util.LayuiPage;

@Service
public class RoleServiceImple implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> simpleFoundRole(Role r) {
		
		return roleMapper.simpleFoundRole(r);
	}

	@Override
	public Integer insertRole(Role r) {
		r.setId(UUID.randomUUID().toString());
		
		return roleMapper.insertRole(r);
	}

	@Override
	public Integer updateRole(Role r) {
		
		return roleMapper.updateRole(r);
	}

	@Override
	public Integer deleteRole(Role r) {
		Integer result = -1;
		
		try {
			roleMapper.deleteRole(r);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
		
	}

	@Override
	public List<Role> pageFoundRole(Role r,LayuiPage p) {
		
		return roleMapper.pageFoundRole(r,p.getStart(),p.getEnd());
	}

	@Override
	public Integer pageCountRole(Role r) {
		
		return roleMapper.pageCountRole(r);
	}

}

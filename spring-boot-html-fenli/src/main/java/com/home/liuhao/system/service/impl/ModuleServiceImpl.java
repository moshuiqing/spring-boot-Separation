package com.home.liuhao.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.system.mapper.ModelMapper;
import com.home.liuhao.system.po.Module;
import com.home.liuhao.system.service.ModuleService;
@Service
public class ModuleServiceImpl implements ModuleService {
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<Module> simepleFound() {
		
		return modelMapper.simepleFound();
	}

}

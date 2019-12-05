package com.home.lh.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.system.mapper.ModelMapper;
import com.home.lh.system.po.Module;
import com.home.lh.system.service.ModuleService;
@Service
public class ModuleServiceImpl implements ModuleService {
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<Module> simepleFound() {
		
		return modelMapper.simepleFound();
	}

}

package com.home.lh.webbussess.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.util.Global;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.DateUtils;
import com.home.lh.util.systemutil.SimpleUtils;
import com.home.lh.util.systemutil.SystemUtils;
import com.home.lh.webbussess.mapper.UserMapper;
import com.home.lh.webbussess.po.User;
import com.home.lh.webbussess.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public Integer insert(User u) {
		u.setId(UUID.randomUUID().toString());
		boolean flag = SimpleUtils.isEmpty(u.getBirthday(), u.getHeadImg(), u.getPassword(), u.getPhone(),
				u.getRemark(), u.getUserName(),u.getRealName(),u.getHeadImg());
		if (flag) {
			return -2;
		}
		String salt = DateUtils.backNum("");
		if (u.getPassword() != null) {
			String pwd = SystemUtils.MD5(Global.type, u.getPassword(), salt, Global.iterations);
			u.setPassword(pwd);
		}
		u.setSalt(salt);
		return userMapper.insert(u);
	}

	@Override
	public Integer update(User user) {

		if (user.getId().isEmpty()) {
			return -2;
		}

		String salt = DateUtils.backNum("");
		if (user.getPassword() != null) {
			String pwd = SystemUtils.MD5(Global.type, user.getPassword(), salt, Global.iterations);
			user.setPassword(pwd);
		}
		user.setSalt(salt);

		return userMapper.update(user);
	}

	@Override
	public Integer delete(User user) {

		Integer result = -1;
		if (user.getId().isEmpty()) {
			result = -2;
		} else {
			try {
				userMapper.delete(user);
				result = 1;
			} catch (Exception e) {
				result = -1;
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public List<User> simpleFound(User user) {

		return userMapper.simpleFound(user);
	}

	@Override
	public User login(User user) {
		return userMapper.login(user);
	}

	@Override
	public Integer pageCount(User user) {

		return userMapper.pageCount(user);
	}

	@Override
	public List<User> pageFound(User u, LayuiPage page) {

		return userMapper.pageFound(u, page.getStart(), page.getEnd());
	}

	@Override
	public Integer insert1(User u) {

		u.setId(UUID.randomUUID().toString());
		boolean flag = SimpleUtils.isEmpty(u.getPassword(), u.getUserName());
		if (flag) {
			return -2;
		}
		String salt = DateUtils.backNum("");
		if (u.getPassword() != null) {
			String pwd = SystemUtils.MD5(Global.type, u.getPassword(), salt, Global.iterations);
			u.setPassword(pwd);
		}
		u.setSalt(salt);

		return userMapper.insert1(u);
	}

	@Override
	public Integer saveLoginTime(User u) {

		return userMapper.saveLoginTime(u);
	}

	@Override
	public Integer updateChange(User u) {

		if (SimpleUtils.isEmpty(u.getId(), u.getIsdisable())) {
			return -2;
		}
		return userMapper.update(u);
	}

	@Override
	public Integer deletesWebUsers(String[] uids) {

		Integer result = -1;
		try {
			userMapper.deletesWebUsers(uids);
			result = 1;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;

	}

	@Override
	public Integer wxUpdate(User user) {
	
		return userMapper.update(user);
	}

}

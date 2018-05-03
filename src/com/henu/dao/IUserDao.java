package com.henu.dao;

import com.henu.domain.User;

public interface IUserDao {

	/**
	 * 插入或修改用户 
	 * @return 是否成功
	 */
	boolean save(User user);

	/**
	 * 查询用户信息
	 * @return 查询到的用户信息
	 */
	User find(String phonenumber, String password);

	/**
	 * 查询手机号是否已经注册
	 * @return 是否已经注册
	 */
	User findByPhone(String phonenumber);
}
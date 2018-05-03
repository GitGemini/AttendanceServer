package com.henu.service;

public interface IUserService {

	/**
	 * 此处认为客户端在进行注册之前已经向服务端查询过 username 与 phonenumber 是否存在 故此处不再进行验证
	 */
	void registerUser();

	void login();

	void resetPassword();

	void queryUserInfo();

	void changeUserInfo();

	void findPhonenumber();

}
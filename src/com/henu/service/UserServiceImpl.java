package com.henu.service;

import java.net.Socket;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.henu.dao.IUserDao;
import com.henu.dao.UserDaoImpl;
import com.henu.domain.SocketMessage;
import com.henu.domain.User;
import com.henu.socket.SendMsgHandler;
import com.henu.socket.SocketManger;


public class UserServiceImpl implements IUserService {
	private JSONObject json;
	private Socket socket;
	private IUserDao userDao;
	private SocketMessage sm;

	public UserServiceImpl(JSONObject json, Socket socket) {
		this.json = json;
		this.socket = socket;
		this.userDao = new UserDaoImpl();
	}

	/**
	 * 不能重复注册	
	 * 主要检测 手机号与 学号加上真实姓名这两项 
	 */
	@Override
	public void registerUser() {
		User user =	User.parseJsonString(json.toJSONString());
		boolean flag = userDao.save(user);
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = new JSONObject();
		sm.setMid("001");
		if (!flag) {//注册失败
			sm.setType(SocketMessage.TYPE_ERROR);			
			json.put("code", SocketMessage.REGISTER_FAILED);
			json.put("info", messages.get(SocketMessage.REGISTER_FAILED));			
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.REGISTER_SUC);
			json.put("info", messages.get(SocketMessage.REGISTER_SUC));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	@Override
	public void login() {
		String phonenumber = json.getString("phonenumber");
		String password = json.getString("password");
		User user = userDao.find(phonenumber, password);
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = null;
		sm.setMid("002");
		if (user != null) {//登录成功
			/**
			 * 登录成功的socket添加到管理列表中
			 * 后面可以推送消息
			 */
			Integer uid = Integer.parseInt(user.getUserid());
			SocketManger.addSocket(uid, socket);
			
			json = (JSONObject) JSONObject.toJSON(user);
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.LOGIN_SUC);			
			json.put("info", messages.get(SocketMessage.LOGIN_SUC));

		} else {
			json = new JSONObject();
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("code", SocketMessage.LOGIN_FAILED);
			json.put("info", messages.get(SocketMessage.LOGIN_FAILED));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	@Override
	public void resetPassword() {
		String phonenumber = json.getString("phonenumber");
		String password = json.getString("password");
		User user = userDao.findByPhone(phonenumber);
		user.setPassword(password);
		boolean flag = userDao.save(user);
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = new JSONObject();
		sm.setMid("003");
		if (flag) {//修改成功
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.RESET_SUCC);
			json.put("info", messages.get(SocketMessage.RESET_SUCC));

		} else {//修改失败
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("code", SocketMessage.RESET_FAILED);
			json.put("info", messages.get(SocketMessage.RESET_FAILED));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	@Override
	public void queryUserInfo() {
		 String phonenumber = json.getString("phonenumber");
		User user = userDao.findByPhone(phonenumber);
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = new JSONObject();
		sm.setMid("004");
		if (user != null) {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.USER_EXIST);
			json.put("info", user.toString());
		} else {
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("code", SocketMessage.USER_NOT_EXIST);
			json.put("info", messages.get(SocketMessage.USER_NOT_EXIST));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	@Override
	public void changeUserInfo() {
		User user = User.parseJsonString(json.toJSONString());
		boolean flag = userDao.save(user);
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = new JSONObject();
		sm.setMid("005");
		if (flag) {//用户信息修改成功，返回成功信息
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.RESET_SUCC);
			json.put("info", messages.get(SocketMessage.RESET_SUCC));

		} else {//用户信息修改失败，返回失败信息
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("code", SocketMessage.RESET_FAILED);
			json.put("info", messages.get(SocketMessage.RESET_FAILED));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}	

	@Override
	public void findPhonenumber() {		
		User user = userDao.findByPhone(json.getString("phonenumber"));
		sm = new SocketMessage();
		Map<Integer, String> messages = SocketMessage.getMessages();
		JSONObject json = new JSONObject();
		sm.setMid("006");
		if (user != null) {//手机号已被注册，返回失败信息
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("code", SocketMessage.PN_EXIST);
			json.put("info", messages.get(SocketMessage.PN_EXIST));

		} else {//手机号未被注册，返回成功信息
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("code", SocketMessage.PN_NOT_EXIST);
			json.put("info", messages.get(SocketMessage.PN_NOT_EXIST));
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	public void sendMessage() {
		String msg = sm.toString();
		Thread t = new Thread(new SendMsgHandler(socket, msg));
		t.start();
	}
}

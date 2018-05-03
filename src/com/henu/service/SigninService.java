package com.henu.service;

import java.net.Socket;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.henu.dao.GroupDaoImpl;
import com.henu.dao.SigninDao;
import com.henu.domain.Signin;
import com.henu.domain.SocketMessage;
import com.henu.domain.User;
import com.henu.socket.SendMsgHandler;
import com.henu.socket.SocketManger;

public class SigninService {
	private JSONObject json;
	private Socket socket;
	private SigninDao signinDao;
	private SocketMessage sm;

	public SigninService(JSONObject json, Socket socket) {
		this.json = json;
		this.socket = socket;
		this.signinDao = new SigninDao();
	}

	public void createSignin() {
		Signin sign = Signin.parseJson(this.json.toJSONString());
		String sid = signinDao.createSignin(sign);
		sm = new SocketMessage();
		sm.setMid("201");
		String msg = null;
		if (sid == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "发起签到失败！！");
			msg = json.toJSONString();
		} else {
			Signin s = signinDao.find(sid);
			/* 推送  */
			pushSignin(s);
			
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(s);
		}
		sm.setMessage(msg);
		sendMessage();
	}

	public void getSignins() {
		Integer gid = this.json.getInteger("gid");
		List<Signin> signs = signinDao.getSignins(gid);
		sm = new SocketMessage();
		sm.setMid("202");
		String msg = null;
		if (signs == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取历史签到信息失败！！");
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(signs);
		}
		sm.setMessage(msg);
		sendMessage();
	}

	// 推送签到消息
	private void pushSignin(Signin sign) {
		GroupDaoImpl groupDao = new GroupDaoImpl();
		String gid = sign.getGid().toString();
		List<User> users = groupDao.getGroupMember(gid);
		if(users==null) {
			return;
		}
		for (User user : users) {
			Integer uid = Integer.parseInt(user.getUserid());
			SocketMessage sm = new SocketMessage();
			sm.setType(SocketMessage.TYPE_SIGN);
			sm.setMessage(JSONObject.toJSONString(sign));
			SocketManger.pushToClient(uid, sm.toString());
		}
	}

	
	private void sendMessage() {
		String msg = sm.toString();
		Thread t = new Thread(new SendMsgHandler(socket, msg));
		t.start();
	}

}

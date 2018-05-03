package com.henu.service;

import java.net.Socket;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.henu.dao.SignMemberDao;
import com.henu.domain.SigninMember;
import com.henu.domain.SocketMessage;
import com.henu.socket.SendMsgHandler;

public class SignMemberService {
	
	private JSONObject json;
	private Socket socket;
	private SignMemberDao dao = new SignMemberDao();
	private SocketMessage sm;

	public SignMemberService(JSONObject json, Socket socket) {
		this.json = json;
		this.socket = socket;
	}
	
	public void signin() {
		SigninMember signin = SigninMember.parseJson(this.json.toString());
		int num = dao.signin(signin);
		sm = new SocketMessage();
		sm.setMid("203");
		String msg = null;
		if (num < 1) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "签到失败！！");
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			JSONObject json = new JSONObject();
			json.put("info", "签到成功！！");
			msg = json.toJSONString();
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	public void findMembers() {		
		Integer gid = this.json.getInteger("gid");
		List<SigninMember> members= dao.findMembers(gid);
		sm = new SocketMessage();
		sm.setMid("204");
		String msg = null;
		if (members == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取签到记录失败！！");
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(members);
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	public void findSigns() {
		Integer uid = this.json.getInteger("uid");
		List<SigninMember> members= dao.findMembers(uid);
		sm = new SocketMessage();
		sm.setMid("205");
		String msg = null;
		if (members == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取签到记录失败！！");
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(members);
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	private void sendMessage() {
		String msg = sm.toString();
		Thread t = new Thread(new SendMsgHandler(socket, msg));
		t.start();
	}
}

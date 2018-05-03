package com.henu.service;

import java.net.Socket;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.henu.dao.GroupDaoImpl;
import com.henu.dao.IGroupDao;
import com.henu.domain.Group;
import com.henu.domain.SocketMessage;
import com.henu.domain.User;
import com.henu.socket.SendMsgHandler;

public class GroupServiceImpl implements IGroupService {
	private JSONObject json;
	private Socket socket;
	private IGroupDao groupDao;
	private SocketMessage sm;
	
	public GroupServiceImpl(JSONObject json, Socket socket) {
		this.json = json;
		this.socket = socket;
		this.groupDao = new GroupDaoImpl();
	}		

	@Override
	public void createGroup() {
		Group group = Group.parseJsonString(this.json.toJSONString());
		String gid = groupDao.createGroup(group);
		sm = new SocketMessage();
		String msg = null;		
		if (gid == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			sm.setMid("101");
			json.put("info", "创建群失败！！");	
			msg = json.toJSONString();
		} else {
			Group g = groupDao.getGroup(gid);
			//群主加群
			groupDao.joinGroup(g.getGid(), g.getGowner());
			sm.setType(SocketMessage.TYPE_EVENT);
			JSONObject json = (JSONObject) JSONObject.toJSON(g);
			json.put("ID", "101");
			msg = json.toJSONString();
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	@Override
	public void updateGroup() {
		Group group = Group.parseJsonString(this.json.toJSONString());
		boolean flag = groupDao.updateGroup(group);
		sm = new SocketMessage();
		JSONObject json = new JSONObject();
		sm.setMid("102");
		if (!flag) {
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("info", "修改群信息失败！！");			
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("info", "修改群信息成功！！");
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}
	
	@Override
	public void dissGroup() {
		String gid = this.json.getString("gid");
		boolean flag = groupDao.dissGroup(gid);
		sm = new SocketMessage();
		JSONObject json = new JSONObject();
		sm.setMid("103");
		if (!flag) {
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("info", "解散群失败！！");			
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("info", "解散群成功！！");
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}

	/**
	 *注意不能重复加群
	 */
	@Override
	public void joinGroup() {
		String gid = this.json.getString("gid");
		String uid = this.json.getString("uid");
		boolean flag = groupDao.joinGroup(gid, uid);
		sm = new SocketMessage();
		JSONObject json = new JSONObject();
		sm.setMid("104");
		if (!flag) {
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("info", "加群失败！！");	
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("info", "加群成功！！");	
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}
	
	@Override
	public void exitGroup() {
		String gid = this.json.getString("gid");
		String uid = this.json.getString("uid");
		boolean flag = groupDao.exitGroup(gid, uid);
		sm = new SocketMessage();
		JSONObject json = new JSONObject();
		sm.setMid("105");
		if (!flag) {
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("info", "退群失败！！");	
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			json.put("info", "退群成功！！");	
		}
		sm.setMessage(json.toJSONString());
		sendMessage();
	}	

	@Override
	public void getGroup() {
		String gid = this.json.getString("gid");
		Group group = groupDao.getGroup(gid);
		sm = new SocketMessage();
		sm.setMid("106");
		String msg = null;
		if (group == null) {
			JSONObject json = new JSONObject();
			sm.setType(SocketMessage.TYPE_ERROR);
			json.put("info", "获取群信息失败！！");	
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(group);
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	@Override
	public void getGroupList() {
		List<Group> groups = groupDao.getGroupList();
		sm = new SocketMessage();
		sm.setMid("107");
		String msg = null;
		if (groups.size()>0) {			
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(groups);
		} else {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取群列表失败！！");
			msg = json.toJSONString();
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	@Override
	public void getGroupMember() {
		String gid = this.json.getString("gid");
		List<User> users = groupDao.getGroupMember(gid);
		sm = new SocketMessage();
		sm.setMid("108");
		String msg = null;
		if (users == null) {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取群成员列表失败！！");
			msg = json.toJSONString();
		} else {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg =JSONObject.toJSONString(users);
			json.toJSONString();
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	@Override
	public void getOwnGroups() {
		String uid = json.getString("gowner");
		List<Group> groups = groupDao.getOwnGroups(uid);
		sm = new SocketMessage();
		sm.setMid("109");
		String msg = null;
		if (groups.size()>0) {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(groups);
		} else {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("info", "获取群列表失败！！");
			msg = json.toJSONString();
		}
		sm.setMessage(msg);
		sendMessage();
	}
	
	@Override
	public void getInGroups() {
		String uid = json.getString("userid");
		List<Group> groups = groupDao.getInGroups(uid);
		sm = new SocketMessage();
		String msg = null;
		sm.setMid("110");
		if (groups.size()>0) {
			sm.setType(SocketMessage.TYPE_EVENT);
			msg = JSONObject.toJSONString(groups);
		} else {
			sm.setType(SocketMessage.TYPE_ERROR);
			JSONObject json = new JSONObject();
			json.put("ID", "110");
			json.put("info", "获取群列表失败！！");
			msg = json.toJSONString();
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

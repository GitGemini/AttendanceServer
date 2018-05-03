package com.henu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.henu.domain.SocketMessage;
import com.henu.socket.SocketHelper;
import com.henu.utils.Config;

public class Methods {
	
	public static void queryMethod(SocketMessage sm, SocketHelper sh) {		
		//1.解析Json数据，获取方法ID;
		JSONObject json =  JSON.parseObject(sm.getMessage());
		String ID = sm.getMid();
		//2.创建与用户相关的处理对象
		IUserService userService = new UserServiceImpl(json, sh.getSocket());
		IGroupService groupService = new GroupServiceImpl(json, sh.getSocket());
		SigninService signinService = new SigninService(json, sh.getSocket());
		SignMemberService memberService = new SignMemberService(json, sh.getSocket());
		
		//根据ID,匹配需要执行的方法
		//心跳数据  不做处理
		if(ID.equals("000")) {
			Config.Print("收到客户端发来的心跳数据！");
		}
		
		//100以内，为用户操作
		if(ID.equals("001")) {
			userService.registerUser();			
		}
		if(ID.equals("002")) {
			userService.login();
		}
		if(ID.equals("003")) {
			userService.resetPassword();
		}
		if(ID.equals("004")) {
			userService.queryUserInfo();
		}
		if(ID.equals("005")) {
			userService.changeUserInfo();
		}
		if(ID.equals("006")) {
			userService.findPhonenumber();
		}
		if(ID.equals("007")) {
			
		}		
		
		//100-200 ，为签到群相关操作		
		if("101".equals(ID)) {
			groupService.createGroup();
		}		
		if("102".equals(ID)) {
			groupService.updateGroup();
		}		
		if("103".equals(ID)) {
			groupService.dissGroup();
		}
		if("104".equals(ID)) {
			groupService.joinGroup();
		}
		if("105".equals(ID)) {
			groupService.exitGroup();
		}
		if("106".equals(ID)) {
			groupService.getGroup();
		}
		if("107".equals(ID)) {
			groupService.getGroupList();
		}
		if("108".equals(ID)) {
			groupService.getGroupMember();
		}
		if("109".equals(ID)) {
			groupService.getOwnGroups();
		}
		if("110".equals(ID)) {
			groupService.getInGroups();
		}
		
		if("201".equals(ID)) {
			signinService.createSignin();
		}
		if("202".equals(ID)) {
			signinService.getSignins();
		}
		
		if("203".equals(ID)) {
			memberService.signin();
		}
		if("204".equals(ID)) {
			memberService.findMembers();
		}
		if("205".equals(ID)) {
			memberService.findSigns();
		}
	}
}

package com.henu.dao;

import java.util.List;

import com.henu.domain.Group;
import com.henu.domain.User;

public interface IGroupDao {

	String createGroup(Group group);
	
	boolean updateGroup(Group group);

	List<Group> getGroupList();

	Group getGroup(String gid);

	boolean dissGroup(String gid);

	boolean joinGroup(String gid, String memberid);

	boolean exitGroup(String gid, String memberid);

	List<User> getGroupMember(String gid);

	List<Group> getOwnGroups(String uid);

	List<Group> getInGroups(String uid);
}
package junit.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.henu.dao.GroupDaoImpl;
import com.henu.dao.IGroupDao;
import com.henu.domain.Group;
import com.henu.domain.User;

public class GroupTest {
	
	@Test
	public void testList() {
		List<Group> groups = new ArrayList<>();
		
		Group group = new Group();
		group.setGname("操作系统");
		group.setGnumber(0);
		group.setGowner("2");
		group.setGicon("defaulticon.png");
		groups.add(group);
		
		Group g = new Group();
		g.setGname("asdsd");
		g.setGnumber(0);
		g.setGowner("2");
		g.setGicon("defaulticon.png");
		groups.add(g);
		
		String str = JSON.toJSONString(groups);
		JSONArray arr = JSON.parseArray(str);
		System.out.println(arr.size());
		for (Object object : arr) {
			System.out.println(object+"a");
		}
	}
	
	@Test
	public void testMakeGroup() {
		Group group = new Group();
		group.setGname("操作系统");
		group.setGnumber(0);
		group.setGowner("2");
		group.setGicon("defaulticon.png");
		IGroupDao groupDao = new GroupDaoImpl();
		groupDao.updateGroup(group);
	}
	
	@Test
	public void testUpdateGroup() {
		IGroupDao groupDao = new GroupDaoImpl();
		Group group = groupDao.getGroup("2");
		group.setGicon("asdd");
		groupDao.updateGroup(group);
	}
	
	@Test
	public void testDissGroup() {
		IGroupDao groupDao = new GroupDaoImpl();
		boolean num = groupDao.dissGroup("2");
		System.out.println(num);
	}
	
	@Test
	public void testGetGroup() {
		Group group = null;
		IGroupDao groupDao = new GroupDaoImpl();
		group = groupDao.getGroup("2");
		System.out.println(group);
	}
	
	@Test
	public void testJoinGroup() {
		IGroupDao groupDao = new GroupDaoImpl();
		boolean n = groupDao.joinGroup("3", "1");
		System.out.println(n);
	}
	
	@Test
	public void testExitGroup() {
		IGroupDao groupDao = new GroupDaoImpl();
		boolean n = groupDao.exitGroup("3", "1");
		System.out.println(n);
	}
	
	@Test
	public void testGetMembers() {
		IGroupDao groupDao = new GroupDaoImpl();
		List<User> users = groupDao.getGroupMember("3");
		System.out.println(users);
	}
}

package com.henu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.henu.domain.Group;
import com.henu.domain.User;
import com.henu.utils.JdbcUtils;

public class GroupDaoImpl implements IGroupDao {

	@Override
	public String createGroup(Group group) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String newId = null;
		String command = "insert into signgroup(gname, gowner, gnumber, gicon) values(?,?,?,?);";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, group.getGname());
			stat.setString(2, group.getGowner());
			stat.setInt(3, group.getGnumber());
			stat.setString(4, group.getGicon());
			int num = stat.executeUpdate();

			if (num > 0) {// 获取刚刚生成的id
				String sql = "select LAST_INSERT_ID()";
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet nrs = st.executeQuery();
				if (nrs.next()) {
					newId = nrs.getString(1);
				}
				JdbcUtils.release(conn, st, nrs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return newId;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#updateGroup(com.henu.model.Group)
	 */
	@Override
	public boolean updateGroup(Group group) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;
		String command = "update signgroup set gname =?, gowner=?, gnumber=?, gicon=? where gid = ?";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, group.getGname());
			stat.setString(2, group.getGowner());
			stat.setInt(3, group.getGnumber());
			stat.setString(4, group.getGicon());
			stat.setString(5, group.getGid());
			int num = stat.executeUpdate();
			if (num > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return flag;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#getGroupList()
	 */
	@Override
	public List<Group> getGroupList() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<Group> groups = new ArrayList<Group>();

		String command = "select * from signgroup";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			rs = stat.executeQuery();
			while (rs.next()) {
				Group group = Group.ResultSet2Group(rs);
				groups.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return groups;
	}

	@Override
	public List<Group> getOwnGroups(String uid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<Group> groups = null;
		
		String command = "select * from signgroup where gowner = ?";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, uid);
			rs = stat.executeQuery();
			groups = new ArrayList<Group>();
			while (rs.next()) {
				Group user = Group.ResultSet2Group(rs);
				groups.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return groups;
	}
	
	@Override
	public List<Group> getInGroups(String uid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<Group> groups = null;
		
		String command = "select gid from groupmember where memberid = ?";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, uid);
			rs = stat.executeQuery();
			List<String> gs = new ArrayList<String>();
			while (rs.next()) {
				gs.add(rs.getString(1));
			}
			if (gs.size() > 0) {
				// 初始化用户集合
				groups = new ArrayList<Group>();

				// 拼接查找用户的sql语句
				StringBuffer sql = new StringBuffer("select * from signgroup where gid in (");
				for (int i = 0; i < gs.size(); i++) {
					sql.append("?,");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(")");
				PreparedStatement st = conn.prepareStatement(sql.toString());
				for (int i = 0; i < gs.size(); i++) {// 填充参数
					st.setString(i + 1, gs.get(i));
				}
				ResultSet urs = st.executeQuery();
				while (urs.next()) {
					Group user = Group.ResultSet2Group(urs);
					groups.add(user);
				}
				JdbcUtils.release(null, st, urs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return groups;
	}
	/*
	 * @see com.henu.dao.impl.IGroupDao#getGroup(java.lang.String)
	 */
	@Override
	public Group getGroup(String gid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		Group group = null;

		String command = "select * from signgroup where gid = ? ";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, gid);
			rs = stat.executeQuery();
			if (rs.next()) {
				group = Group.ResultSet2Group(rs);

			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return group;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#dissGroup(java.lang.String)
	 */
	@Override
	public boolean dissGroup(String gid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		String sql = "delete from groupmember where gid = ? ";

		try {// 先将群中的成员都删除
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(sql);
			stat.setString(1, gid);
			int num = stat.executeUpdate();
			if (num > 0) {// 群成员删除完之后，再将表删除
				String command = "delete from signgroup where gid = ? ";
				PreparedStatement st = conn.prepareStatement(command);
				st.setString(1, gid);
				int n = st.executeUpdate();
				if (n > 0) {
					flag = true;
					st.close();
				} else {
					throw new RuntimeException("解散群失败！！！");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return flag;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#joinGroup(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean joinGroup(String gid, String memberid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		// 1.将新用户插入到群成员表中
		String command = "insert into groupmember(gid, memberid) values(?, ?)";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, gid);
			stat.setString(2, memberid);
			int num = stat.executeUpdate();
			if (num > 0) {// 插入成功，则更新signgroup表中成员数
				String sql = "update signgroup set gnumber = gnumber+1 where gid = ?";
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, gid);
				int n = st.executeUpdate();
				if (n == 0) {
					throw new RuntimeException("加入群失败！！！");
				} else {
					flag = true;
					st.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return flag;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#exitGroup(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean exitGroup(String gid, String memberid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		boolean flag = false;

		// 1.将用户从到群成员表中删除
		String command = "delete from groupmember where (gid = ? and memberid = ?)";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, gid);
			stat.setString(2, memberid);
			int num = stat.executeUpdate();
			if (num > 0) {// 删除成功，则更新signgroup表中成员数
				String sql = "update signgroup set gnumber = gnumber-1 where gid = ?";
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, gid);
				int n = st.executeUpdate();
				if (n == 0) {
					throw new RuntimeException("退出群失败！！！");
				} else {
					flag = true;
					st.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return flag;
	}

	/*
	 * @see com.henu.dao.impl.IGroupDao#getGroupMember(java.lang.String)
	 */
	@Override
	public List<User> getGroupMember(String gid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<User> users = null;
		String command = "select memberid from groupmember where gid = ?";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, gid);
			rs = stat.executeQuery();
			List<String> members = new ArrayList<String>();
			while (rs.next()) {
				members.add(rs.getString(1));
			}
			System.out.println(members);
			if (members.size() > 0) {
				// 初始化用户集合
				users = new ArrayList<User>();

				// 拼接查找用户的sql语句
				StringBuffer sql = new StringBuffer("select * from student where userid in (");
				for (int i = 0; i < members.size(); i++) {
					sql.append("?,");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(")");
				PreparedStatement st = conn.prepareStatement(sql.toString());
				for (int i = 0; i < members.size(); i++) {// 填充参数
					st.setString(i + 1, members.get(i));
				}
				ResultSet urs = st.executeQuery();
				while (urs.next()) {
					User user = User.ResultSet2User(urs);
					users.add(user);
				}
				JdbcUtils.release(null, st, urs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return users;
	}	
}

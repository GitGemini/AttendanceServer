package com.henu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.henu.domain.User;
import com.henu.utils.JdbcUtils;

public class UserDaoImpl implements IUserDao{
	/* (non-Javadoc)
	 * @see com.henu.dao.impl.IUserDao#save(com.henu.model.User)
	 */
	@Override
	public boolean save(User user) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String command = null;
		boolean flag = false;
		
		if(user.getUserid()==null) {//注册
			command = "insert into student(username, password, realname, studentid, phonenumber, usericon) values(?,?,?,?,?,?)";
		}else {
		    command = "update student set username=?, password=?, realname=?, studentid=?, phonenumber=?, usericon=? where userid=?";
		}	
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, user.getUsername());
			stat.setString(2, user.getPassword());
			stat.setString(3, user.getRealname());
			stat.setString(4, user.getStudentid());
			stat.setString(5, user.getPhonenumber());
			stat.setString(6, user.getUsericon()); 
			if(user.getUserid()!=null) {
				stat.setString(7, user.getUserid()); 
			}
			int num = stat.executeUpdate();
			if(num > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.henu.dao.impl.IUserDao#find(java.lang.String, java.lang.String)
	 */
	@Override
	public User find(String phonenumber, String password){
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		User user = null;

		String command = "select * from student where (phonenumber = ? and password = ?)";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, phonenumber);
			stat.setString(2, password);
			rs = stat.executeQuery();
			if (rs.next()) {
				// 取出查询到的用户信息
				user = User.ResultSet2User(rs);				
			} 
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return user;
	}

	/* (non-Javadoc)
	 * @see com.henu.dao.impl.IUserDao#queryPhoneNumber(java.lang.String)
	 */
	@Override
	public User findByPhone(String phonenumber) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		User user = null;
		
		String command = "select * from student where phonenumber = ?";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, phonenumber);
			rs = stat.executeQuery();
			if (rs.next()) {
				user = User.ResultSet2User(rs);
			} 
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return user;
	}
}

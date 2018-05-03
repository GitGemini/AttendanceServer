package com.henu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.henu.domain.SigninMember;
import com.henu.utils.JdbcUtils;

public class SignMemberDao {
	public int signin(SigninMember signin) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		int num = 0;
		String command = "insert into signinmember(gid, receiver,time rlogintude, rlatitude,result) values(?,?,?,?,?,?);";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, signin.getGid());
			stat.setInt(2, signin.getReceiver());
			stat.setString(3, signin.getTime());
			stat.setString(4, signin.getRlogintude());
			stat.setString(5, signin.getRlatitude());
			stat.setInt(6, signin.getResult());
			num = stat.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return num;
	}
	
	public List<SigninMember> findMembers(Integer gid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<SigninMember> members = new ArrayList<SigninMember>();

		String command = "select * from signinmember where gid = ? ";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, gid);
			rs = stat.executeQuery();
			while (rs.next()) {
				SigninMember signin = SigninMember.ResultSet2SigninMember(rs);
				members.add(signin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return members;
	}
	
	public List<SigninMember> findSigns(Integer uid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<SigninMember> members = new ArrayList<SigninMember>();

		String command = "select * from signinmember where receiver = ? ";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, uid);
			rs = stat.executeQuery();
			while (rs.next()) {
				SigninMember signin = SigninMember.ResultSet2SigninMember(rs);
				members.add(signin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return members;
	}
}

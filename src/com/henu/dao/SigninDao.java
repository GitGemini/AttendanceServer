package com.henu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.henu.domain.Signin;
import com.henu.utils.JdbcUtils;

public class SigninDao {
	public String createSignin(Signin sign) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String newId = null;
		String command = "insert into signin(originator, gid, time, logintude, latitude,region,state) values(?,?,?,?,?,?,?);";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, sign.getOriginator());
			stat.setInt(2, sign.getGid());
			stat.setString(3, sign.getTime());
			stat.setString(4, sign.getLogintude());
			stat.setString(5, sign.getLatitude());
			stat.setString(6, sign.getRegion());
			stat.setBoolean(7, sign.getState());
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
	
	public boolean save(Signin sign) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String command = "update signin set originator = ?, gid=?, time=?, logintude=?, latitude=?,region=?,state=? where id = ?";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, sign.getOriginator());
			stat.setInt(2, sign.getGid());
			stat.setString(3, sign.getTime());
			stat.setString(4, sign.getLogintude());
			stat.setString(5, sign.getLatitude());
			stat.setString(6, sign.getRegion());
			stat.setBoolean(7, sign.getState());

			stat.setInt(8, sign.getId());
			int num = stat.executeUpdate();

			if (num > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return false;
	}
	
	public Signin find(String id) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		Signin sign = null;

		String command = "select * from signin where id = ? ";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setString(1, id);
			rs = stat.executeQuery();
			if (rs.next()) {
				sign = Signin.ResultSet2Signin(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return sign;
	}
	
	public List<Signin> getSignins(Integer gid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		List<Signin> members = new ArrayList<Signin>();

		String command = "select * from signin where gid = ? ";

		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, gid);
			rs = stat.executeQuery();
			if (rs.next()) {
				Signin signin = Signin.ResultSet2Signin(rs);
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

package com.henu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.henu.domain.TsMessage;
import com.henu.utils.JdbcUtils;

public class TsMessageDao {
	public boolean add(TsMessage tm) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String command = "insert into tsmessage(type,sender,receiver, content,state) values(?,?,?,?,?);";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, tm.getType());
			stat.setInt(2, tm.getSender());
			stat.setInt(3, tm.getReceiver());
			stat.setString(4, tm.getContent());
			stat.setString(5, tm.getState());
			int num = stat.executeUpdate();

			if (num > 0) {// 获取刚刚生成的id
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return false;
	}

	public boolean save(TsMessage tm) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String command = "update tsmessage set type=?,sender=?, receiver=?, content=?,state=?) where id=?;";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, tm.getType());
			stat.setInt(2, tm.getSender());
			stat.setInt(3, tm.getReceiver());
			stat.setString(4, tm.getContent());
			stat.setString(5, tm.getState());
			stat.setInt(6, tm.getId());
			int num = stat.executeUpdate();

			if (num > 0) {// 获取刚刚生成的id
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return false;
	}
	
	/*
	 *
	 */
	public List<TsMessage> findByUser(Integer uid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		
		List<TsMessage> tms = new ArrayList<TsMessage>();
		String command = "select * from tsmessage where receiver=?;";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, uid);
			rs = stat.executeQuery();
			
			while (rs.next()) {
				TsMessage tm = TsMessage.ResultSet2TsMessage(rs);
				tms.add(tm);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return tms;
	}
	
	public TsMessage findById(Integer tid) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		
		TsMessage tm = null;
		String command = "select * from tsmessage where id=?;";
		try {
			conn = JdbcUtils.getConnection();
			stat = conn.prepareStatement(command);
			stat.setInt(1, tid);
			rs = stat.executeQuery();
			
			if (rs.next()) {
				tm = TsMessage.ResultSet2TsMessage(rs);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(conn, stat, rs);
		}
		return tm;
	}
}

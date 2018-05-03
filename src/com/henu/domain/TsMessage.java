package com.henu.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TsMessage {
	private Integer id;
	private Integer type;
	private Integer sender;
	private Integer receiver;
	private String content;
	private String state;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSender() {
		return sender;
	}
	public void setSender(Integer sender) {
		this.sender = sender;
	}
	public Integer getReceiver() {
		return receiver;
	}
	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public static TsMessage ResultSet2TsMessage(ResultSet rs) {
		TsMessage tm = new TsMessage();
		try {
			tm.setId(rs.getInt(1));
			tm.setType(rs.getInt(2));
			tm.setSender(rs.getInt(3));
			tm.setReceiver(rs.getInt(4));
			tm.setContent(rs.getString(5));
			tm.setState(rs.getString(6));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return tm;
	}
}

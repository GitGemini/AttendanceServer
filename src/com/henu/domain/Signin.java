package com.henu.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSONObject;

public class Signin {
	private Integer id;
	private Integer originator;
	private Integer gid;
	private String time;
	private String logintude;
	private String latitude;
	private String region;
	private Boolean state;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINESE);

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOriginator() {
		return originator;
	}

	public void setOriginator(Integer originator) {
		this.originator = originator;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLogintude() {
		return logintude;
	}

	public void setLogintude(String logintude) {
		this.logintude = logintude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public static Signin ResultSet2Signin(ResultSet rs) {
		Signin sign = new Signin();
		try {
			sign.setId(rs.getInt(1));
			sign.setOriginator(rs.getInt(2));
			sign.setGid(rs.getInt(3));
			sign.setTime(rs.getString(4));
			sign.setLatitude(rs.getString(5));
			sign.setLogintude(rs.getString(6));
			sign.setRegion(rs.getString(7));
			sign.setState(rs.getBoolean(8));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return sign;
	}

	public static Signin parseJson(String jsonStr) {
		Signin sign = new Signin();
		JSONObject json = JSONObject.parseObject(jsonStr);
		sign.setId(json.getInteger("id"));
		sign.setOriginator(json.getInteger("originator"));
		sign.setGid(json.getInteger("gid"));

		String time = json.getString("time");
		if (time == null) {
			sign.setTime(sdf.format(new Date()));
		} else {
			sign.setTime(time);
		}
		sign.setLatitude(json.getString("latitude"));
		sign.setLogintude(json.getString("logintude"));
		String region = json.getString("region");
		if(region==null) {
			sign.setRegion(region);
		}else {
			sign.setRegion("20");
		}
		Boolean state = json.getBoolean("time");
		if (state == null) {
			sign.setState(false);
		} else {
			sign.setState(state);
		}
		return sign;
	}

	// 判断签到是否过期
	public boolean isTimeValid(SigninMember sign) {
		if (this.getState()) {
			return false;
		}
		String src = this.getTime();
		String des = sign.getTime();
		Date time = null;
		Date now = null;
		try {
			time = sdf.parse(src);
		} catch (ParseException e) {
			time = new Date();
		}
		try {
			now = sdf.parse(des);
		} catch (ParseException e) {
			now = new Date();
		}
		long diff = now.getTime() - time.getTime();
		int dif = (int) (diff / 1000 / 60);
		if (dif <= 30 && diff >= 0) {
			return true;
		}
		return false;
	}

	// 使签到过期
	public void invalid(Signin sign) {

	}

}

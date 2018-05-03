package com.henu.service;

import java.util.List;

import com.henu.dao.TsMessageDao;
import com.henu.domain.TsMessage;

public class TsMessageService {
	private TsMessageDao dao = new TsMessageDao();

	public boolean add(TsMessage tm) {
		return dao.add(tm);
	}

	public boolean save(TsMessage tm) {
		return dao.save(tm);
	}

	public List<TsMessage> findByUser(Integer uid) {
		return dao.findByUser(uid);
	}

	public TsMessage findById(Integer tid) {
		return dao.findById(tid);
	}
}

package com.dsw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.dsw.constant.StatusCode;
import com.dsw.dao.PeopleDao;
import com.dsw.model.People;
import com.dsw.util.DBConnection;

public class PeopleDaoImpl implements PeopleDao {
	private static PeopleDaoImpl pdi = new PeopleDaoImpl();

	private PeopleDaoImpl() {

	}

	public static PeopleDaoImpl getInstance() {
		return pdi;
	}

	/**
	 * 插入人员定位信息
	 */
	@Override
	public StatusCode insertPeopleAddr(People people) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.getConnertion();
			ps = conn.prepareStatement("insert into person_trace(tag_euid,content) values(?,?)");
			ps.setString(1, people.getTagEuid());
			ps.setString(2, people.getZoonName());
			int flag = ps.executeUpdate();
			if (flag == 1) {
				return StatusCode.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DBConnection.free(ps, conn);
		}
		return StatusCode.FAIl;
	}

	/**
	 * 查询人员信息有无变动
	 */
	@Override
	public boolean checkFinalPeopleAddr(String euid, String content) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnertion();
			ps = conn
					.prepareStatement("select content from person_trace where tag_euid=? order by create_time desc limit 1");
			ps.setString(1, euid);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (content.equals(rs.getString(1))) {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DBConnection.free(rs, ps, conn);
		}
		return true;
	}

	@Override
	public boolean checkPeopleStatus(String euid) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnertion();
			ps = conn.prepareStatement("select is_activate from card_manage where tag_euid=?");
			ps.setString(1, euid);
			rs = ps.executeQuery();
			if (rs.next()) {
				if ("1".equals(rs.getString(1))) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DBConnection.free(rs, ps, conn);
		}
		return false;
	}

}

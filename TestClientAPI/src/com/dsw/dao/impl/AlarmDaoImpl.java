package com.dsw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dsw.constant.StatusCode;
import com.dsw.dao.AlarmDao;
import com.dsw.model.Alarm;
import com.dsw.util.DBConnection;

public class AlarmDaoImpl implements AlarmDao {
	private static AlarmDaoImpl adi = new AlarmDaoImpl();

	private AlarmDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	public static AlarmDaoImpl getInstance() {
		return adi;
	}

	@Override
	public StatusCode checkAlarm(Alarm alarm) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBConnection.getConnertion();
		try {
			ps = conn.prepareStatement("select is_activate from card_manage where tag_euid = ?");
			ps.setString(1, alarm.getTag_euid());
			rs = ps.executeQuery();
			if (rs.next()) {
				String activeFlag = rs.getString("is_activate");
				if ("1".equals(activeFlag)) {
					// 如果已激活手环
					if (isActive(alarm)) {
						return StatusCode.SUCCESS;
					} else {
						return StatusCode.FAIl;
					}
				}
			}

			return StatusCode.FAIl;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("checkAlarm()方法异常");
		} finally {
			DBConnection.free(rs, ps, conn);
		}
		// 否则均返回失败！
		return StatusCode.FAIl;
	}

	private boolean isActive(Alarm alarm) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBConnection.getConnertion();
		try {
			ps = conn.prepareStatement(
					"select create_time from alarm_manage where tag_euid= ? order by create_time desc limit 1");
			ps.setString(1, alarm.getTag_euid());
			rs = ps.executeQuery();

			if (rs.next()) {
				java.sql.Timestamp ts = rs.getTimestamp(1);
				if (compareTime(ts, alarm.getCreate_time())) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("isActive()方法异常");
		}
		return false;
	}

	@Override
	public StatusCode insertAlarm(Alarm alarm) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DBConnection.getConnertion();
		try {
			ps = conn.prepareStatement(
					"insert into alarm_manage(alarm_type,alarm_level,content,tag_euid,create_time) values('1','3',?,?,?)");
			ps.setString(1, alarm.getContent());
			ps.setString(2, alarm.getTag_euid());
			ps.setTimestamp(3, alarm.getCreate_time());
			if (ps.executeUpdate() >= 1) {
				return StatusCode.SUCCESS;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.free(ps, conn);
		}
		return StatusCode.FAIl;
	}

	private boolean compareTime(java.sql.Timestamp ts1, java.sql.Timestamp ts2) {
		long dif = ts2.getTime() - ts1.getTime();
		if ((dif / 1000) > 180) {
			return true;
		}
		return false;
	}

}

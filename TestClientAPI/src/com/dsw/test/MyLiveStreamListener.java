package com.dsw.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import com.dsw.model.Alarm;
import com.dsw.model.People;
import com.dsw.thread.PeopleDaoThread;
import com.dsw.util.PackageUtil;
import com.dsw.util.PropUtil;
import com.idolink.rtls.client.api.listener.LiveStreamListener;
import com.idolink.rtls.client.api.vo.LiveStreamError;
import com.idolink.rtls.client.api.vo.LiveStreamPosition;

public class MyLiveStreamListener implements LiveStreamListener {
	private static final String ROOM_NAME = PropUtil.getInstance().getProperty("ALARM_ROOM");

	@Override
	public void liveStreamPosition(LiveStreamPosition paramLiveStreamPosition) {
		peopleTrail(paramLiveStreamPosition);
		try {
			peopleAlarm(paramLiveStreamPosition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void liveStreamError(LiveStreamError paramLiveStreamError) {

	}

	@Override
	public void liveStreamPlayback(LiveStreamPosition paramLiveStreamPosition) {
		System.out.println("liveStreamPlayback");
	}

	/**
	 * 人员轨迹处理方法
	 * 
	 * @param paramLiveStreamPosition
	 */
	private void peopleTrail(LiveStreamPosition paramLiveStreamPosition) {
		if (paramLiveStreamPosition.getErrorCase() == 0) {
			People people = PackageUtil.getPeople(paramLiveStreamPosition);
			System.out.println(people.getZoonName());
			;
			try {
				PeopleDaoThread peopleThread = new PeopleDaoThread(people);
				peopleThread.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void peopleAlarm(LiveStreamPosition paramLiveStreamPosition) throws Exception {
		if (paramLiveStreamPosition.getErrorCase() == 0) {
			if (ROOM_NAME.equals(paramLiveStreamPosition.getZoneName())) {
				Alarm alarm = new Alarm();
				alarm.setContent("未经授权进入了" + ROOM_NAME);
				alarm.setCreate_time(new Timestamp(new Date().getTime()));
				alarm.setTag_euid(paramLiveStreamPosition.getTagEuid());

				Properties prop = PropUtil.getInstance();

				com.dsw.thread.AlarmListener listener = new com.dsw.thread.AlarmListener();
				listener.setAlarmContent(alarm);
				// listener.sendMessage(prop.getProperty("ALARM_IP"),
				// prop.getProperty("ALARM_PORT"),
				// prop.getProperty("ALARM_ADDRESS"), 1, true);

			}
		}
	}
}

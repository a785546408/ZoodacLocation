package com.dsw.dao;
/**
 * people dao层接口
 * @author hlr
 *
 */

import com.dsw.constant.StatusCode;
import com.dsw.model.People;

public interface PeopleDao {
	/**
	 * 新增人员定位信息
	 * @param people 人员对象
	 * @return PeopleStatusCode
	 */
	StatusCode insertPeopleAddr(People people);

	/**
	 * 返回人员最后一次定位信息
	 * 
	 * @param id 手环euid
	 * @param content 内容
	 * @return boolean
	 */
	boolean checkFinalPeopleAddr(String euid,String content);

	/**
	 * 查询手环激活状态
	 * 
	 * @param id 手环euid
	 * @return boolean
	 */
	boolean checkPeopleStatus(String id);
}

package com.cspticw.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: CollectRecordService
 * @author: StarFall
 * @date: 2018年5月17日 下午3:27:25
 * @Description:
 */
public interface CollectRecordService {

	/**
	 * 查询是否存在企业记录
	 * 
	 * @param stuId
	 * @param jobId
	 * @param compId
	 * @param resumeId
	 * @param collectUser
	 * @return
	 */
	boolean selectRecordByCompany(Long compId, Long resumeId, int collectUser);

	boolean selectRecordByStudent(Long stuId, Long jobId, int collectUser);

	/**
	 * 新增收藏记录
	 * 
	 * @param stuId
	 * @param jobId
	 * @param compId
	 * @param resumeId
	 * @param collectUser
	 *            0学生发起，1企业发起
	 * @return
	 */
	boolean addCollectRecord(Long stuId, Long jobId, Long compId, Long resumeId, int collectUser);

	/**
	 * 获取收藏记录
	 * 
	 * @param stuId
	 * @param compId
	 * @param collectUser
	 * @return
	 */
	List<JSONObject> getCollectRecord(Long stuId, Long compId, Integer collectUser);

	boolean deleteCollectRecord(Long recordId);
}

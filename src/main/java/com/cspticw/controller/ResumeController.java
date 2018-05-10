package com.cspticw.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.cspticw.service.ResumeService;
import com.cspticw.util.CommonUtils;
import com.cspticw.util.tools.Constants;
import com.cspticw.util.tools.ErrorCode;

/**
 * @ClassName: ResumeController
 * @author: StarFall
 * @date: 2018年5月5日 下午6:24:18
 * @Description:简历
 */
@RestController
public class ResumeController extends BaseController {

	@Autowired
	private ResumeService resumeService;

	/**
	 * 添加简历
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add_resume", method = RequestMethod.POST)
	public Map<String, Object> addResume(
			@RequestParam(value = "image_file", required = false) MultipartFile imageFile,
			@RequestParam(value = "annex_resume", required = false) MultipartFile annexResume,
			@RequestParam(value = "jsonData") String jsonData) {
		Map<String, Object> returnMap = new HashMap<>();
		// 父路径/upfile/student/1
		String path = getUpFilePath(Constants.STUDENT_USER);
		String imageFileName = null;
		String annexResumeName = null;
		try {
			// 图片文件
			if (imageFile != null && !imageFile.isEmpty()) {
				imageFileName = CommonUtils.getUUID();
				String fileName = imageFile.getOriginalFilename();
				if (fileName.indexOf(".") >= 0) {
					// 后缀
					imageFileName = imageFileName + fileName.substring(fileName.lastIndexOf("."));
				}
				imageFile.transferTo(new File(path + File.separator + imageFileName));
			}
			// 简历附件
			if (annexResume != null && !annexResume.isEmpty()) {
				// 简历文件暂不重命名
				annexResumeName = annexResume.getOriginalFilename();
				annexResume.transferTo(new File(path + File.separator + annexResumeName));
				/*
				 * annexResumeName = CommonUtils.getUUID(); String fileName =
				 * annexResume.getOriginalFilename(); if (fileName.indexOf(".")
				 * >= 0) { // 后缀 annexResumeName = annexResumeName +
				 * fileName.substring(fileName.lastIndexOf(".")); }
				 * annexResume.transferTo(new File(path + File.separator +
				 * annexResumeName));
				 */
			}

		} catch (Exception e) {
			// 上传文件错误
			returnMap.put(Constants.ERROR, ErrorCode.ERROR_UP_FILE);
			return returnMap;
		}
		boolean flag = resumeService.addResume(getStuUserInfo().getId(), imageFileName,
				annexResumeName, jsonData);
		if (flag) {
			returnMap.put(Constants.MSG, Constants.SUCCESS);
		} else {
			returnMap.put(Constants.ERROR, ErrorCode.ERROR_ADD_RESUME);
		}
		return returnMap;
	}

	/**
	 * 获取我的简历详细信息List
	 * 
	 * @return
	 */
	@RequestMapping("/get_myresume_detail_list")
	public Map<String, Object> getMyRsumeDetailList() {
		Map<String, Object> returnMap = new HashMap<>();
		List<JSONObject> myResumeDetailList = resumeService.getMyResumeDetailList(getStuUserInfo());
		returnMap.put("data", myResumeDetailList);
		return returnMap;
	}

	/**
	 * 获取简历详细信息
	 * 
	 * @return
	 */

	@RequestMapping("/get_resume_detail")
	public Map<String, Object> getResumeDetail() {
		Map<String, Object> returnMap = new HashMap<>();
		System.out.println("获取简历详细信息/get_resume_detail");
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		// 简历ID
		Long id = Long.valueOf((String) session.getAttribute(Constants.RESUME_ID));
		JSONObject resumeDetail = resumeService.getResumeDetail(id);
		returnMap.put("data", resumeDetail);
		return returnMap;
	}

	/**
	 * 更新简历
	 * 
	 * @param status
	 *            状态
	 * @param resumeId
	 *            简历Id
	 * @return
	 */
	@RequestMapping(value = "/update_resume", method = RequestMethod.POST)
	public Map<String, Object> updateResumeStatus(
			@RequestParam(value = "image_file", required = false) MultipartFile imageFile,
			@RequestParam(value = "annex_resume", required = false) MultipartFile annexResume,
			@RequestParam(value = "jsonData") String jsonData) {
		Map<String, Object> returnMap = new HashMap<>();
		String imageFileName = null;// 图片文件名
		String annexResumeName = null;// 简历附件名
		try {
			// 父路径/upfile/student/1
			String path = getUpFilePath(Constants.STUDENT_USER);
			// 图片文件
			if (imageFile != null && !imageFile.isEmpty()) {
				// 图片文件重命名
				imageFileName = CommonUtils.getUUID();
				String fileName = imageFile.getOriginalFilename();
				if (fileName.indexOf(".") >= 0) {
					// 后缀
					imageFileName = imageFileName + fileName.substring(fileName.lastIndexOf("."));
				}
				imageFile.transferTo(new File(path + File.separator + imageFileName));
			}
			// 简历附件
			if (annexResume != null && !annexResume.isEmpty()) {
				// 简历文件暂不重命名
				annexResumeName = annexResume.getOriginalFilename();
				annexResume.transferTo(new File(path + File.separator + annexResumeName));
			}
		} catch (Exception e) {
			// 上传文件错误
			returnMap.put(Constants.ERROR, ErrorCode.ERROR_UP_FILE);
			return returnMap;
		}
		System.out.println(jsonData);
		boolean flag = resumeService.updateResume(imageFileName, annexResumeName, jsonData);
		if (flag) {
			returnMap.put(Constants.MSG, Constants.SUCCESS);
		} else {
			returnMap.put(Constants.ERROR, ErrorCode.ERROR_UPDATE_RESUME);
		}
		return returnMap;
	}

	/**
	 * 保存更新的Id
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save_resume_id", method = RequestMethod.POST)
	public Map<String, Object> saveResumeId(@RequestBody String id) {
		Map<String, Object> returnMap = new HashMap<>();
		System.out.println("保存更新的Id/save_resume_id");
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute(Constants.RESUME_ID, id);
		returnMap.put(Constants.MSG, Constants.SUCCESS);
		return returnMap;
	}

	/**
	 * 删除简历
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete_resume", method = RequestMethod.POST)
	public Map<String, Object> deleteResume(@RequestBody String id) {
		Map<String, Object> returnMap = new HashMap<>();
		boolean flag = resumeService.deleteResume(Long.valueOf(id));
		if (flag) {
			returnMap.put(Constants.MSG, Constants.SUCCESS);
		} else {
			returnMap.put(Constants.ERROR, ErrorCode.ERROR_DELETE_RESUME);
		}
		return returnMap;
	}
}
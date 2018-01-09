package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.AssignAuthRequest;
import com.mdvns.mdvn.common.bean.RemoveAuthRequest;
import com.mdvns.mdvn.common.bean.RtrvStaffAuthInfoRequest;
import com.mdvns.mdvn.common.bean.model.StaffAuthInfo;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限工具类
 * 
 * @author Administrator
 *
 */
public class StaffAuthUtil {
	private static final Logger LOG = LoggerFactory.getLogger(StaffAuthUtil.class);

	/**
	 * 分配权限
	 * 
	 * @param restTemplate
	 * @param assignAuthRequest
	 * @return
	 */
//	public static List<StaffAuthInfo> assignAuth(RestTemplate restTemplate, AssignAuthRequest assignAuthRequest) throws BusinessException {
//		ResponseEntity<StaffAuthInfo[]> responseEntity = null;
//		//10014
//		String assignAuthUrl = "http://localhost:10014/mdvn-staff-papi/staff/assignAuth";
//		try {
//			responseEntity = restTemplate.postForEntity(assignAuthUrl, assignAuthRequest, StaffAuthInfo[].class);
//		} catch (Exception ex) {
//			LOG.error("添加权限失败:{}", ex.getLocalizedMessage());
//			throw new BusinessException(ErrorEnum.ASSIGN_AUTH_FAILED, "添加权限失败.");
//		}
//		LOG.info("添加权限完成：{}", responseEntity.getBody().toString());
//		return Arrays.asList(responseEntity.getBody());
//	}

    /**
     * 分配权限
     * @param assignAuthUrl
     * @param assignAuthRequest
     * @return
     * @throws BusinessException
     */
	public static List<StaffAuthInfo> assignAuth(String assignAuthUrl, AssignAuthRequest assignAuthRequest) throws BusinessException{
	    RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> responseEntity ;
        try {
            responseEntity = restTemplate.postForEntity(assignAuthUrl,assignAuthRequest,List.class);

        } catch (Exception ex) {
            LOG.error("添加权限失败:{}", ex.getLocalizedMessage());
            throw new BusinessException(ErrorEnum.ASSIGN_AUTH_FAILED, "添加权限失败.");
        }
        LOG.info("添加权限完成：{}", responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

	/**
	 * 获取员工在项目中的权限信息
	 * 
	 * @param restTemplate
	 * @param projId
	 * @param hierarchyId
	 * @param staffId
	 * @return
	 */
//	public static List<StaffAuthInfo> rtrvStaffAuthInfo(RestTemplate restTemplate, String projId, String hierarchyId,
//			String staffId) {
//		//10014
//		String rtrvStaffAuthUrl = "http://localhost:10014/mdvn-staff-papi/staff/rtrvAuth";
//		RtrvStaffAuthInfoRequest rtrvStaffAuthInfoRequest = new RtrvStaffAuthInfoRequest();
//		rtrvStaffAuthInfoRequest.setProjId(projId);
//		rtrvStaffAuthInfoRequest.setStaffId(staffId);
//		rtrvStaffAuthInfoRequest.setHierarchyId(hierarchyId);
//		ResponseEntity<StaffAuthInfo[]> responseEntity = restTemplate.postForEntity(rtrvStaffAuthUrl,
//				rtrvStaffAuthInfoRequest, StaffAuthInfo[].class);
//
//		StaffAuthInfo[] staffAuthInfos = null;
//		if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//			staffAuthInfos = responseEntity.getBody();
//		}
//		return Arrays.asList(staffAuthInfos);
//	}

    /**
     * 获取员工在项目中的权限信息
     * @param rtrvStaffAuthUrl
     * @param projId
     * @param hostId
     * @param staffId
     * @return
     * @throws BusinessException
     */
	public static List<StaffAuthInfo> rtrvStaffAuthInfo(String rtrvStaffAuthUrl, Long projId, Long hostId, Long staffId) throws BusinessException{
       RestTemplate restTemplate = new RestTemplate();
       ResponseEntity<List> responseEntity ;
       RtrvStaffAuthInfoRequest rtrvStaffAuthInfoRequest = new RtrvStaffAuthInfoRequest();
       rtrvStaffAuthInfoRequest.setProjId(projId);
       rtrvStaffAuthInfoRequest.setHostId(hostId);
       rtrvStaffAuthInfoRequest.setStaffId(staffId);
        try {
            responseEntity = restTemplate.postForEntity(rtrvStaffAuthUrl,rtrvStaffAuthInfoRequest,List.class);

        } catch (Exception ex) {
            LOG.error("获取权限失败:{}", ex.getLocalizedMessage());
            throw new BusinessException(ErrorEnum.RTRV_AUTH_FAILED, "获取权限失败.");
        }
        LOG.info("获取权限完成：{}", responseEntity.getBody().toString());
        return responseEntity.getBody();
    }



	
//	/**
//	 * 取消指定项目的指定模块的所有人的权限
//	 * @param restTemplate
//	 * @param projId
//	 * @param hierarchyId
//	 */
//	public static void deleteAllAuth(RestTemplate restTemplate, String projId, String hierarchyId) {
//		//10013
//		String deleteAllAuthUrl = "http://localhost:10013/mdvn-staff-sapi/staff/removeAllAuth"+"/"+projId+"/"+hierarchyId;
//		LOG.info("取消权限的Url为："+deleteAllAuthUrl);
//		restTemplate.delete(deleteAllAuthUrl);
//		LOG.info("全部删除项目{}的，{}层的所有权限成功!", projId, hierarchyId);
//	}






//	/**
//	 * 取消权限
//	 *
//	 * @param restTemplate
//	 * @param removeAuthRequest
//	 * @return
//	 */
//	public static ResponseEntity<?> removeAuth(RestTemplate restTemplate, RemoveAuthRequest removeAuthRequest) throws BusinessException {
//		String removeAuthUrl = "";
//		ResponseEntity<StaffAuthInfo> responseEntity = restTemplate.postForEntity(removeAuthUrl, removeAuthRequest,
//				StaffAuthInfo.class);
//		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//			throw new BusinessException(ErrorEnum.REMOVE_AUTH_FAILED, "取消权限失败.");
//		}
//		return responseEntity;
//	}


    /**
     * 移除权限
     * @param removeAuthUrl
     * @param removeAuthRequest
     * @return
     * @throws BusinessException
     */
	public static ResponseEntity<?> removeAuth(String removeAuthUrl, RemoveAuthRequest removeAuthRequest) throws BusinessException{
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> responseEntity ;
        try {
            responseEntity = restTemplate.postForEntity(removeAuthUrl,removeAuthRequest,List.class);
            if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                throw new BusinessException(ErrorEnum.REMOVE_AUTH_FAILED, "移除权限失败.");
            }
        } catch (Exception ex) {
            LOG.error("获取权限失败:{}", ex.getLocalizedMessage());
            throw new BusinessException(ErrorEnum.REMOVE_AUTH_FAILED, "移除权限失败.");
        }
        return responseEntity;

    }

//	/**
//	 * 给创建者添加权限
//	 *
//	 * @param restTemplate
//	 * @param projId
//	 * @param creatorId
//	 * @param hierarchyId
//	 * @param authCode
//	 * @return
//	 */
//	public static List<StaffAuthInfo> assignAuthForCreator(RestTemplate restTemplate, String projId,
//			String hierarchyId, String creatorId,Integer authCode) throws BusinessException {
//		AssignAuthRequest assignAuthRequest = new AssignAuthRequest();
//		assignAuthRequest.setProjId(projId);
//		assignAuthRequest.setAssignerId(creatorId);
//		List<String> assignees = new ArrayList<String>();
//		assignees.add(creatorId);
//		assignAuthRequest.setAssignees(assignees);
//		assignAuthRequest.setHierarchyId(hierarchyId);
//		assignAuthRequest.setAuthCode(authCode);
//		return assignAuth(restTemplate, assignAuthRequest);
//	}


//    /**
//     * 给创建者添加权限
//     * @param assignAuthUrl
//     * @param assignAuthRequest
//     * @return
//     * @throws BusinessException
//     */
//	public static List<StaffAuthInfo> assignAuthForCreator(String assignAuthUrl, AssignAuthRequest assignAuthRequest)throws BusinessException {
//        AssignAuthRequest assignAuthRequestForCreator = assignAuthRequest;
//
//        boolean flag = assignAuthRequestForCreator.getAddList().removeAll(assignAuthRequestForCreator.getAddList());
//        if(flag){
//            assignAuthRequestForCreator.getAddList().add(assignAuthRequestForCreator.getAssignerId());
//        }
//	    return assignAuth(assignAuthUrl,assignAuthRequestForCreator);
//    }

	
	

}

package com.mdvns.mdvn.requirement.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.requirement.service.MemberService;
import com.mdvns.mdvn.requirement.service.RetrieveService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * Requirement查询Controller
 */
@RestController
@CrossOrigin
@RequestMapping(value = {"/requirements","/v1.0/requirements"})
public class RetrieveController {

    /*注入retrieve service*/
    @Resource
    private RetrieveService retrieveService;

    /**
     * 查询指定project下的requirement列表:支持分页
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveList")
    public RestResponse<?> retrieveListByHostSerialNo(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveListByHostSerialNo(singleCriterionRequest);
    }

    /**
     * 根据id获取详情
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     */
    @PostMapping(value = "/retrieveDetail")
    public RestResponse<?> retrieveDetailSerialNo(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailBySerialNo(singleCriterionRequest);
    }

    /**
     * 获取指定编号需求的成员
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveMember")
    public RestResponse<?> retrieveMember(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveMember(retrieveRequest);
    }

    /**
     * 获取指定编号的需求的过程方法id
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveLabelIdBySerialNo")
    public Long retrieveLabelIdBySerialNo(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveLabelIdBySerialNo(retrieveRequest);
    }

    /**
     * 获取指定serialNo的Story的不重复成员Id,以及创建者
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieveReqMembers")
    public List<Long> retrieveReqMembersBySerialNo(@RequestBody  @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveReqMembersBySerialNo(singleCriterionRequest);
    }


    /**
     * 获取指定项目serialNo下的所有requirement的不重复成员对象
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieveAllReqMembersInfo")
    public RestResponse<?> retrieveReqMembersInfoBySerialNo(@RequestBody  @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveReqMembersInfoBySerialNo(singleCriterionRequest);
    }


    /**
     * 获取指定过程方法对应的需求编号
     *
     * @param retrieveRequest request
     * @param bindingResult   bindingResult
     * @return List
     */
    @PostMapping(value = "/retrieveSerialNoByLabel")
    public List<String> retrieveSerialNoByLabel(@RequestBody @Validated RetrieveReqmtByLabelRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveSerialNoByLabel(retrieveRequest);
    }

    /**
     * 获取指定hostSerialNo(项目编号)和templateId的需求编号
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return List
     */
    @PostMapping(value = "/retrieveSerialNo")
    public List<String> retrieveSerialNo(@RequestBody @Validated RetrieveReqmtSerialNoRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveSerialNo(retrieveRequest);
    }

    /**
     *获取指定需求集合的dashboard
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return ReqmtDashboard
     */
    @PostMapping(value = "/retrieveDashboard")
    public ReqmtDashboard retrieveDashboard(@RequestBody @Validated RetrieveMvpContentRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDashboard(retrieveRequest);
    }
}

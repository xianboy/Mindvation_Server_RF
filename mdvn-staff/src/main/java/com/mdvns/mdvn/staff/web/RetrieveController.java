package com.mdvns.mdvn.staff.web;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.staff.domain.RetrieveStaffRequest;
import com.mdvns.mdvn.staff.service.RetrieveService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping(value = {"/staff", "/v1.0/staff"})
public class RetrieveController {

    @Resource
    private RetrieveService retrieveService;

    /**
     * 获取指定id的Staff详情
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveById")
    public RestResponse<?> retrieveDetailById(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailById(singleCriterionRequest);
    }

    /**
     * 获取staff列表
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveAll")
    public RestResponse<?> retrieveAll(@RequestBody @Validated PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveAll(pageableQueryWithoutArgRequest);
    }

    /**
     * 根据指定id集合查询id和name
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveTerseInfo")
    public RestResponse<?> retrieveTerseInfo(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveTerseInfo(retrieveTerseInfoRequest);
    }

    /**
     * 根据id集合获取staff对象
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveStaffInfos")
    public RestResponse<?> retrieveStaffInfos(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveStaffInfos(retrieveTerseInfoRequest);
    }


    @PostMapping(value = "/retrieveByName")
    public RestResponse<?> retrieveByName(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveByName(singleCriterionRequest);
    }

    @PostMapping(value = "/retrieveList")
    public RestResponse<?> retrieveByNameOrTags(@RequestBody RetrieveStaffRequest retrieveRequest) throws BusinessException {
        return this.retrieveService.retrieveByNameOrTags(retrieveRequest);
    }


}

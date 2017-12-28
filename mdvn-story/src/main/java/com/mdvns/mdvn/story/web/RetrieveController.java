package com.mdvns.mdvn.story.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.story.service.RetrieveService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping(value = {"/stories", "/v1.0/stories"})
public class RetrieveController {

    @Resource
    private RetrieveService retrieveService;


    /**
     * 获取指定id的Story的详情
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieveDetailById")
    public RestResponse<?> retrieveDetailById(@RequestBody  @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailById(singleCriterionRequest);
    }


    /**
     * 获取story列表: 支持分页
     * @param singleCriterionRequest request
     * @param bindingResult 参数校验
     * @return restResponse
     */
    @PostMapping(value = "/retrieveList")
    public RestResponse<?> retrieveListByHostSerialNo(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveListByHostSerialNo(singleCriterionRequest);
    }

    /**
     * 获取指定serialNo的Story的详情
     * @param singleCriterionRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieveDetailBySerialNo")
    public RestResponse<?> retrieveDetailBySerialNo(@RequestBody  @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailBySerialNo(singleCriterionRequest);
    }


}

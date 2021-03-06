package com.mdvns.mdvn.requirement.web;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.requirement.service.UpdateService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping(value = {"/requirements", "/v1.0/requirements"})
public class UpdateController {

    @Resource
    private UpdateService updateService;

    @PostMapping(value = "/updateStatus")
    public RestResponse<?> updateStatus(@RequestBody @Validated UpdateStatusRequest updateStatusRequest, BindingResult bindingResult) {
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //调用service更新状态
        return this.updateService.updateStatus(updateStatusRequest);
    }

    /**
     * 修改基础信息
     *
     * @param updateRequest 更新参数
     * @param bindingResult 参数校验
     * @return restResponse
     */
    @PostMapping(value = "/updateBasicInfo")
    public RestResponse<?> updateBaseInfo(@RequestBody @Validated UpdateBasicInfoRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //调用更新service
        return this.updateService.updateBasicInfo(updateRequest);
    }


    /**
     * 修改其他信息
     * @param updateRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/updateOtherInfo")
    public RestResponse<?> updateOtherInfo(@RequestBody @Validated UpdateOtherInfoRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //调用更新service
        return this.updateService.updateOtherInfo(updateRequest);
    }

    /**
     * 修改可选信息
     * @param updateRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/updateOptionalInfo")
    public RestResponse<?> updateOptionalInfo(@RequestBody @Validated UpdateOptionalInfoRequest updateRequest, BindingResult bindingResult) throws BusinessException{
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //调用更新service
        return this.updateService.updateOptionalInfo(updateRequest);
    }

    /**
     * 更新指定集合的需求的mvpId(三层)
     *
     * @param request request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/updateMvp")
    public RestResponse<?> updateMvp(@RequestBody @Validated UpdateMvpContentRequest request, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.updateMvp(request);
    }

    /**
     * 修改某个模板的mvp Dashboard
     * @param updateRequest updateRequest
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/updateMvpDashboard")
    public RestResponse<?> updateMvpDashboard(@RequestBody @Validated UpdateMvpDashboardRequest updateRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.updateMvpDashboard(updateRequest);
    }
}

package com.mdvns.mdvn.project.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.project.domain.UpdateOtherInfoRequest;
import com.mdvns.mdvn.project.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 更新controller
 */
@RestController
@CrossOrigin
@RequestMapping(value = {"/projects", "/v1.0/projects"})
public class UpdateController {

    /*自动注入update service*/
    private final UpdateService updateService;

    @Autowired
    public UpdateController(UpdateService updateService) {
        Assert.notNull(updateService, "updateService must not be null!");
        this.updateService = updateService;
    }


    /**
     * 状态更新
     * @param updateStatusRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     */
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
    public RestResponse<?> updateBasicInfo(@RequestBody @Validated UpdateBasicInfoRequest updateRequest, BindingResult bindingResult) {
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
     * @throws BusinessException error
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
     * @throws BusinessException error
     */
    @PostMapping(value = "/updateOptionalInfo")
    public RestResponse<?> updateOptionalInfo(@RequestBody @Validated UpdateOptionalInfoRequest updateRequest, BindingResult bindingResult) throws BusinessException{
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //调用更新service
        return this.updateService.updateOptionalInfo(updateRequest);
    }

}

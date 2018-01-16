package com.mdvns.mdvn.dashboard.web;


import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.dashboard.domain.CreateMvpRequest;
import com.mdvns.mdvn.dashboard.service.CreateService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/dashboards", "/v1.0/dashboards"})
public class CreateController {

    @Resource
    private CreateService service;


    /**
     * 新建Dashboard
     *
     * @param createRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateMvpRequest createRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.service.create(createRequest);
    }

    /**
     * 按照模板智能排列mvp
     * @param arrangeRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/arrangeMvp")
    public RestResponse<?> arrangeMvp(@RequestBody @Validated SingleCriterionRequest arrangeRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.service.arrangeMvp(arrangeRequest);
    }
}

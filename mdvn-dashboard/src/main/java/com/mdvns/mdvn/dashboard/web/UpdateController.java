package com.mdvns.mdvn.dashboard.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpDashboardRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.dashboard.service.UpdateService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/dashboards", "/v1.0/dashboards"})
public class UpdateController {

    @Resource
    private UpdateService service;

    /**
     * 拖动(修改)某个模板的mvp Dashboard
     * @param updateRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/update")
    public RestResponse<?> update(@RequestBody @Validated UpdateMvpDashboardRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.service.update(updateRequest);
    }





}

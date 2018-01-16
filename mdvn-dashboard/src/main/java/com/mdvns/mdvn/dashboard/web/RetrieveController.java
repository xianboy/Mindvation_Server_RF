package com.mdvns.mdvn.dashboard.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.dashboard.service.RetrieveService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/dashboards", "/v1.0/dashboards"})
public class RetrieveController {

    @Resource
    private RetrieveService retrieveService;

    /**
     * 获取指定serialNo的项目的 MVP Dashboard
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveMvpDashboard")
    public RestResponse<?> retrieveMvpDashboard(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveMvpDashboard(retrieveRequest);
    }


}

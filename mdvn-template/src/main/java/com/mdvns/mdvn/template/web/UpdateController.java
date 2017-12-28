package com.mdvns.mdvn.template.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.template.service.UpdateService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/templates", "/v1.0/templates"})
public class UpdateController {

    @Resource
    private UpdateService updateService;


    /**
     * 删除指定id的FunctionLabel
     *
     * @param request request
     * @return restResponse
     */
    @PostMapping(value = "/deleteLabelById")
    public RestResponse<?> deleteLabelById(@RequestBody SingleCriterionRequest request, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.deleteLabelById(Long.valueOf(request.getCriterion()));
    }
}

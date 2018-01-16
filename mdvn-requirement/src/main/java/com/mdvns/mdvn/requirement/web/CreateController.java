package com.mdvns.mdvn.requirement.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpContentRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.requirement.domain.CreateRequirementRequest;
import com.mdvns.mdvn.requirement.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = {"/requirements", "/v1.0/requirements"})
public class CreateController {

    @Autowired
    private CreateService createService;

    /**
     * 新建需求
     *
     * @param createRequest
     * @return restResponse
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateRequirementRequest createRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.createService.create(createRequest);
    }

}

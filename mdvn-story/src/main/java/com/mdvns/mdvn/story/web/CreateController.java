package com.mdvns.mdvn.story.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpContentRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import org.springframework.web.bind.annotation.RestController;

import com.mdvns.mdvn.story.domain.CreateStoryRequest;
import com.mdvns.mdvn.story.service.CreateService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping(value = {"/stories", "/v1.0/stories"})
public class CreateController {

    @Resource
    private CreateService createService;

    /**
     * 创建story
     *
     * @param createRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     */

    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateStoryRequest createRequest, BindingResult bindingResult) throws BusinessException, BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.createService.create(createRequest);
    }

    /**
     * 更新mvp
     *
     * @param request       request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/createMvp")
    public RestResponse<?> createMvp(@RequestBody @Validated UpdateMvpContentRequest request, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.createService.createMvp(request);
    }
}


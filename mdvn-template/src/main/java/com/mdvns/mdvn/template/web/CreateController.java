package com.mdvns.mdvn.template.web;

import com.mdvns.mdvn.common.bean.CustomDeliveryRequest;
import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.template.domain.CreateTemplateRequest;
import com.mdvns.mdvn.template.service.CreateService;
import com.mdvns.mdvn.template.service.DeliveryService;
import com.mdvns.mdvn.template.service.LabelService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/templates", "/v1.0/templates"})
public class CreateController {

    @Resource
    private CreateService templateService;

    @Resource
    private LabelService labelService;

    @Resource
    private DeliveryService deliveryService;

    /**
     * 创建模板
     * @param createRequest request
     * @return RestResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateTemplateRequest createRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.templateService.create(createRequest);
    }

    /**
     * 保存自定义过程方法模块: requirement/story
     * @param customRequest request
     * @param bindingResult request校验
     * @return restResponse
     */
    @PostMapping(value = "/customLabel")
    public Long create(@RequestBody @Validated CustomFunctionLabelRequest customRequest, BindingResult bindingResult) {
        //请求参数校验
        BindingResultUtil.brResolve(bindingResult);
        //请求处理
        return this.labelService.create(customRequest);
    }

    /**
     * 自定义交付件
     * @param request request
     * @param bindingResult bindingResult
     * @return Long
     */
    @PostMapping(value = "/customDelivery")
    public Long create(@RequestBody @Validated CustomDeliveryRequest request, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return deliveryService.create(request);
    }

}

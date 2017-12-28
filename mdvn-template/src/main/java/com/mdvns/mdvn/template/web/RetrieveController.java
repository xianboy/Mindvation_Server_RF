package com.mdvns.mdvn.template.web;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.template.service.RetrieveService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = {"/templates", "/v1.0/templates"})
public class RetrieveController {

    @Resource
    private RetrieveService retrieveService;


    /**
     * 根据行业类型查询
     * @param singleCriterionRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveByIndustry")
    public RestResponse<?> retrieveByIndustryId(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveByIndustryId(singleCriterionRequest);
    }

    /**
     * 分页查询所有数据
     * @param pageableQueryWithoutArgRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveAll")
    public RestResponse<?> retrieveAll(@RequestBody @Validated PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveAll(pageableQueryWithoutArgRequest);
    }

    /**
     * 根据指定id集合查询id和name
     * @param retrieveTerseInfoRequest retrieveTerseInfoRequest
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveTerseInfo")
    public RestResponse<?> retrieveTerseInfo(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveTerseInfo(retrieveTerseInfoRequest);
    }

    /**
     * 根据指定id集合查询TemplateROle的id和name
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveRoleTerseInfo")
    public RestResponse<?> retrieveRoleBaseInfo(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveRoleBaseInfo(retrieveTerseInfoRequest);
    }

    /**
     * 根据指定id集合查询FunctionLabel
     * @param retrieveTerseInfoRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     */
    @PostMapping(value = "/retrieveLabel")
    public RestResponse<?> retrieveLabel(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveLabel(retrieveTerseInfoRequest);
    }

    /**
     * 根据name和hostSerialNo查询过程方法
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return restResponse
     */
    @PostMapping(value = "/retrieveByNameAndHost")
    public RestResponse<?> retrieveByNameAndHost(@RequestBody RetrieveByNameAndHostRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveByNameAndHost(retrieveRequest);
    }

    /**
     * 根据id获取模板信息
     * @param singleCriterionRequest request
     * @param bindingResult  bindingResult
     * @return RestResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieve")
    public RestResponse<?> retrieveById(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveById(singleCriterionRequest);
    }
}

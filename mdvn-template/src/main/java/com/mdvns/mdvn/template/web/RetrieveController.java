package com.mdvns.mdvn.template.web;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.template.domain.entity.MvpTemplate;
import com.mdvns.mdvn.template.service.RetrieveService;
import com.mdvns.mdvn.template.service.RoleService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = {"/templates", "/v1.0/templates"})
public class RetrieveController {

    @Resource
    private RetrieveService retrieveService;

    @Resource
    private RoleService roleService;

    /**
     * 根据行业类型查询
     *
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
     *
     * @param pageableQueryWithoutArgRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveAll")
    public RestResponse<?> retrieveAll(@RequestBody @Validated PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveAll(pageableQueryWithoutArgRequest);
    }

    /**
     * 根据指定id集合查询id,serialNo和name
     *
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
     *
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
     *
     * @param retrieveTerseInfoRequest request
     * @param bindingResult            bindingResult
     * @return restResponse
     */
    @PostMapping(value = "/retrieveLabel")
    public RestResponse<?> retrieveLabel(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveLabel(retrieveTerseInfoRequest);
    }

    /**
     * 根据name和hostSerialNo查询过程方法
     *
     * @param retrieveRequest request
     * @param bindingResult   bindingResult
     * @return restResponse
     */
    @PostMapping(value = "/retrieveByNameAndHost")
    public RestResponse<?> retrieveByNameAndHost(@RequestBody RetrieveByNameAndHostRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveByNameAndHost(retrieveRequest);
    }

    /**
     * 根据id获取模板信息
     *
     * @param singleCriterionRequest request
     * @param bindingResult          bindingResult
     * @return RestResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/retrieve")
    public RestResponse<?> retrieveById(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveById(singleCriterionRequest);
    }

    /**
     * @param singleCriterionRequest request
     * @param bindingResult          bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveTemplateRoles")
    public RestResponse<?> retrieveTemplateRoles(@RequestBody @Validated SingleCriterionRequest singleCriterionRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.roleService.retrieveRoles(singleCriterionRequest);
    }

    /**
     * 获取指定id的过程方法及其子方法
     *
     * @param retrieveRequest request
     * @param bindingResult   bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveLabelDetail")
    public RestResponse<?> retrieveLabelDetail(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveLabelDetail(retrieveRequest);
    }

    /**
     * 获取指定id的交付件
     *
     * @param retrieveRequest request
     * @param bindingResult   bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveDelivery")
    public RestResponse<?> retrieveDelivery(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDelivery(retrieveRequest);
    }

    /**
     * 获取指定id的模板的所有交付件
     *
     * @param retrieveRequest request
     * @param bindingResult   bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveDeliveries")
    public RestResponse<?> retrieveDeliveries(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDeliveries(retrieveRequest);
    }

    /**
     * 获取指定ID的模板的迭代计划
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return List<MvpTemplate>
     */
    @PostMapping(value = "/retrieveMvpTemplates")
    public List<MvpTemplate> retrieveMvpTemplates(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveMvpTemplates(retrieveRequest);
    }

    /**
     * 获取mvpId为指定的值的过程方法的Id
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return List
     */
    @PostMapping(value = "/retrieveLabelByMvp")
    public List<Long> retrieveLabelByMvp(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveLabelByMvp(retrieveRequest);
    }

    /**
     * 获取指定ID的模板名称
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return 模板名称
     */
    @PostMapping(value = "/retrieveName")
    public String retrieveTemplateName(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveTemplateName(retrieveRequest);
    }

}

package com.mdvns.mdvn.department.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.department.domain.UpdateDepartmentRequest;
import com.mdvns.mdvn.department.service.UpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = {"/depts", "/v1.0/depts"})
public class UpdateController {

    @Autowired
    private UpdateService updateService;
    /**
     * 更新部门信息
     * @param updateRequest
     * @return
     */
    @PostMapping(value = "/updateDept")
    public RestResponse<?> updateDept(@RequestBody @Validated UpdateDepartmentRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.updateDept(updateRequest);
    }

    /**
     * 删除一个部门信息
     * @param retrieveDetailRequest
     * @return
     */
    @PostMapping(value = "/deleteDept")
    public RestResponse<?> deleteDept(@RequestBody @Validated SingleCriterionRequest retrieveDetailRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.deleteDept(retrieveDetailRequest);
    }


}

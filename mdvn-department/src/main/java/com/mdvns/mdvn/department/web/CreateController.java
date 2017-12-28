package com.mdvns.mdvn.department.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.department.domain.CreateDeptRequest;
import com.mdvns.mdvn.department.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/depts", "/v1.0/depts"})
@CrossOrigin
public class CreateController {

    @Autowired
    private CreateService deptService;

    /**
     * 新建部门
     * @param deptRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateDeptRequest deptRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.deptService.create(deptRequest);
    }

}

package com.mdvns.mdvn.department.web;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.department.service.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = {"/depts", "/v1.0/depts"})
public class RetrieveController {

    @Autowired
    private RetrieveService retrieveService;

    /**
     * 查询部门列表:支持分页
     * @param pageableQueryWithoutArgRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/retrieveAll")
    public RestResponse<?> retrieveAll(@RequestBody @Validated PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveAll(pageableQueryWithoutArgRequest);
    }

    /**
     * 获取指定id的部门详情
     * @param retrieveDetailRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/retrieveById")
    public RestResponse<?> retrieveDeatilById(@RequestBody @Validated SingleCriterionRequest retrieveDetailRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailById(retrieveDetailRequest);
    }

    /**
     * 获取指定name的部门详情
     * @param retrieveDetailRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/retrieveByName")
    public RestResponse<?> retrieveDeatilByName(@RequestBody @Validated SingleCriterionRequest retrieveDetailRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailByName(retrieveDetailRequest);
    }
}

package com.mdvns.mdvn.task.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.task.domain.CreateTaskRequest;
import com.mdvns.mdvn.task.domain.UpdateAttachRequest;
import com.mdvns.mdvn.task.domain.UpdateProgressRequest;
import com.mdvns.mdvn.task.service.CreateService;
import com.mdvns.mdvn.task.service.RetrieveService;
import com.mdvns.mdvn.task.service.UpdateService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping(value = {"/tasks", "/v1.0/tasks"})
public class TaskController {

    @Resource
    private CreateService createService;

    @Resource
    private RetrieveService retrieveService;

    @Resource
    private UpdateService updateService;

    /**
     * 创建
     * @param createRequest createRequest
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody CreateTaskRequest createRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.createService.create(createRequest);
    }

    /**
     * 根据id获取详情
     * @param retrieveRequest retrieveRequest
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveDetailById")
    public RestResponse<?> retrieveDetailById(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveDetailById(retrieveRequest);
    }

    /**
     * 更新进度
     * @param updateRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     * @throws BusinessException exception
     */
    @PostMapping(value = "/updateProgress")
    public RestResponse<?> updateProgress(@RequestBody @Validated UpdateProgressRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.updateService.updateProgress(updateRequest);
    }

    /**
     * 获取指定hostSerialNo的task列表
     * @param retrieveRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveList")
    public RestResponse<?> retrieveList(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveList(retrieveRequest);
    }

    /**
     * 给某个task添加附件
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/addAttachForTask")
    private RestResponse<?> addAttachForTask(@RequestBody UpdateAttachRequest request) throws Exception, BusinessException {
        return this.updateService.addAttachForTask(request);
    }

    /**
     * 给某个task删除附件
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteAttachForTask")
    private RestResponse<?> deleteAttachForTask(@RequestBody UpdateAttachRequest request) throws Exception, BusinessException {
        return this.updateService.deleteAttachForTask(request);
    }

    /**
     * 获取历史记录
     * @param retrieveRequest retrieveRequest
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveHistory")
    public RestResponse<?> retrieveHistory(@RequestBody @Validated SingleCriterionRequest retrieveRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.retrieveService.retrieveHistory(retrieveRequest);
    }

}

package com.mdvns.mdvn.task.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.task.config.WebConfig;
import com.mdvns.mdvn.task.domain.UpdateAttachRequest;
import com.mdvns.mdvn.task.domain.UpdateProgressRequest;
import com.mdvns.mdvn.task.domain.entity.Task;
import com.mdvns.mdvn.task.domain.entity.TaskHistory;
import com.mdvns.mdvn.task.repository.HistoryRepository;
import com.mdvns.mdvn.task.repository.TaskRepository;
import com.mdvns.mdvn.task.service.RetrieveService;
import com.mdvns.mdvn.task.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UpdateServiceImpl implements UpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private TaskRepository repository;

    @Resource
    private WebConfig webConfig;

    @Resource
    private HistoryRepository historyRepository;

    /**
     * 更新进度
     *
     * @param updateRequest request
     * @return RestResponse
     * @throws BusinessException exception
     */
    @Override
    @Transactional
    @Modifying
    public RestResponse<?> updateProgress(UpdateProgressRequest updateRequest) throws BusinessException {
        LOG.info("更新进度开始...");
        //更新历史记录
        TaskHistory history = new TaskHistory();
        Task task = this.repository.findOne(updateRequest.getHostId());
        Integer oldProgress = task.getProgress();
        MdvnCommonUtil.notExistingError(task, ErrorEnum.TASK_NOT_EXISTS, "ID为【" + updateRequest.getHostId() + "】的task不存在.");
        if (!StringUtils.isEmpty(updateRequest.getComment())) {
            task.setComment(updateRequest.getComment());
            //记录备注历史
            history.setBeforeRemarks(task.getComment());
            history.setNowRemarks(updateRequest.getComment());
        }
        LOG.info("更新进度...");
        if (null != updateRequest.getProgress()) {
            if (updateRequest.getProgress() < 100 && updateRequest.getProgress() > MdvnConstant.ZERO) {
                task.setStatus(MdvnConstant.IN_PROGRESS);
            } else {
                task.setStatus(MdvnConstant.DONE);
            }
            task.setProgress(updateRequest.getProgress());
            //记录进度历史
            history.setBeforeProgress(task.getProgress());
            history.setNowProgress(updateRequest.getProgress());
        }
        //保存更新后的数据
        this.repository.saveAndFlush(task);
        //添加历史记录表
        try {
            history.setTaskId(updateRequest.getHostId());
            history.setCreatorId(updateRequest.getStaffId());
            history.setAction(MdvnConstant.UPDATE);
            history.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.historyRepository.saveAndFlush(history);
        } catch (Exception ex) {
            LOG.error("保存Task更新记录失败...");
        }
        if (!StringUtils.isEmpty(updateRequest.getComment()) && (null == updateRequest.getProgress() || oldProgress == updateRequest.getProgress())) {
            /**
             * 消息推送（只更改task备注）
             */
            Long initiatorId = updateRequest.getStaffId();
            this.serverPushByUpdate(initiatorId, task);
        }
        if (null != updateRequest.getProgress() && oldProgress != updateRequest.getProgress()) {
            /**
             * 消息推送（更改task进度）
             */
            Long initiatorId = updateRequest.getStaffId();
            this.serverPushByUpdateProgress(initiatorId, task,oldProgress);
        }
        LOG.info("更新进度完成...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 添加附件
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RestResponse<?> addAttachForTask(UpdateAttachRequest request) throws BusinessException {
        LOG.info("添加附件开始...");
        //根据id获取task
        Task task = this.repository.findOne(request.getHostId());
        String attaches = task.getAttaches();
        if (StringUtils.isEmpty(attaches)) {
            task.setAttaches(String.valueOf(request.getAttachId()));
        } else {
            //","隔开的字符串转化为list集合
            String[] attachIds = attaches.split(",");
            List<String> attachIdList = Arrays.asList(attachIds);
            List<String> attIds = new ArrayList(attachIdList);
            if (!StringUtils.isEmpty(request.getAttachId()) && !attIds.contains(request.getAttachId())) {
                attIds.add(request.getAttachId().toString());
                attaches = MdvnStringUtil.join(attIds, ",");
                task.setAttaches(attaches);
            }
        }
        this.updateAttach(request, task.getSerialNo(), MdvnConstant.ZERO);
        task = this.repository.saveAndFlush(task);

        //记录附件添加历史失败
        try {
            attachHistory(request.getStaffId(), task.getId(), request.getAttachId(), MdvnConstant.REMOVE_ATTACH);
        } catch (Exception ex) {
            LOG.error("记录附件添加历史失败：【{}】...", ex.getMessage());
        }


        /**
         * 消息推送（添加task附件）
         */
        Long initiatorId = request.getStaffId();
        this.serverPushByUpdate(initiatorId, task);

        LOG.info("添加附件完成...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 删除附件
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RestResponse<?> deleteAttachForTask(UpdateAttachRequest request) throws BusinessException {
        LOG.info("删除附件开始...");
        //根据id获取task
        Task task = this.repository.findOne(request.getHostId());
        String attaches = task.getAttaches();
        //","隔开的字符串转化为list集合
        String[] attachIds = attaches.split(",");
        List<String> attachIdList = Arrays.asList(attachIds);
        List<String> attIds = new ArrayList(attachIdList);
        if (!StringUtils.isEmpty(request.getAttachId())) {
            attIds.remove(request.getAttachId().toString());
            attaches = MdvnStringUtil.join(attIds, ",");
            task.setAttaches(attaches);
            this.updateAttach(request, task.getSerialNo(), MdvnConstant.ONE);
            task = this.repository.saveAndFlush(task);
        }

        //记录附件删除历史失败
        try {
            attachHistory(request.getStaffId(), request.getHostId(), request.getAttachId(), MdvnConstant.REMOVE_ATTACH);
        } catch (Exception ex) {
            LOG.error("记录附件删除历史失败：【{}】...", ex.getMessage());
        }

        /**
         * 消息推送（删除task附件）
         */
        Long initiatorId = request.getStaffId();
        this.serverPushByUpdate(initiatorId, task);
        LOG.info("删除附件完成...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 更新task附件
     *
     * @param request
     * @param flag
     * @return
     * @throws BusinessException
     */


    public RestResponse<?> updateAttach(UpdateAttachRequest request, String serialNo, Integer flag) throws
            BusinessException {

        LOG.info("更新task附件信息开始...");
        UpdateOptionalInfoRequest updateRequest = new UpdateOptionalInfoRequest();
        Long attachId = request.getAttachId();
        AddOrRemoveById attaches = new AddOrRemoveById();
        List<Long> attachIds = new ArrayList<>();
        attachIds.add(attachId);
        if (flag == MdvnConstant.ZERO) {
            attaches.setAddList(attachIds);
        } else {
            attaches.setRemoveList(attachIds);
        }
        updateRequest.setAttaches(attaches);
        updateRequest.setHostId(request.getHostId());
        updateRequest.setStaffId(request.getStaffId());
        //更新附件
        if (null != updateRequest.getAttaches()) {
            FileUtil.updateAttaches(updateRequest, serialNo);
        }


        LOG.info("更新项目附件信息结束...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 记录附件更新历史
     * @param creatorId  creatorId
     * @param taskId taskId
     * @param attachId attachId
     * @param action action
     * @return TaskHistory
     */
    private TaskHistory attachHistory(Long creatorId, Long taskId, Long attachId, String action) {
        //删除附件历史记录
        TaskHistory history = new TaskHistory();
        try {
            history.setTaskId(taskId);
            history.setCreatorId(creatorId);
            if (MdvnConstant.REMOVE_ATTACH.equals(action)) {
                history.setDeleteAttachId(attachId);
                history.setAction(action);
                LOG.error("保存Task删除附件历史成功...");
            } else {
                history.setAddAttachId(attachId);
                history.setAction(action);
                LOG.error("保存Task添加附件历史成功...");
            }
            history.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            history = this.historyRepository.saveAndFlush(history);
        } catch (Exception ex) {
            LOG.error("保存Task附件更新记录失败...");
        }
        return history;
	}
     /** 更改task的消息推送
     *
     * @param initiatorId
     * @param task
     * @throws BusinessException
     */

    private void serverPushByUpdate(Long initiatorId, Task task) throws BusinessException {
        try {
            String serialNo = task.getSerialNo();
            String subjectType = "task";
            String type = "update";
            String taskByStoryId = task.getHostSerialNo();
            //实例化restTemplate对象
            RestTemplate restTemplate = new RestTemplate();
            SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
            singleCriterionRequest.setStaffId(initiatorId);
            singleCriterionRequest.setCriterion(task.getHostSerialNo());
            List<Long> staffIds = restTemplate.postForObject(webConfig.getRetrieveStoryMembersUrl(), singleCriterionRequest, List.class);
            ServerPushUtil.serverPushByTask(initiatorId, serialNo, subjectType, type, staffIds, taskByStoryId);
            LOG.info("更改story信息，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(更改story)出现异常，异常信息：" + e);
        }
    }

    /**
     * 更改task的消息推送(进度)
     *
     * @param initiatorId
     * @param task
     * @throws BusinessException
     */

    private void serverPushByUpdateProgress(Long initiatorId, Task task,Integer oldProgress) throws BusinessException {
        try {
            String serialNo = task.getSerialNo();
            String subjectType = "task";
            String type = "update progress";
            String taskByStoryId = task.getHostSerialNo();
            Integer newProgress = task.getProgress();
            //实例化restTemplate对象
            RestTemplate restTemplate = new RestTemplate();
            SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
            singleCriterionRequest.setStaffId(initiatorId);
            singleCriterionRequest.setCriterion(task.getHostSerialNo());
            List<Long> staffIds = restTemplate.postForObject(webConfig.getRetrieveStoryMembersUrl(), singleCriterionRequest, List.class);
            ServerPushUtil.serverPushByTaskProgress(initiatorId, serialNo, subjectType, type, staffIds, taskByStoryId,newProgress,oldProgress);
            LOG.info("更改task进度信息，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(更改task进度)出现异常，异常信息：" + e);
        }
    }
}

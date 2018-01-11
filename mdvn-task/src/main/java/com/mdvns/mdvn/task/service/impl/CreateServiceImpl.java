package com.mdvns.mdvn.task.service.impl;

import com.mdvns.mdvn.common.bean.AssignAuthRequest;
import com.mdvns.mdvn.common.bean.CustomDeliveryRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.StaffAuthInfo;
import com.mdvns.mdvn.common.constant.AuthConstant;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.ServerPushUtil;
import com.mdvns.mdvn.common.util.StaffAuthUtil;
import com.mdvns.mdvn.task.config.WebConfig;
import com.mdvns.mdvn.task.domain.CreateTaskRequest;
import com.mdvns.mdvn.task.domain.entity.Task;
import com.mdvns.mdvn.task.domain.entity.TaskHistory;
import com.mdvns.mdvn.task.repository.HistoryRepository;
import com.mdvns.mdvn.task.repository.TaskRepository;
import com.mdvns.mdvn.task.service.CreateService;
import com.mdvns.mdvn.task.service.RetrieveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    @Resource
    private TaskRepository repository;

    @Resource
    private WebConfig webConfig;

    @Resource
    private RetrieveService retrieveService;

    @Resource
    private HistoryRepository historyRepository;

    /**
     * 创建task
     *
     * @param createRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> create(CreateTaskRequest createRequest) throws BusinessException {
        //根据request构建task
        Task task = buildByRequest(createRequest);
        //保存
        task = this.repository.saveAndFlush(task);

        //分配权限
        List<StaffAuthInfo> staffAuthInfos = StaffAuthUtil.assignAuth(webConfig.getAssignAuthUrl(),new AssignAuthRequest(task.getProjSerialNo(),task.getCreatorId(), Arrays.asList(task.getCreatorId()),task.getSerialNo(), AuthConstant.TMEMBER));
        task.setStaffAuthInfo(staffAuthInfos);
        createHistory(createRequest.getCreatorId(), task.getId());
        //根据id获取task详情
        /**
         * 消息推送
         */
        this.serverPushByCreate(createRequest,task);
        return retrieveService.retrieveDetailById(new SingleCriterionRequest(createRequest.getCreatorId(), task.getId().toString()));
    }

    /**
     * 构建task
     *
     * @param createRequest request
     * @return Task
     */
    private Task buildByRequest(CreateTaskRequest createRequest) throws BusinessException {
        Task task = new Task();
        task.setCreatorId(createRequest.getCreatorId());
        String serialNo = buildSerialNo();
        task.setSerialNo(serialNo);
        task.setHostSerialNo(createRequest.getHostSerialNo());
        task.setDescription(createRequest.getDescription());
        task.setDeliveryId(buildDelivery(createRequest.getDelivery(), createRequest.getCreatorId(), serialNo));
        task.setStartDate(createRequest.getStartDate());
        task.setEndDate(createRequest.getEndDate());

        task.setProjSerialNo(createRequest.getProjSerialNo());
        return task;
    }

    /**
     * 构建交付件
     *
     * @param delivery     delivery
     * @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @return Long
     */
    private Long buildDelivery(Object delivery, Long creatorId, String hostSerialNo) throws BusinessException {
        //如果是Integer类型, 就是已存在的交付件的id
        if (delivery instanceof Integer) {
            return Long.valueOf(delivery.toString());
        } else {
            //如果是CustomDeliverable类型, 则自定义交付件
            try {
                return customDelivery(delivery, creatorId, hostSerialNo);
            } catch (Exception ex) {
                LOG.error("task交付件参数【{}】错误...", delivery.toString());
                throw new BusinessException(ErrorEnum.ILLEGAL_ARG, "task交付件参数错误.");
            }
        }
    }

    /**
     * 自定义交付件
     *
     * @param delivery     delivery
     * @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @return Long
     */
    private Long customDelivery(Object delivery, Long creatorId, String hostSerialNo) {
        CustomDeliveryRequest customDelivery = new CustomDeliveryRequest();
        LinkedHashMap map = (LinkedHashMap) delivery;
        customDelivery.setName(map.get("name").toString());
        customDelivery.setTypeId(Integer.valueOf(map.get("typeId").toString()));
        customDelivery.setCreatorId(creatorId);
        customDelivery.setHostSerialNo(hostSerialNo);
        RestTemplate restTemplate = new RestTemplate();
        String customDeliveryUrl = webConfig.getCustomDeliveryUrl();
        return restTemplate.postForObject(customDeliveryUrl, customDelivery, Long.class);
    }

    /**
     * 记录task新建历史
     *
     * @param staffId staffId
     * @param taskId  taskId
     * @return TaskHistory
     */
    private TaskHistory createHistory(Long staffId, Long taskId) {
        LOG.info("记录ID为【{}】的task创建历史开始...", taskId);
        //历史记录
        TaskHistory history = new TaskHistory();
        try {
            history.setTaskId(taskId);
            history.setCreatorId(staffId);
            history.setAction(MdvnConstant.CREATE);
            history.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            history = this.historyRepository.saveAndFlush(history);
            LOG.info("记录ID为【{}】的task创建历史成功", taskId);
        } catch (Exception ex) {
            LOG.error("保存Task更新记录失败...");
        }
        return history;
    }

    /**
     * 构建编号
     *
     * @return String
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.repository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += 1;
        return MdvnConstant.T + maxId;
    }

    /**
     * 创建task的消息推送
     * @param createRequest
     * @param task
     * @throws BusinessException
     */
    private void serverPushByCreate(CreateTaskRequest createRequest,Task task) throws BusinessException {
        try {
            Long initiatorId = createRequest.getCreatorId();
            String serialNo = task.getSerialNo();
            String subjectType = "task";
            String type = "create";
            String taskByStoryId = task.getHostSerialNo();
            //实例化restTemplate对象
            RestTemplate restTemplate = new RestTemplate();
            SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
            singleCriterionRequest.setStaffId(createRequest.getCreatorId());
            singleCriterionRequest.setCriterion(taskByStoryId);
            List<Long> staffIds = restTemplate.postForObject(webConfig.getRetrieveStoryMembersUrl(),singleCriterionRequest,List.class);
            ServerPushUtil.serverPushByTask(initiatorId,serialNo,subjectType,type,staffIds,taskByStoryId);
            LOG.info("创建task，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(创建task)出现异常，异常信息：" + e);
        }
    }


}

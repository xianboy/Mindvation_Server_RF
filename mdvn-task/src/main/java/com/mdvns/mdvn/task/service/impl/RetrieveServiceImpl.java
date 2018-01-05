package com.mdvns.mdvn.task.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.*;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.task.config.WebConfig;
import com.mdvns.mdvn.task.domain.entity.Task;
import com.mdvns.mdvn.task.domain.entity.TaskHistory;
import com.mdvns.mdvn.task.repository.HistoryRepository;
import com.mdvns.mdvn.task.repository.TaskRepository;
import com.mdvns.mdvn.task.service.RetrieveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);

    @Resource
    private TaskRepository repository;

    @Resource
    private WebConfig webConfig;

    @Resource
    private HistoryRepository historyRepository;

    /**
     * 根据Id获取详情
     *
     * @param retrieveRequest retrieveRequest
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveDetailById(SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("根据Id获取详情开始...");
        //从request中获取id
        Long id = Long.valueOf(retrieveRequest.getCriterion());
        //根据id获取task
        Task task = getTaskById(retrieveRequest.getStaffId(), id);
        LOG.info("根据Id获取详情成功...");
        return RestResponseUtil.success(task);
    }

    /**
     * 获取指定id的task详情
     *
     * @param staffId 当前用户Id
     * @param id      task的id
     * @return task
     * @throws BusinessException BusinessException
     */
    private Task getTaskById(Long staffId, Long id) throws BusinessException {
        LOG.info("根据Id获取task详情开始...");
        //根据id获取task
        Task task = this.repository.findOne(id);
        MdvnCommonUtil.notExistingError(task, ErrorEnum.TASK_NOT_EXISTS, "Id为【" + id + "】的task不存在...");
        //获取task创建人信息
        String retrieveMembersUrl = webConfig.getRetrieveMembersUrl();
        List<Long> ids = new ArrayList<>();
        ids.add(task.getCreatorId());
        List<TerseInfo> list = RestTemplateUtil.retrieveTerseInfo(task.getCreatorId(), ids, retrieveMembersUrl);
        MdvnCommonUtil.emptyList(list, ErrorEnum.STAFF_NOT_EXISTS, "id为【" + task.getCreatorId() + "】的Staff不存在.");
        task.setCreator(list.get(MdvnConstant.ZERO));
        //获取task的交付件
        task.setDelivery(getDeliveryById(staffId, task.getDeliveryId()));
        //获取task的附件
        task.setAttchInfos(FileUtil.getAttaches(task.getSerialNo()));
        LOG.info("根据Id获取详情成功...");
        return task;
    }


    /**
     * 获取指定hostSerialNo的task列表
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveList(SingleCriterionRequest retrieveRequest) throws BusinessException {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        List<Long> taskIdList = this.repository.findIdByHostSerialNoAndIsDeleted(retrieveRequest.getCriterion(), isDeleted);
        List<Task> tasks = new ArrayList<>();
        for (Long taskId : taskIdList) {
            Task task = getTaskById(retrieveRequest.getStaffId(), taskId);
            tasks.add(task);
        }
        return RestResponseUtil.success(tasks);
    }

    /**
     * 获取指定id的交付件
     *
     * @param deliveryId deliveryId
     * @return Delivery
     */
    private Delivery getDeliveryById(Long staffId, Long deliveryId) throws BusinessException {
        String retrieveDeliveryUrl = webConfig.getRetrieveDeliveryUrl();
        return RestTemplateUtil.getDeliveryById(retrieveDeliveryUrl, new SingleCriterionRequest(staffId, deliveryId.toString()));
    }

    /**
     * 获取指定id的task的历史记录
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveHistory(SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("获取ID为【{}】的task的历史记录开始...", retrieveRequest.getCriterion());
        Long taskId = Long.valueOf(retrieveRequest.getCriterion());

        if (StringUtils.isEmpty(retrieveRequest.getCriterion())) {
            LOG.error("值为【{}】的查询参数错误.", retrieveRequest.getCriterion());
            throw new BusinessException(ErrorEnum.ILLEGAL_ARG, "查询参数错误");
        }
        PageableCriteria pageableCriteria = retrieveRequest.getPageableCriteria();
        //构建分页对象
        if (pageableCriteria != null) {
            PageRequest pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
            Page<TaskHistory> historyPage = this.historyRepository.findByTaskId(taskId, pageRequest);
            return RestResponseUtil.success(historyPage);
        }
        List<TaskHistory> list = this.historyRepository.findByTaskId(taskId);
        List<TaskHistory> historyList = new ArrayList<>();
        for (TaskHistory history: list) {
            String retrieveCreatorUrl = webConfig.getRetrieveCreatorUrl();
            //获取创建人
            Staff creator = RestTemplateUtil.retrieveCreator(retrieveCreatorUrl, new SingleCriterionRequest(retrieveRequest.getStaffId(), history.getCreatorId().toString()));
            history.setCreator(creator);
            String retrieveAttachUrl = webConfig.getRetrieveAttachUrl();
            //获取附件信息
            if (null!=history.getAddAttachId()) {
                AttchInfo attachAdded = RestTemplateUtil.retrieveAttach(retrieveAttachUrl, new SingleCriterionRequest(retrieveRequest.getStaffId(), history.getAddAttachId().toString()));
                history.setAddAttchInfo(attachAdded);
            }
            if (null!=history.getDeleteAttachId()) {
                AttchInfo attachRemoved = RestTemplateUtil.retrieveAttach(retrieveAttachUrl, new SingleCriterionRequest(retrieveRequest.getStaffId(), history.getDeleteAttachId().toString()));
                history.setDeleteAttchInfo(attachRemoved);
            }
            historyList.add(history);
        }
        LOG.info("获取task的历史记录成功...");
        return RestResponseUtil.success(historyList);
    }

}

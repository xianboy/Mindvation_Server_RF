package com.mdvns.mdvn.task.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.task.domain.UpdateProgressRequest;
import com.mdvns.mdvn.task.domain.entity.Task;
import com.mdvns.mdvn.task.repository.TaskRepository;
import com.mdvns.mdvn.task.service.RetrieveService;
import com.mdvns.mdvn.task.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UpdateServiceImpl implements UpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private TaskRepository repository;

    /**
     * 更新进度
     * @param updateRequest request
     * @return RestResponse
     * @throws BusinessException exception
     */
    @Override
    @Transactional
    @Modifying
    public RestResponse<?> updateProgress(UpdateProgressRequest updateRequest) throws BusinessException {
        LOG.info("更新进度开始...");
        if (!StringUtils.isEmpty(updateRequest.getComment())) {
            this.repository.updateProgress(updateRequest.getProgress(), updateRequest.getComment(), updateRequest.getHostId());
        } else {
            LOG.info("只更新进度，没有备注...");
            this.repository.updateProgress(updateRequest.getProgress(), updateRequest.getHostId());
        }
        LOG.info("更新进度完成...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }
}

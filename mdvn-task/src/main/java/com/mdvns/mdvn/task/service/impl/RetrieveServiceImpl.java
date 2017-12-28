package com.mdvns.mdvn.task.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.task.config.WebConfig;
import com.mdvns.mdvn.task.domain.entity.Task;
import com.mdvns.mdvn.task.repository.TaskRepository;
import com.mdvns.mdvn.task.service.RetrieveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        //查询
        Task task = this.repository.findOne(id);
        MdvnCommonUtil.notExistingError(task, ErrorEnum.TASK_NOT_EXISTS, "Id为【" + id + "】的task不存在...");
        String retrieveMembersUrl = webConfig.getRetrieveMembersUrl();
        List<Long> ids = new ArrayList<>();
        ids.add(task.getCreatorId());
        List<TerseInfo> list = RestTemplateUtil.retrieveBasicInfo(retrieveRequest.getStaffId(), ids, retrieveMembersUrl);
        MdvnCommonUtil.emptyList(list, ErrorEnum.STAFF_NOT_EXISTS, "id为【" + task.getCreatorId() + "】的Staff不存在.");
        task.setCreator(list.get(MdvnConstant.ZERO));
        LOG.info("根据Id获取详情成功...");
        return RestResponseUtil.success(task);
    }
}

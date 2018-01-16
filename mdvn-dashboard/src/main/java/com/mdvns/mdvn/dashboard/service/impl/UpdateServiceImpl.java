package com.mdvns.mdvn.dashboard.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpContentRequest;
import com.mdvns.mdvn.common.bean.UpdateMvpDashboardRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.dashboard.config.WebConfig;
import com.mdvns.mdvn.dashboard.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UpdateServiceImpl implements UpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private WebConfig webConfig;

    /**
     * 拖动(修改)某个模板的mvp Dashboard
     *
     * @param updateRequest updateRequest
     * @return RestResponse
     */
    @Override
    public RestResponse<?> update(UpdateMvpDashboardRequest updateRequest) throws BusinessException {

        Integer layerType = updateRequest.getLayerType();

        LOG.info("更新mvp Dashboard开始...");
        String updateMvpDashboardUrl = webConfig.getUpdateMvpDashboardByStoryUrl();
        //默认分为四层；如果是三层，则修改符合条件的requirement的mvpId
        if (MdvnConstant.THREE.equals(layerType)) {
            updateMvpDashboardUrl = webConfig.getUpdateMvpDashboardByReqmtUrl();
        }
        RestTemplate restTemplate = new RestTemplate();
        RestResponse restResponse = restTemplate.postForObject(updateMvpDashboardUrl, updateRequest, RestResponse.class);
        if (!restResponse.getCode().equals(MdvnConstant.SUCCESS_CODE)) {
            LOG.error("更新id为【{}】的MVP内容失败...");
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "更新mvp失败...");
        }
        LOG.info("更新mvp Dashboard成功...");

        return restResponse;
    }
}

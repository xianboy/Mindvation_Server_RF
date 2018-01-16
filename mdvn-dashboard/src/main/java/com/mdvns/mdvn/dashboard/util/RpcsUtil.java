package com.mdvns.mdvn.dashboard.util;

import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.dashboard.config.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RpcsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RpcsUtil.class);

    /**
     * 获取指定serialNo的项目的所有模板Id
     *
     * @param retrieveRequest request
     * @return List
     */
    public static List<Long> retrieveTemplates(String retrieveTemplateUrl,SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("获取serialNo为【{}】的项目的所有模板ID开始, URL为【{}】.", retrieveRequest.getCriterion(), retrieveTemplateUrl);
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<Long[]> typeRef = new ParameterizedTypeReference<Long[]>() {
        };
        ResponseEntity<Long[]> responseEntity = restTemplate.exchange(retrieveTemplateUrl, HttpMethod.POST, new HttpEntity<>(retrieveRequest), typeRef);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("获取ID为【{}】的项目模板ID失败...", retrieveRequest.getCriterion());
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "获取ID为【" + retrieveRequest.getCriterion() + "】的项目模板ID失败.");
        }
        LOG.info("成功获取serialNo为【{}】的项目的所有模板ID:【{}】", retrieveRequest.getCriterion(), responseEntity.getBody());
        return Arrays.asList(responseEntity.getBody());
    }
}

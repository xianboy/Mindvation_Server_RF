package com.mdvns.mdvn.dashboard.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveMvpContentRequest;
import com.mdvns.mdvn.common.bean.RetrieveReqmtSerialNoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.MvpDashboard;
import com.mdvns.mdvn.common.bean.model.ReqmtDashboard;
import com.mdvns.mdvn.common.bean.model.Story;
import com.mdvns.mdvn.common.bean.model.StoryDashboard;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.dashboard.config.WebConfig;
import com.mdvns.mdvn.dashboard.domain.entity.MvpTemplate;
import com.mdvns.mdvn.dashboard.repository.MvpRepository;
import com.mdvns.mdvn.dashboard.service.RetrieveService;
import com.mdvns.mdvn.dashboard.util.RpcsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);

    @Resource
    private MvpRepository repository;

    @Resource
    private WebConfig webConfig;

    /**
     * 获取指定serialNo的项目的 MVP Dashboard
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveMvpDashboard(SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("获取serialNo 为【{}】的项目的MVP Dashboard 开始...", retrieveRequest.getCriterion());
        //1.获取项目的所有模板Id;
        String retrieveTemplateUrl = webConfig.getRetrieveTemplateUrl();
        List<Long> templates = RpcsUtil.retrieveTemplates(retrieveTemplateUrl, retrieveRequest);
        List<MvpDashboard> dashboardList = new ArrayList<>();
        //遍历模板ID获取每个模板对应的看板信息
        for (Long templateId : templates) {
            MvpDashboard dashboard = buildMvpDashboardByTemplate(retrieveRequest.getStaffId(), retrieveRequest.getCriterion(), templateId);
            dashboardList.add(dashboard);
        }
        LOG.info("成功获取serialNo 为【{}】的项目的MVP Dashboard:【{}】.", retrieveRequest.getCriterion(), dashboardList);
        return RestResponseUtil.success(dashboardList);
    }

    /**
     * 获取指定的项目下指定模板Id对应的mvp Dashboard
     *
     * @param staffId      staffId
     * @param projSerialNo projSerialNo
     * @param templateId   templateId
     * @return MvpDashboard
     */
    private MvpDashboard buildMvpDashboardByTemplate(Long staffId, String projSerialNo, Long templateId) throws BusinessException {
        LOG.info("构建serialNo为【{}】的项目的模板ID为【{}】的Dashboard开始...", projSerialNo, templateId);
        //获取取项目中指定ID的模板对应的需求
        List<String> reqmtList = getReqmtSerialNo(new RetrieveReqmtSerialNoRequest(staffId, projSerialNo, templateId));
        MvpDashboard mvpDashboard = new MvpDashboard();
        //设置templateName
        mvpDashboard.setTemplateName(retrieveTemplateNameById(new SingleCriterionRequest(staffId, templateId.toString())));
        //分层:
        String retrieveProjectLayerTypeUrl = webConfig.getRetrieveProjectLayerTypeUrl();
        //分层标识
        Integer layerType = RestTemplateUtil.retrieveLayerType(retrieveProjectLayerTypeUrl, staffId, projSerialNo);
        //获取项目指定模板ID的前两个没有close的mvpId
        List<MvpTemplate> top2Mvp = this.repository.findTop2ByHostSerialNoAndTemplateIdAndStatusIsNotOrderByMvpIndexAsc(projSerialNo, templateId, MdvnConstant.CLOSE);
//        MdvnCommonUtil.emptyList(top2Mvp, ErrorEnum.MVP_NOT_EXISTS, "编号为【"+projSerialNo+"】的项目, ID为【"+templateId+"】的模板没有MVP");
        if (top2Mvp.size()==MdvnConstant.ZERO) {
            return null;
        }
        List<Long> top2MvpId = new ArrayList<>();
        for (MvpTemplate mvp : top2Mvp) {
            top2MvpId.add(mvp.getId());
        }
        //构建request
        RetrieveMvpContentRequest retrieveRequest = new RetrieveMvpContentRequest(staffId, top2MvpId, reqmtList);
        //分层(默认四层):三层时，mvp内容为requirement
        if (MdvnConstant.THREE.equals(layerType)) {
            mvpDashboard.setReqmtDashboard(getReqmtDashboard(retrieveRequest));
            return mvpDashboard;
        }

        mvpDashboard.setStoryDashboard(getStoryDashboard(retrieveRequest));
        //构建product backlogs
//        mvpDashboard.setBacklogs(retrieveMvpContent(new RetrieveMvpContentRequest(staffId, reqmtList)));

        //如果top2MvpId为空，就是项目对应的模板没有MVP
       /* if (null != top2Mvp && top2Mvp.size() > 0) {
            mvpDashboard.setCurrentMvp(retrieveMvpContent(new RetrieveMvpContentRequest(staffId, top2Mvp.get(MdvnConstant.ZERO).getId(), reqmtList)));
            if (null != top2Mvp.get(MdvnConstant.ONE)) {
                mvpDashboard.setNextMvp(retrieveMvpContent(new RetrieveMvpContentRequest(staffId, top2Mvp.get(MdvnConstant.ONE).getId(), reqmtList)));
            }
        }*/
        return mvpDashboard;
    }

    /**
     * 获取指定hostSerialNo集合下的Story的mvp dashboard
     * @param retrieveRequest request
     * @return StoryDashboard
     * @throws BusinessException StoryDashboard
     */
    private StoryDashboard getStoryDashboard(RetrieveMvpContentRequest retrieveRequest) throws BusinessException {
        LOG.info("获取hostSerialNo为指定需求集合【{}】的mvp dashboard开始...", retrieveRequest.getSerialNoList());
        String retrieveStoryDashboardUrl = webConfig.getRetrieveStoryDashboardUrl();
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<StoryDashboard> typeRef = new ParameterizedTypeReference<StoryDashboard>() {
        };
        ResponseEntity<StoryDashboard> responseEntity = restTemplate.exchange(retrieveStoryDashboardUrl, HttpMethod.POST, new HttpEntity<>(retrieveRequest), typeRef);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("获取指定hostSerialNo【{}】和mvpId为空的story失败...", retrieveRequest.getSerialNoList());
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "获取hostSerialNo为指定需求集合【" + retrieveRequest.getSerialNoList() + "】的MVP Dashboard失败.");
        }
        return responseEntity.getBody();
    }

    /**
     * 获取指定需求集合的mvp dashboard
     * @param retrieveRequest request
     * @return ReqmtDashboard
     */
    private ReqmtDashboard getReqmtDashboard(RetrieveMvpContentRequest retrieveRequest) throws BusinessException {
        LOG.info("获取指定需求集合【{}】的mvp dashboard开始...", retrieveRequest.getSerialNoList());
        String retrieveReqmtDashboardUrl = webConfig.getRetrieveReqmtDashboardUrl();
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<ReqmtDashboard> typeRef = new ParameterizedTypeReference<ReqmtDashboard>() {
        };
        ResponseEntity<ReqmtDashboard> responseEntity = restTemplate.exchange(retrieveReqmtDashboardUrl, HttpMethod.POST, new HttpEntity<>(retrieveRequest), typeRef);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("获取指定hostSerialNo【{}】和mvpId为空的story失败...", retrieveRequest.getSerialNoList());
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "获取指定需求编号集合【" + retrieveRequest.getSerialNoList() + "】的MVP Dashboard失败.");
        }
        return responseEntity.getBody();
    }

    /**
     * 根据Id获取模板信息
     *
     * @param retrieveRequest request
     * @return Template
     */
    private String retrieveTemplateNameById(SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("获取ID为【{}】的模板名称开始...", retrieveRequest.getCriterion());
        RestTemplate restTemplate = new RestTemplate();
        String retrieveTemplateNameUrl = webConfig.getRetrieveTemplateNameUrl();
        String templateName = restTemplate.postForObject(retrieveTemplateNameUrl, retrieveRequest, String.class);
        if (templateName.isEmpty()) {
            LOG.error("获取ID为【{}】的模板名称失败,name为空或数据不存在...", retrieveRequest.getCriterion());
            throw new BusinessException(ErrorEnum.TEMPLATE_SYSTEM_ERROR, "ID为【" + retrieveRequest.getCriterion() + "】的模板不存或name为空.");
        }
        LOG.info("成功获取ID为【{}】的模板名称:【{}】.", retrieveRequest.getCriterion(), templateName);
        return templateName;
    }

    /**
     * 获取指定hostSerialNo(暂为需求编号)及mvpId为空的Story
     *
     * @param retrieveRequest request
     * @return List
     */
    /*private List<Story> retrieveMvpContent(RetrieveMvpContentRequest retrieveRequest) throws BusinessException {
        LOG.info("获取hostSerialNo为【{}】的backlogs开始...", retrieveRequest.getHostSerialNoList());
        String retrieveMvpStoryContentUrl = webConfig.getRetrieveMvpStoryContentUrl();
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<Story[]> typeRef = new ParameterizedTypeReference<Story[]>() {
        };
        ResponseEntity<Story[]> responseEntity = restTemplate.exchange(retrieveMvpStoryContentUrl, HttpMethod.POST, new HttpEntity<>(retrieveRequest), typeRef);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("获取指定hostSerialNo【{}】和mvpId为空的story失败...", retrieveRequest.getHostSerialNoList());
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "获取指定hostSerialNo【" + retrieveRequest.getHostSerialNoList() + "】的backlogs失败.");
        }
        Story[] stories = responseEntity.getBody();
        LOG.info("成功获取hostSerialNo为【{}】的backlogs:【{}】.", retrieveRequest.getHostSerialNoList(), stories);
        return Arrays.asList(stories);
    }*/

    /**
     * 获取指定hostSerialNo和templateId对应的需求编号
     *
     * @param retrieveRequest request
     * @return List
     */
    private List<String> getReqmtSerialNo(RetrieveReqmtSerialNoRequest retrieveRequest) throws BusinessException {
        LOG.info("获取指定hostSerialNo【{}】和templateId【{}】对应的需求编号开始...", retrieveRequest.getHostSerialNo(), retrieveRequest.getTemplateId());
        String retrieveReqmtSerialNoUrl = webConfig.getRetrieveReqmtSerialNoUrl();
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<String[]> typeRef = new ParameterizedTypeReference<String[]>() {
        };
        ResponseEntity<String[]> responseEntity = restTemplate.exchange(retrieveReqmtSerialNoUrl, HttpMethod.POST, new HttpEntity<>(retrieveRequest), typeRef);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("获取指定hostSerialNo【{}】和templateId【{}】对应的需求编号失败...", retrieveRequest.getHostSerialNo(), retrieveRequest.getTemplateId());
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "获取指定hostSerialNo【" + retrieveRequest.getHostSerialNo() + "】和templateId【" + retrieveRequest.getTemplateId() + "】对应的需求编号失败.");
        }
        String[] result = responseEntity.getBody();
        LOG.info("成功获取指定hostSerialNo【{}】和templateId【{}】对应的需求编号:【{}】。", retrieveRequest.getHostSerialNo(), retrieveRequest.getTemplateId(), result);
        return Arrays.asList(result);
    }


}

package com.mdvns.mdvn.dashboard.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveReqmtByLabelRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.UpdateMvpContentRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.dashboard.config.WebConfig;
import com.mdvns.mdvn.dashboard.domain.CreateMvpRequest;
import com.mdvns.mdvn.dashboard.domain.entity.MvpTemplate;
import com.mdvns.mdvn.dashboard.repository.MvpRepository;
import com.mdvns.mdvn.dashboard.service.CreateService;
import com.mdvns.mdvn.dashboard.service.RetrieveService;
import com.mdvns.mdvn.dashboard.util.RpcsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CreateServiceImpl implements CreateService {
    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    @Resource
    private MvpRepository repository;

    @Resource
    private WebConfig webConfig;

    @Resource
    private RetrieveService retrieveService;


    /**
     * 创建mvp
     *
     * @param createRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> create(CreateMvpRequest createRequest) {
        MvpTemplate mvp = new MvpTemplate();
        mvp.setCreatorId(createRequest.getCreatorId());
        mvp.setMvpIndex(createRequest.getMvpIndex());

        return null;
    }


    /**
     * 智能排列指定serialNo的项目的Dashboard
     *
     * @param arrangeRequest request
     * @return RestResponse
     * @throws BusinessException BusinessException
     */
    @Override
    public RestResponse<?> arrangeMvp(SingleCriterionRequest arrangeRequest) throws BusinessException {
        LOG.info("Arrange MvpDashboard 开始...");
        //获取指定serialNo的项目的所有模板信息
        String retrieveTemplateUrl = webConfig.getRetrieveTemplateUrl();
        List<Long> templates = RpcsUtil.retrieveTemplates(retrieveTemplateUrl, arrangeRequest);
        //1.查询指定ID的模板的迭代计划
        List<MvpTemplate> mvpTemplates;
        for (Long templateId : templates) {
            //获取指定Id的模板的迭代计划
            mvpTemplates = retrieveMvpTemplates(new SingleCriterionRequest(arrangeRequest.getStaffId(), templateId.toString()));
            MdvnCommonUtil.emptyList(mvpTemplates, ErrorEnum.TEMPLATE_SYSTEM_ERROR, "ID为【" + templateId + "】的模板没有迭代计划");
            //2.根据模板迭代计划构建项目Dashboard(MVP)
            for (MvpTemplate mvpTemplate : mvpTemplates) {
                buildDashboardByMvpTemplate(arrangeRequest.getStaffId(), mvpTemplate, arrangeRequest.getCriterion(), templateId);
            }
        }
        //查询该项目的Dashboard并返回
        return this.retrieveService.retrieveMvpDashboard(arrangeRequest);
    }

    /**
     * 根据模板的迭代计划创建Dashboard
     * 1.获取mvpTemplate下过程方法的Id
     * 2.获取项目中以上过程方法对应的requirement
     * 3.修改requirement中的Story的mvpId为新建的mvp的Id
     *
     * @param staffId      当前用户ID
     * @param mvpTemplate  模板的迭代计划
     * @param hostSerialNo 项目编号
     */
    private void buildDashboardByMvpTemplate(Long staffId, MvpTemplate mvpTemplate, String hostSerialNo, Long templateId) throws BusinessException {
        LOG.info("根据模板的迭代计划创建Dashboard开始...");
        //根据迭代计划给Dashboard新建mvp
        MvpTemplate mvp = create(staffId, mvpTemplate.getMvpIndex(), hostSerialNo, templateId);
        //获取mvpTemplate下过程方法的Id
        List<Long> contents = retrieveMvpTemplateContents(staffId, mvpTemplate.getId());

        if (contents.size() > 0) {

            //获取项目中指定过程方法对应的requirement的serialNo
            List<String> list = getReqmtSerialNo(staffId, hostSerialNo, contents);
            //修改这些需求中的Story的mvpId为新建的mvpId
            updateMvpContent(staffId, list, mvp.getId(), hostSerialNo);
        }
        LOG.info("根据模板的迭代计划创建Dashboard成功.");
    }

    /**
     * 创建MVP
     *
     * @param staffId      当前用户
     * @param mvpIndex     mvp编号
     * @param hostSerialNo hostSerialNo
     * @param templateId   模板ID
     * @return MvpTemplate
     */
    private MvpTemplate create(Long staffId, Integer mvpIndex, String hostSerialNo, Long templateId) {
        LOG.info("给编号为【{}】的模块创建模板ID为【{}】的第【{}】个MVP开始...", hostSerialNo, templateId, mvpIndex);
        MvpTemplate mvp = new MvpTemplate();
        mvp.setCreatorId(staffId);
        mvp.setMvpIndex(mvpIndex);
        mvp.setHostSerialNo(hostSerialNo);
        mvp.setTemplateId(templateId);
        mvp.setEndTime(new Timestamp(System.currentTimeMillis()));
        mvp.setStartTime(new Timestamp(System.currentTimeMillis()));
        mvp.setStatus(MdvnConstant.NEW);
        mvp = this.repository.saveAndFlush(mvp);
        LOG.info("成功给编号为【{}】的模块创建模板ID为【{}】的第【{}】个MVP：【{}】.", hostSerialNo, templateId, mvpIndex, mvp);
        return mvp;
    }

    /**
     * 修改指定需求集合中的mvpId
     * 默认四层
     * @param staffId 当前用户ID
     * @param list    需求编号集合
     * @param mvpId   mvpId
     * @param hostSerialNo 项目编号
     */
    private void updateMvpContent(Long staffId, List<String> list, Long mvpId, String hostSerialNo) throws BusinessException {
        //分层
        String retrieveProjectLayerTypeUrl = webConfig.getRetrieveProjectLayerTypeUrl();
        Integer layerType = RestTemplateUtil.retrieveLayerType(retrieveProjectLayerTypeUrl, staffId, hostSerialNo);
        String updateMvpContentUrl = webConfig.getCreateStoryMvpUrl();
        //如果是四层
        /*if (MdvnConstant.FOUR.equals(layerType)) {
            LOG.info("更新hostSerialNo为【{}】的story的mvpId为【{}】开始...", list.toString(), mvpId);
            updateMvpContentUrl = webConfig.getUpdateStoryMvpUrl();
        }*/
        //如果是三层,更新requirement下的mvpId
        if (MdvnConstant.THREE.equals(layerType)) {
            updateMvpContentUrl = webConfig.getUpdateReqmtMvpUrl();
        }

        //更新mvpId
        RestTemplate restTemplate = new RestTemplate();
        RestResponse restResponse = restTemplate.postForObject(updateMvpContentUrl, new UpdateMvpContentRequest(staffId, list, mvpId), RestResponse.class);
        if (!restResponse.getCode().equals(MdvnConstant.SUCCESS_CODE)) {
            LOG.error("更新id为【{}】的MVP内容失败...", mvpId);
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR, "更新mvp失败...");
        }
        LOG.info("更新hostSerialNo为【{}】, 的mvpId为【{}】成功.", list, mvpId);
    }

    /**
     * 获取项目下指定过程方法集合对应的需求编号
     *
     * @param staffId 当前用户Id
     * @param labels  模板下所有过程方法Id
     * @return 需求编号
     */
    private List<String> getReqmtSerialNo(Long staffId, String hostSerialNo, List<Long> labels) {
        LOG.info("获取FunctionLabelId为【{}】的需求的serialNo开始...", labels);
        RestTemplate restTemplate = new RestTemplate();
        String retrieveReqmtSerialNoByLabel = webConfig.getRetrieveReqmtSerialNoByLabel();
        String[] list = restTemplate.postForObject(retrieveReqmtSerialNoByLabel, new RetrieveReqmtByLabelRequest(staffId, hostSerialNo, labels), String[].class);
        LOG.info("成功获取FunctionLabelId为【{}】的需求的serialNo为【{}】...", labels, list);
        return Arrays.asList(list);
    }

    /**
     * 获取指定ID的MvpTemplate所包含的过程方法Id
     *
     * @param staffId 当前用户ID
     * @param mvpId   mvpId
     * @return List
     */
    private List<Long> retrieveMvpTemplateContents(Long staffId, Long mvpId) {
        LOG.info("获取ID为【{}】的MvpTemplate包含的过程方法ID开始...", mvpId);
        RestTemplate restTemplate = new RestTemplate();
        String retrieveMvpLabelUrl = webConfig.getRetrieveMvpLabelUrl();
        Long[] labels = restTemplate.postForObject(retrieveMvpLabelUrl, new SingleCriterionRequest(staffId, mvpId.toString()), Long[].class);
        LOG.info("成功获取到ID为【{}】的MvpTemplate包含的过程方法的ID:【{}】...", mvpId, labels.toString());
        return Arrays.asList(labels);
    }

    /**
     * 获取指定ID的模板的迭代计划
     *
     * @param retrieveRequest retrieveRequest
     * @return List<MvpTemplate>
     */
    private List<MvpTemplate> retrieveMvpTemplates(SingleCriterionRequest retrieveRequest) throws BusinessException {
        LOG.info("获取编号为【{}】的模板的迭代计划开始...", retrieveRequest.getCriterion());
        String retrieveMvpTemplatesUrl = webConfig.getRetrieveMvpTemplatesUrl();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MvpTemplate[]> responseEntity = restTemplate.postForEntity(retrieveMvpTemplatesUrl, retrieveRequest, MvpTemplate[].class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new BusinessException(ErrorEnum.TEMPLATE_SYSTEM_ERROR, "获取ID为【" + retrieveRequest.getCriterion() + "}】的模板的迭代计划失败...");
        }
        MvpTemplate[] mvpTemplates = responseEntity.getBody();
        LOG.info("成功获取编号为【{}】的模板的迭代计划:【{}】", retrieveRequest.getCriterion(), mvpTemplates);
        return Arrays.asList(mvpTemplates);
    }
}

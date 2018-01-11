package com.mdvns.mdvn.issue.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.exception.BusinessException;

import com.mdvns.mdvn.common.util.PageableQueryUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.issue.config.WebConfig;
import com.mdvns.mdvn.issue.domain.IssueInfo;
import com.mdvns.mdvn.issue.domain.IssueListResponse;
import com.mdvns.mdvn.issue.domain.entity.Issue;
import com.mdvns.mdvn.issue.repository.IssueRepository;
import com.mdvns.mdvn.issue.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Description: Issue sapi业务处理
 * @Date:
 */
@Service
public class IssueServiceImpl implements IssueService {

    /* 日志常亮 */
    private static final Logger LOG = LoggerFactory.getLogger(IssueServiceImpl.class);

    private final String CLASS = this.getClass().getName();
    /*Dashboard Repository*/
    @Autowired
    private IssueRepository issueRepository;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /**
     * 获取悬赏榜列表信息(解决/未解决)
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> rtrvIssueList(SingleCriterionRequest request) throws BusinessException {
        //后见返回对象issueListResponse
        IssueListResponse issueListResponse = new IssueListResponse();
        //获取条件参数（解决/未解决）
        String criterion = request.getCriterion();
        //获取分页参数对象
        PageableCriteria pageableCriteria = request.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", request.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Issue> issuePage = null;
        if (criterion.equals("unsolved")) {
            issuePage = this.issueRepository.findByIsResolvedAndIsDeleted(pageRequest, 0, 0);
        }
        if (criterion.equals("resolved")) {
            issuePage = this.issueRepository.findByIsResolvedAndIsDeleted(pageRequest, 1, 0);
        }
        //返回对象信息
        List<IssueInfo> issueInfos = new ArrayList<>();
        List<Issue> issues = issuePage.getContent();
        for (int i = 0; i < issues.size(); i++) {
            Issue issue = issues.get(i);
            IssueInfo issueInfo = this.getIssueInfo(issue);
            issueInfos.add(issueInfo);
        }
        issueListResponse.setIssueInfos(issueInfos);
        issueListResponse.setTotalElements(issuePage.getTotalElements());
        //返回结果
        return RestResponseUtil.success(issueListResponse);
    }


    /**
     * 通过staffId获取staff详情
     *
     * @param id
     * @return
     */
    public Staff rtrvStaffInfoById(Long id) {
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
        singleCriterionRequest.setCriterion(String.valueOf(id));
        singleCriterionRequest.setStaffId(id);
        ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
        };
        ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
        RestResponse<Staff> restResponse = responseEntity.getBody();
        return restResponse.getData();
    }


    /**
     * 赋值并返回人员及标签对象信息
     *
     * @param issue
     * @return
     */
    private IssueInfo getIssueInfo(Issue issue) {
        IssueInfo issueInfo = new IssueInfo();
        //赋值...
        BeanUtils.copyProperties(issue, issueInfo);
        issueInfo.setCreateTime(issue.getCreateTime().getTime());
        //查询创建者对象信息
        Staff creatorInfo = this.rtrvStaffInfoById(issue.getCreatorId());
        issueInfo.setCreatorInfo(creatorInfo);
        if (!StringUtils.isEmpty(issue.getTagId())) {
            //实例化restTemplate对象
            RestTemplate restTemplate = new RestTemplate();
            //查询tag对象信息
            String findByIdUrl = webConfig.getFindTagInfoByIdUrl();
            findByIdUrl = StringUtils.replace(findByIdUrl, "{tagId}", String.valueOf(issue.getTagId()));
            Tag tag = restTemplate.postForObject(findByIdUrl, issue.getTagId(), Tag.class);
            issueInfo.setTagInfo(tag);
        }
        return issueInfo;
    }

}

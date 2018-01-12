package com.mdvns.mdvn.issue.service.impl;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.exception.BusinessException;

import com.mdvns.mdvn.common.util.PageableQueryUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.StaffUtil;
import com.mdvns.mdvn.issue.config.WebConfig;
import com.mdvns.mdvn.issue.domain.IssueInfo;
import com.mdvns.mdvn.issue.domain.IssueListResponse;
import com.mdvns.mdvn.issue.domain.IssueRanking;
import com.mdvns.mdvn.issue.domain.entity.Issue;
import com.mdvns.mdvn.issue.repository.IssueAnswerRepository;
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
import java.util.Collections;
import java.util.Comparator;
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
    @Autowired
    private IssueAnswerRepository issueAnswerRepository;

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
     * 获取求助的排行榜
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse<?> rtrvIssueRankingList(PageableQueryWithoutArgRequest request) {
        List<IssueRanking> issueRankings = new ArrayList<>();
        /**
         * 1.查询回答过issue的所有员工
         */
        List<Long> staffIdList = this.issueAnswerRepository.findAllIssueStaffList();
        /**
         * 2.查询每一个员工回答过得所有求助issue（已解决的）
         */
        for (int i = 0; i < staffIdList.size(); i++) {
            IssueRanking issueRanking = new IssueRanking();
            Long staffId = Long.parseLong(String.valueOf(staffIdList.get(i)));
            /*获取某个员工对象信息*/
            String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
            Staff staffInfo = StaffUtil.rtrvStaffInfoById(staffId,retrieveByIdUrl);
            issueRanking.setCreatorInfo(staffInfo);
            List<Issue> issueList = this.issueRepository.findAllIssueListHaveResolved(staffId);
            issueRanking.setResolveNum(Long.valueOf(issueList.size()));
            /**
             * 3.查询员工回答过的issue里面采纳员工的issue(自己被采纳的)
             */
            List<Issue> issueListHaveAdopt = this.issueRepository.findAllIssueListHaveAdopt(staffId);
            issueRanking.setAdoptNum(Long.valueOf(issueListHaveAdopt.size()));
            /**
             * 4.算出比例
             */
            Float proportion = Float.valueOf(issueListHaveAdopt.size()/issueList.size()*100);
            issueRanking.setProportion(proportion);
            issueRankings.add(issueRanking);
        }
        //排序
        Collections.sort(issueRankings, new Comparator<IssueRanking>() {

            public int compare(IssueRanking o1, IssueRanking o2) {

                // 按照学生的年龄进行降序排列
                if (o1.getProportion() > o2.getProportion()) {
                    return -1;
                }
                if (o1.getProportion() == o2.getProportion()) {
                    return 0;
                }
                return 1;
            }
        });
        //给出编号
        for (int i = 0; i < issueRankings.size(); i++) {
            issueRankings.get(i).setNoun(Long.valueOf(i+1));
        }

        return RestResponseUtil.success(issueRankings);
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
        /*获取某个员工对象信息*/
        String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        Staff creatorInfo = StaffUtil.rtrvStaffInfoById(issue.getCreatorId(),retrieveByIdUrl);
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

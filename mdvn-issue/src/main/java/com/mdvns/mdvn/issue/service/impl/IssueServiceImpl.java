package com.mdvns.mdvn.issue.service.impl;

import com.mdvns.mdvn.common.beans.*;
import com.mdvns.mdvn.common.beans.exception.ExceptionEnum;
import com.mdvns.mdvn.common.utils.FetchListUtil;
import com.mdvns.mdvn.common.utils.MdvnStringUtil;
import com.mdvns.mdvn.common.utils.RestResponseUtil;
import com.mdvns.mdvn.issue.config.WebConfig;
import com.mdvns.mdvn.issue.domain.*;
import com.mdvns.mdvn.issue.domain.entity.Issue;
import com.mdvns.mdvn.issue.domain.entity.IssueAnswer;
import com.mdvns.mdvn.issue.repository.IssueAnswerRepository;
import com.mdvns.mdvn.issue.repository.IssueRepository;
import com.mdvns.mdvn.issue.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

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

    /* 注入RestTemplate*/
    @Autowired
    private RestTemplate restTemplate;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /*注入RestResponse*/
    @Autowired
    private RestResponse restResponse;


    /**
     * 发布求助（创建）
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse createIssueInfo(CreateIssueRequest request) {
        LOG.info("开始执行{} createIssueInfo()方法.", this.CLASS);

        IssueInfo issueInfo = new IssueInfo();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        //save Issue表
        Issue issue = new Issue();
        String subjectId = request.getSubjectId();
        String creatorId = request.getCreatorId();
        String content = request.getContent();
        Integer reward = request.getReward();
        String tagId = request.getTagId();
        issue.setCreateTime(currentTime);
        issue.setSubjectId(subjectId);
        issue.setCreatorId(creatorId);
        issue.setContent(content);
        issue.setReward(reward);
        issue.setTagId(tagId);
        issue.setProjId(request.getProjId());
        issue.setIsResolved(0);
        issue = this.issueRepository.saveAndFlush(issue);
        issue.setIssueId("IS" + issue.getUuId());
        issue = this.issueRepository.saveAndFlush(issue);

        //赋值·
        BeanUtils.copyProperties(issue, issueInfo);
        issueInfo.setCreateTime(currentTime.getTime());

        //查询创建者对象信息
        Staff creatorInfo = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), creatorId, Staff.class);
        issueInfo.setCreatorInfo(creatorInfo);
        if (!StringUtils.isEmpty(issue.getTagId())) {
            //查询tag对象信息
            String findByIdUrl = webConfig.getFindTagInfoByIdUrl();
            findByIdUrl = StringUtils.replace(findByIdUrl, "{tagId}", tagId);
            Tag tag = this.restTemplate.postForObject(findByIdUrl, tagId, Tag.class);
            issueInfo.setTagInfo(tag);
        }
        /**
         * 创建完返回详情
         */
        RtrvIssueDetailRequest rtrvRequest = new RtrvIssueDetailRequest();
        rtrvRequest.setIssueId(issue.getIssueId());
        rtrvRequest.setProjId(issue.getProjId());
        rtrvRequest.setSubjectId(issue.getSubjectId());

        /**
         * 消息推送(创建issue)
         */
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            ServerPush serverPush = new ServerPush();
            String initiatorId = request.getCreatorId();
//            String subjectId = request.getSubjectId();
            Staff initiator = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), initiatorId, Staff.class);
            serverPush.setInitiator(initiator);
            serverPush.setSubjectType("issue");
            serverPush.setSubjectId(subjectId);
            serverPush.setType("create");
            //查询所评论的需求或者story的创建者
            String createId = this.restTemplate.postForObject(webConfig.getRtrvCreatorIdUrl(), subjectId, String.class);
            if (subjectId.substring(0, 1).equals("R")) {
                // call reqmnt sapi
                ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<List<ReqmntMember>>() {
                };
                List<ReqmntMember> data = FetchListUtil.fetch(restTemplate, webConfig.getRtrvReqmntMembersUrl(), subjectId, parameterizedTypeReference);
                List<ReqmntMember> reqmntMembers = data;
                List<String> memberIds = new ArrayList<>();
                for (int i = 0; i < reqmntMembers.size(); i++) {
                    String id = reqmntMembers.get(i).getStaffId();
                    if (!memberIds.isEmpty() && memberIds.contains(id)) {
                        continue;
                    }
                    memberIds.add(id);
                }
                sendMessageRequest.setStaffIds(memberIds);
            }
            if (subjectId.substring(0, 1).equals("S")) {
                RtrvStoryDetailRequest rtrvStoryDetailRequest = new RtrvStoryDetailRequest();
                rtrvStoryDetailRequest.setStoryId(subjectId);
                rtrvStoryDetailRequest.setStaffId(initiatorId);
                // call story sapi
                ParameterizedTypeReference reqmntTagTypeReference = new ParameterizedTypeReference<List<StoryRoleMember>>() {
                };
                List<StoryRoleMember> storyRoleMembers = FetchListUtil.fetch(restTemplate, webConfig.getRtrvSRoleMembersUrl(), rtrvStoryDetailRequest, reqmntTagTypeReference);
                //选出不同的角色
                List<String> staffIds = new ArrayList<String>();
                for (int i = 0; i < storyRoleMembers.size(); i++) {
                    String id = storyRoleMembers.get(i).getStaffId();
                    if (!staffIds.isEmpty() && staffIds.contains(id)) {
                        continue;
                    }
                    staffIds.add(id);
                }
                sendMessageRequest.setStaffIds(staffIds);
            }
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setServerPushResponse(serverPush);
            Boolean flag = this.restTemplate.postForObject(webConfig.getSendMessageUrl(), sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送(创建issue)出现异常，异常信息：" + e);
        }

//        restResponse.setResponseBody(issueInfo);
//        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
//        restResponse.setResponseMsg("请求成功");
//        restResponse.setResponseCode("000");

        LOG.info("结束执行{} createIssueInfo()方法.", this.CLASS);
        return rtrvIssueDetail(rtrvRequest);
    }


    /**
     * 创建answer
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse createIssueAnswerInfo(CreateIssueAnswerRequest request) {
        LOG.info("开始执行{} createIssueAnswerInfo()方法.", this.CLASS);
        IssueAnswerInfo issueAnswerInfo = new IssueAnswerInfo();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long curTime = currentTime.getTime();
        String issueId = request.getIssueId();
        String creatorId = request.getCreatorId();
        String content = request.getContent();
        //save IssueAnswer表
        IssueAnswer issueAnswer = new IssueAnswer();
        issueAnswer.setIssueId(issueId);
        issueAnswer.setContent(content);
        issueAnswer.setCreateTime(currentTime);
        issueAnswer.setIsAdopt(0);
        issueAnswer.setLikeQty(0);
        issueAnswer.setDislikeQty(0);
        issueAnswer.setCreatorId(creatorId);
        issueAnswer.setProjId(request.getProjId());
        issueAnswer = this.issueAnswerRepository.saveAndFlush(issueAnswer);
        issueAnswer.setAnswerId("AS" + issueAnswer.getUuId());
        issueAnswer = this.issueAnswerRepository.saveAndFlush(issueAnswer);

        //赋值·
        BeanUtils.copyProperties(issueAnswer, issueAnswerInfo);
        issueAnswerInfo.setCreateTime(curTime);
        //查询创建者对象信息
        Staff creatorInfo = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), creatorId, Staff.class);
        issueAnswerInfo.setCreatorInfo(creatorInfo);

        /**
         * 消息推送(创建answer)
         */
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            ServerPush serverPush = new ServerPush();
            Issue issue = this.issueRepository.findByIssueId(issueId);
            String initiatorId = request.getCreatorId();
            String subjectId = issue.getSubjectId();
            Staff initiator = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), initiatorId, Staff.class);
            serverPush.setInitiator(initiator);
            serverPush.setSubjectType("answer");
            serverPush.setSubjectId(subjectId);
            serverPush.setType("create");
            //查询issue的创建者
            String createId = issue.getCreatorId();
            List<String> staffIds = new ArrayList<>();
            staffIds.add(createId);
            sendMessageRequest.setStaffIds(staffIds);
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setServerPushResponse(serverPush);
            Boolean flag = this.restTemplate.postForObject(webConfig.getSendMessageUrl(), sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送(创建answer)出现异常，异常信息：" + e);
        }

        restResponse.setResponseBody(issueAnswerInfo);
        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
        restResponse.setResponseMsg("请求成功");
        restResponse.setResponseCode("000");

        LOG.info("结束执行{} createIssueAnswerInfo()方法.", this.CLASS);
        return restResponse;
    }


    /**
     * 对Answer点赞或者踩
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse likeOrDislikeAnswer(LikeOrDislikeAnswerRequest request) {
        LOG.info("开始执行{} likeOrDislikeAnswer()方法.", this.CLASS);
        String remark = request.getRemark();
        String answerId = request.getAnswerId();
        String creatorId = request.getCreatorId();
        IssueAnswer issueAnswer = new IssueAnswer();
        issueAnswer = this.issueAnswerRepository.findByAnswerId(answerId);
        if (remark.equals("like")) {
            //对于点赞这边
            String likeIds = issueAnswer.getLikeIds();
            if (!StringUtils.isEmpty(likeIds)) {
                String[] likeArrayIds = likeIds.split(",");
                List<String> likeIdList = Arrays.asList(likeArrayIds);
                List list = new ArrayList(likeIdList);
                for (int i = 0; i < list.size(); i++) {
                    if (creatorId.equals(list.get(i))) {
                        list.remove(creatorId);
                    }
                }
                likeIds = MdvnStringUtil.join(list, ",");
                if (likeIdList.size() == list.size()) {
                    issueAnswer.setLikeQty(issueAnswer.getLikeQty() + 1);
                    issueAnswer.setLikeIds(likeIds + "," + creatorId);
                } else {
                    issueAnswer.setLikeQty(issueAnswer.getLikeQty() - 1);
                    issueAnswer.setLikeIds(likeIds);
                }
            } else {
                issueAnswer.setLikeQty(1);
                issueAnswer.setLikeIds(creatorId);
            }
            //对于踩这边
            String dislikeIds = issueAnswer.getDislikeIds();
            if (!StringUtils.isEmpty(dislikeIds)) {
                String[] dislikeArrayIds = dislikeIds.split(",");
                List<String> dislikeIdList = Arrays.asList(dislikeArrayIds);
                List list = new ArrayList(dislikeIdList);
                for (int i = 0; i < list.size(); i++) {
                    if (creatorId.equals(list.get(i))) {
                        list.remove(creatorId);
                    }
                }
                dislikeIds = MdvnStringUtil.join(list, ",");
                if (dislikeIdList.size() != list.size()) {
                    issueAnswer.setDislikeQty(issueAnswer.getDislikeQty() - 1);
                    issueAnswer.setDislikeIds(dislikeIds);
                }
            }
        }
        if (remark.equals("dislike")) {
            //对于踩这边
            String dislikeIds = issueAnswer.getDislikeIds();
            if (!StringUtils.isEmpty(dislikeIds)) {
                String[] dislikeArrayIds = dislikeIds.split(",");
                List<String> dislikeIdList = Arrays.asList(dislikeArrayIds);
                List list = new ArrayList(dislikeIdList);
                for (int i = 0; i < list.size(); i++) {
                    if (creatorId.equals(list.get(i))) {
                        list.remove(creatorId);
                    }
                }
                dislikeIds = MdvnStringUtil.join(list, ",");
                if (dislikeIdList.size() == list.size()) {
                    issueAnswer.setDislikeQty(issueAnswer.getDislikeQty() + 1);
                    issueAnswer.setDislikeIds(dislikeIds + "," + creatorId);
                } else {
                    issueAnswer.setDislikeQty(issueAnswer.getDislikeQty() - 1);
                    issueAnswer.setDislikeIds(dislikeIds);
                }
            } else {
                issueAnswer.setDislikeQty(1);
                issueAnswer.setDislikeIds(creatorId);
            }
            //对于点赞这边
            String likeIds = issueAnswer.getLikeIds();
            if (!StringUtils.isEmpty(likeIds)) {
                String[] likeArrayIds = likeIds.split(",");
                List<String> likeIdList = Arrays.asList(likeArrayIds);
                List list = new ArrayList(likeIdList);
                for (int i = 0; i < list.size(); i++) {
                    if (creatorId.equals(list.get(i))) {
                        list.remove(creatorId);
                    }
                }
                likeIds = MdvnStringUtil.join(list, ",");
                if (likeIdList.size() != list.size()) {
                    issueAnswer.setLikeQty(issueAnswer.getLikeQty() - 1);
                    issueAnswer.setLikeIds(likeIds);
                }
            }
        }
        issueAnswer = this.issueAnswerRepository.saveAndFlush(issueAnswer);

        restResponse.setResponseBody(issueAnswer);
        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
        restResponse.setResponseMsg("请求成功");
        restResponse.setResponseCode("000");

        LOG.info("结束执行{} likeOrDislikeAnswer()方法.", this.CLASS);
        return restResponse;
    }

    /**
     * 获取某个reqmnt或者story的单个求助信息（同时返回该reqmnt或者story下的其他求助信息的key：id,value:是否解决）
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse rtrvIssueDetail(RtrvIssueDetailRequest request) {
        LOG.info("开始执行{} rtrvIssueDetail()方法.", this.CLASS);
        RtrvIssueDetailResponse rtrvIssueDetailResponse = new RtrvIssueDetailResponse();
        /**
         * 1.issueInfo
         */
        IssueDetail issueDetail = new IssueDetail();
        String subjectId = request.getSubjectId();
        String issueId = request.getIssueId();
        Issue issue = new Issue();
        if (StringUtils.isEmpty(issueId)) {
            issue = this.issueRepository.findFirstBySubjectId(subjectId);
        } else {
            issue = this.issueRepository.findByIssueId(issueId);
        }
        IssueInfo issueInfo = new IssueInfo();
        if (issue == null) {
//            issueDetail.setIssueInfo(null);
            rtrvIssueDetailResponse.setIssueDetail(null);
            rtrvIssueDetailResponse.setTotalElements(new ArrayList<>());
            return RestResponseUtil.success(rtrvIssueDetailResponse);//一个都没有就返回null
        } else {
            //赋值...
            BeanUtils.copyProperties(issue, issueInfo);
            issueInfo.setCreateTime(issue.getCreateTime().getTime());
            //查询创建者对象信息
            Staff creatorInfo = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), issue.getCreatorId(), Staff.class);
            issueInfo.setCreatorInfo(creatorInfo);
            if (!StringUtils.isEmpty(issue.getTagId())) {
                //查询tag对象信息
                String findByIdUrl = webConfig.getFindTagInfoByIdUrl();
                findByIdUrl = StringUtils.replace(findByIdUrl, "{tagId}", issue.getTagId());
                Tag tag = this.restTemplate.postForObject(findByIdUrl, issue.getTagId(), Tag.class);
                issueInfo.setTagInfo(tag);
            }
            issueDetail.setIssueInfo(issueInfo);
        }
        /**
         * 2.issueAnswerInfos
         */
        List<IssueAnswerInfo> issueAnswerInfos = new ArrayList<>();
        if (null == issue) {
            issueDetail.setIssueAnswerInfos(issueAnswerInfos);
        } else {
            List<IssueAnswer> issueAnswers = this.issueAnswerRepository.findByIssueId(issue.getIssueId());
            for (int i = 0; i < issueAnswers.size(); i++) {
                IssueAnswer issueAnswer = issueAnswers.get(i);
                String answerId = issueAnswer.getAnswerId();
                IssueAnswerInfo issueAnswerInfo = new IssueAnswerInfo();
                //赋值·
                BeanUtils.copyProperties(issueAnswer, issueAnswerInfo);
                issueAnswerInfo.setCreateTime(issueAnswer.getCreateTime().getTime());
                //查询创建者对象信息
                Staff answerCreatorInfo = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), issueAnswer.getCreatorId(), Staff.class);
                issueAnswerInfo.setCreatorInfo(answerCreatorInfo);
                /**
                 * 3.comments
                 */
                //加上评论list
                String rCommentInfosUrl = webConfig.getRtrvCommentInfosUrl();
                RtrvCommentInfosRequest rtrvCommentInfosRequest = new RtrvCommentInfosRequest();
                rtrvCommentInfosRequest.setProjId(request.getProjId());
                rtrvCommentInfosRequest.setSubjectId(answerId);
                ParameterizedTypeReference trReference = new ParameterizedTypeReference<List<CommentDetail>>() {
                };
                List<CommentDetail> comDetails = FetchListUtil.fetch(restTemplate, rCommentInfosUrl, rtrvCommentInfosRequest, trReference);
                for (int j = 0; j < comDetails.size(); j++) {
                    //创建者返回对象
                    String staffUrl = webConfig.getRtrvStaffInfoUrl();
                    String creatorId = comDetails.get(j).getComment().getCreatorId();
                    Staff staff = restTemplate.postForObject(staffUrl, creatorId, Staff.class);
                    comDetails.get(j).getComment().setCreatorInfo(staff);
                    //被@的人返回对象
                    if (comDetails.get(j).getComment().getReplyId() != null) {
                        String passiveAt = comDetails.get(j).getReplyDetail().getCreatorId();
                        Staff passiveAtInfo = restTemplate.postForObject(staffUrl, passiveAt, Staff.class);
                        comDetails.get(j).getReplyDetail().setCreatorInfo(passiveAtInfo);
                    }
                }
                issueAnswerInfo.setCommentDetails(comDetails);
                issueAnswerInfos.add(issueAnswerInfo);
            }
            issueDetail.setIssueAnswerInfos(issueAnswerInfos);
        }
        rtrvIssueDetailResponse.setIssueDetail(issueDetail);
        /**
         * 4.totalElements
         */
        List<Issue> issues = this.issueRepository.findBySubjectId(subjectId);
        List<IssueIdAndIsResolved> totalElements = new ArrayList<>();
        for (int i = 0; i < issues.size(); i++) {
            IssueIdAndIsResolved issueIdAndIsResolved = new IssueIdAndIsResolved();
            Issue iss = issues.get(i);
            issueIdAndIsResolved.setIssueId(iss.getIssueId());
            issueIdAndIsResolved.setIsResolved(iss.getIsResolved());
            totalElements.add(issueIdAndIsResolved);
        }
        rtrvIssueDetailResponse.setTotalElements(totalElements);

        restResponse.setResponseBody(rtrvIssueDetailResponse);
        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
        restResponse.setResponseMsg("请求成功");
        restResponse.setResponseCode("000");

        LOG.info("结束执行{} rtrvIssueDetail()方法.", this.CLASS);
        return restResponse;
    }

    /**
     * 采纳（answer中isAdopt变为1，issue中isResolved变为1）
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse adoptAnswer(adoptAnswerRequest request) {
        LOG.info("开始执行{} adoptAnswer()方法.", this.CLASS);
        String creatorId = request.getCreatorId();
        String issueId = request.getIssueId();
        String answerId = request.getAnswerId();
        RestResponse restResponse = new RestResponse();
        /**
         * issue中isResolved变为1
         */
        Issue issue = this.issueRepository.findByIssueId(issueId);
        if (!creatorId.equals(issue.getCreatorId())) {
            restResponse.setResponseCode(ExceptionEnum.STAFF_IS_NOT_CREATOR.getErroCode());
            restResponse.setResponseMsg(ExceptionEnum.STAFF_IS_NOT_CREATOR.getErrorMsg());
            return restResponse;
        }
        if (issue.getIsResolved() == 1) {
            restResponse.setResponseCode(ExceptionEnum.ISSUE_ALREADY_RESOLVED.getErroCode());
            restResponse.setResponseMsg(ExceptionEnum.ISSUE_ALREADY_RESOLVED.getErrorMsg());
            return restResponse;
        }
        issue.setIsResolved(1);
        this.issueRepository.saveAndFlush(issue);
        /**
         * answer中isAdopt变为1
         */
        IssueAnswer issueAnswer = this.issueAnswerRepository.findByAnswerId(answerId);
        issueAnswer.setIsAdopt(1);
        this.issueAnswerRepository.saveAndFlush(issueAnswer);

        /**
         * 创建完返回详情
         */
        RtrvIssueDetailRequest rtrvRequest = new RtrvIssueDetailRequest();
        rtrvRequest.setIssueId(issue.getIssueId());
        rtrvRequest.setProjId(issue.getProjId());
        rtrvRequest.setSubjectId(issue.getSubjectId());

        /**
         * 消息推送(采纳)
         */
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            ServerPush serverPush = new ServerPush();
            String initiatorId = request.getCreatorId();
            String subjectId = issue.getSubjectId();
            Staff initiator = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), initiatorId, Staff.class);
            serverPush.setInitiator(initiator);
            serverPush.setSubjectType("issue");
            serverPush.setSubjectId(subjectId);
            serverPush.setType("adopt");
            //查询issueAnswer的创建者
            String createId = issueAnswer.getCreatorId();
            List<String> staffIds = new ArrayList<>();
            staffIds.add(createId);
            sendMessageRequest.setStaffIds(staffIds);
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setServerPushResponse(serverPush);
            Boolean flag = this.restTemplate.postForObject(webConfig.getSendMessageUrl(), sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送(采纳)出现异常，异常信息：" + e);
        }

//        restResponse.setResponseBody(true);
//        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
//        restResponse.setResponseMsg("请求成功");
//        restResponse.setResponseCode("000");
        LOG.info("结束执行{} adoptAnswer()方法.", this.CLASS);
        return rtrvIssueDetail(rtrvRequest);
    }


}

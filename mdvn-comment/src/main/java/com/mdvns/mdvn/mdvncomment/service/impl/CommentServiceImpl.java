package com.mdvns.mdvn.mdvncomment.service.impl;


import com.mdvns.mdvn.common.beans.RestResponse;
import com.mdvns.mdvn.common.beans.SendMessageRequest;
import com.mdvns.mdvn.common.beans.ServerPush;
import com.mdvns.mdvn.common.beans.Staff;
import com.mdvns.mdvn.common.beans.exception.BusinessException;
import com.mdvns.mdvn.common.beans.exception.ExceptionEnum;
import com.mdvns.mdvn.common.utils.MdvnStringUtil;
import com.mdvns.mdvn.mdvncomment.config.WebConfig;
import com.mdvns.mdvn.mdvncomment.domain.*;
import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;
import com.mdvns.mdvn.mdvncomment.repository.CommentRepository;
import com.mdvns.mdvn.mdvncomment.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:
 * @Description: Comment sapi业务处理
 * @Date:
 */
@Service
public class CommentServiceImpl implements CommentService {

    /* 日志常亮 */
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final String CLASS = this.getClass().getName();
    /*Dashboard Repository*/
    @Autowired
    private CommentRepository commentRepository;

    /* 注入RestTemplate*/
    @Autowired
    private RestTemplate restTemplate;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /*注入RestResponse*/
    @Autowired
    private RestResponse restResponse;
    @Override
    public RestResponse createCommentInfo(CreateCommentInfoRequest request) {
        LOG.info("开始执行{} createCommentInfo()方法.",this.CLASS);
        if (request == null || request.getProjId() == null || request.getSubjectId() == null || request.getCreatorId() == null) {
            throw new NullPointerException("createCommentInfo 或项目Id/subjectId/登录者Id不能为空");
        }
        CreateCommentInfoResponse createCommentInfoResponse = new CreateCommentInfoResponse();
        //如果是回复的话，把回复的人加入到passiveAts中
        String commentId = request.getReplyId();
        if (request.getReplyId() != null) {
            Comment replyComm = rtrvCommentDetailInfo(commentId);
            List<String> passiveAts = request.getPassiveAts();
            if (passiveAts == null) {
                passiveAts = new ArrayList<>();
            }
            if (!passiveAts.contains(replyComm.getCreatorId())) {
                passiveAts.add(replyComm.getCreatorId());
                request.setPassiveAts(passiveAts);
            }
        }
        /**
         * 创建comment
         */
        try {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            //save Comment表
            Comment comment = new Comment();
            String projId = request.getProjId();
            String subjectId = request.getSubjectId();
            String creatorId = request.getCreatorId();
            String content = request.getContent();
            comment.setCreateTime(currentTime);
            comment.setProjId(projId);
            comment.setSubjectId(subjectId);
            comment.setCreatorId(creatorId);
            comment.setContent(content);
            comment.setLikeQty(0);
            comment.setDislikeQty(0);
            comment.setIsDeleted(0);
            List<String> passiveAtList = request.getPassiveAts();
            if (passiveAtList != null && passiveAtList.size() > 0) {
                String passiveAts = MdvnStringUtil.join(passiveAtList, ",");
                comment.setPassiveAts(passiveAts);
            }
            //如果是回复
            String replyId = request.getReplyId();
            if (!StringUtils.isEmpty(replyId)) {
                Comment comm = new Comment();
                comment.setReplyId(replyId);
                //算出间隔的时间
                comm = this.commentRepository.findByCommentId(replyId);
                //赋值·
                CommentInfo replyDetail = new CommentInfo();
                BeanUtils.copyProperties(comm, replyDetail);
                replyDetail.setCreateTime(comm.getCreateTime().getTime());
                createCommentInfoResponse.setReplyDetail(replyDetail);
                //间隔时间
                if (comm != null) {
                    long intervalTime = currentTime.getTime() - comm.getCreateTime().getTime();
                    comment.setIntervalTime(intervalTime);
                }
            }
            comment = this.commentRepository.saveAndFlush(comment);
            comment.setCommentId("C" + comment.getUuId());
            comment = this.commentRepository.saveAndFlush(comment);
            //赋值·
            CommentInfo commentInfo = new CommentInfo();
            BeanUtils.copyProperties(comment, commentInfo);
            commentInfo.setCreateTime(comment.getCreateTime().getTime());
            createCommentInfoResponse.setCommentInfo(commentInfo);
        } catch (Exception ex) {
            LOG.info("创建或者回复评论失败");
            throw new BusinessException(ExceptionEnum.COMMENT__NOT_CREATE);
        }

        //创建者返回对象
        String staffUrl = webConfig.getRtrvStaffInfoUrl();
        String creatorId = createCommentInfoResponse.getCommentInfo().getCreatorId();
        Staff staff = restTemplate.postForObject(staffUrl, creatorId, Staff.class);
        createCommentInfoResponse.getCommentInfo().setCreatorInfo(staff);

        //被@的人返回对象
        if (request.getReplyId() != null) {
            String passiveAt = createCommentInfoResponse.getReplyDetail().getCreatorId();
            Staff passiveAtInfo = restTemplate.postForObject(staffUrl, passiveAt, Staff.class);
            createCommentInfoResponse.getReplyDetail().setCreatorInfo(passiveAtInfo);
        }

        /**
         * 消息推送(创建comment)
         */
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            ServerPush serverPush = new ServerPush();
            String initiatorId = request.getCreatorId();
            String subjectId = request.getSubjectId();
            Staff initiator = this.restTemplate.postForObject(webConfig.getRtrvStaffInfoUrl(), initiatorId, Staff.class);
            serverPush.setInitiator(initiator);
            serverPush.setSubjectType("comment");
            serverPush.setSubjectId(subjectId);
            serverPush.setType("at");
            //查询所评论的需求或者story的创建者
            String createId = rtrvCreatorId(subjectId);
            if (request.getPassiveAts().size() > 0) {//回复
                sendMessageRequest.setStaffIds(request.getPassiveAts());
            } else {//不@人
                List<String> staffIds = new ArrayList<>();
                staffIds.add(createId);
                sendMessageRequest.setStaffIds(staffIds);
            }
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setServerPushResponse(serverPush);
            Boolean flag = this.restTemplate.postForObject(webConfig.getSendMessageUrl(), sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送(创建comment)出现异常，异常信息：" + e);
        }
        restResponse.setResponseBody(createCommentInfoResponse);
        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
        restResponse.setResponseMsg("请求成功");
        restResponse.setResponseCode("000");
        LOG.info("结束执行{} createCommentInfo()方法.", this.CLASS);

        return restResponse;
    }

    /**
     * 点赞或踩
     * @param request
     * @return
     */
    @Override
    public RestResponse likeOrDislike(LikeCommentRequest request) {
        LOG.info("开始执行{} likeOrDislikeUrl()方法.", this.CLASS);
        CreateCommentInfoResponse createCommentInfoResponse = new CreateCommentInfoResponse();
        try {
            String remark = request.getRemark();
            String commentId = request.getCommentId();
            String creatorId = request.getCreatorId();
            Comment comment = this.commentRepository.findByCommentId(commentId);
            if (remark.equals("like")) {
                //对于点赞这边
                String likeIds = comment.getLikeIds();
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
                        comment.setLikeQty(comment.getLikeQty() + 1);
                        comment.setLikeIds(likeIds + "," + creatorId);
                    } else {
                        comment.setLikeQty(comment.getLikeQty() - 1);
                        comment.setLikeIds(likeIds);
                    }
                } else {
                    comment.setLikeQty(1);
                    comment.setLikeIds(creatorId);
                }
                //对于踩这边
                String dislikeIds = comment.getDislikeIds();
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
                        comment.setDislikeQty(comment.getDislikeQty() - 1);
                        comment.setDislikeIds(dislikeIds);
                    }
                }
            }
            if (remark.equals("dislike")) {
                //对于踩这边
                String dislikeIds = comment.getDislikeIds();
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
                        comment.setDislikeQty(comment.getDislikeQty() + 1);
                        comment.setDislikeIds(dislikeIds + "," + creatorId);
                    } else {
                        comment.setDislikeQty(comment.getDislikeQty() - 1);
                        comment.setDislikeIds(dislikeIds);
                    }
                } else {
                    comment.setDislikeQty(1);
                    comment.setDislikeIds(creatorId);
                }
                //对于点赞这边
                String likeIds = comment.getLikeIds();
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
                        comment.setLikeQty(comment.getLikeQty() - 1);
                        comment.setLikeIds(likeIds);
                    }
                }
            }
            comment = this.commentRepository.saveAndFlush(comment);
            //赋值·
            CommentInfo commentInfo = new CommentInfo();
            BeanUtils.copyProperties(comment, commentInfo);
            commentInfo.setCreateTime(comment.getCreateTime().getTime());
            createCommentInfoResponse.setCommentInfo(commentInfo);
            if (!StringUtils.isEmpty(comment.getReplyId())) {
                Comment comm = this.commentRepository.findByCommentId(comment.getReplyId());
                //赋值·
                CommentInfo replyDetail = new CommentInfo();
                BeanUtils.copyProperties(comm, replyDetail);
                replyDetail.setCreateTime(comm.getCreateTime().getTime());
                createCommentInfoResponse.setReplyDetail(replyDetail);
            }
        } catch (Exception ex) {
            LOG.info("点赞或者踩评论失败");
            throw new BusinessException(ExceptionEnum.COMMENT__LIKEORDISLIKE_FAILD);
        }
        //创建者返回对象
        String staffUrl = webConfig.getRtrvStaffInfoUrl();
        String creatorId = createCommentInfoResponse.getCommentInfo().getCreatorId();
        Staff staff = restTemplate.postForObject(staffUrl, creatorId, Staff.class);
        createCommentInfoResponse.getCommentInfo().setCreatorInfo(staff);
        //被@的人返回对象
        String replyId = createCommentInfoResponse.getCommentInfo().getReplyId();
        if (!StringUtils.isEmpty(replyId)) {
            String passiveAt = createCommentInfoResponse.getReplyDetail().getCreatorId();
            Staff passiveAtInfo = restTemplate.postForObject(staffUrl, passiveAt, Staff.class);
            createCommentInfoResponse.getReplyDetail().setCreatorInfo(passiveAtInfo);
        }
        restResponse.setResponseBody(createCommentInfoResponse);
        restResponse.setStatusCode(String.valueOf(HttpStatus.OK));
        restResponse.setResponseMsg("请求成功");
        restResponse.setResponseCode("000");
        LOG.info("结束执行{} likeOrDislike()方法.", this.CLASS);
        return restResponse;
    }

    /**
     * 获取每个reqmnt或者story的评论（获取列表时需要）
     *
     * @param request
     * @return
     */
    @Override
    public List<CommentDetail> rtrvCommentInfos(RtrvCommentInfosRequest request) {
        LOG.info("开始执行{} rtrvCommentInfos()方法.", this.CLASS);
        List<CommentDetail> commentDetails = new ArrayList<>();
        String projId = request.getProjId();
        String subjectId = request.getSubjectId();
        List<Comment> comments = this.commentRepository.findByProjIdAndSubjectIdAndIsDeleted(projId, subjectId, 0);
        for (int i = 0; i < comments.size(); i++) {
            CommentDetail commentDetail = new CommentDetail();
            Comment comment = comments.get(i);
            commentDetail.setComment(comment);
            String replyId = comment.getReplyId();
            if (!StringUtils.isEmpty(replyId)) {
                Comment comm = this.commentRepository.findByCommentId(replyId);
                commentDetail.setReplyDetail(comm);
            }
            commentDetails.add(commentDetail);
        }
        LOG.info("结束执行{} rtrvCommentInfos()方法.", this.CLASS);
        return commentDetails;
    }

    @Override
    public String rtrvCreatorId(String subjectId) {
        String createId = this.commentRepository.findCreateId(subjectId);
        return createId;
    }

    @Override
    public Comment rtrvCommentDetailInfo(String commentId) {
        Comment comm = this.commentRepository.findByCommentId(commentId);
        return comm;
    }
}

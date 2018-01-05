package com.mdvns.mdvn.mdvncomment.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RtrvCommentInfosRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.CommentDetail;
import com.mdvns.mdvn.common.bean.model.CommentInfo;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnStringUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.ServerPushUtil;
import com.mdvns.mdvn.mdvncomment.config.WebConfig;
import com.mdvns.mdvn.mdvncomment.domain.*;
import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;
import com.mdvns.mdvn.mdvncomment.repository.CommentRepository;
import com.mdvns.mdvn.mdvncomment.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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

    /*注入WebConfig*/
    @Resource
    private WebConfig webConfig;

    @Override
    @Transactional
    public RestResponse<?> createCommentInfo(CreateCommentInfoRequest request) throws BusinessException {
        LOG.info("开始执行{} createCommentInfo()方法.", this.CLASS);
        if (request == null || request.getProjId() == null || request.getSubjectId() == null || request.getCreatorId() == null) {
            throw new NullPointerException("createCommentInfo 或项目Id/subjectId/登录者Id不能为空");
        }
        CreateCommentInfoResponse createCommentInfoResponse = new CreateCommentInfoResponse();
        //如果是回复的话，把回复的人加入到passiveAts中
        String commentId = request.getReplyId();
        if (request.getReplyId() != null) {
            Comment replyComm = rtrvCommentDetailInfo(commentId);
            List<Long> passiveAts = request.getPassiveAts();
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
            Long creatorId = request.getCreatorId();
            String content = request.getContent();
            comment.setCreateTime(currentTime);
            comment.setProjId(projId);
            comment.setSubjectId(subjectId);
            comment.setCreatorId(creatorId);
            comment.setContent(content);
            comment.setLikeQty(0);
            comment.setDislikeQty(0);
            comment.setIsDeleted(0);
            List<Long> passiveAtList = request.getPassiveAts();
            if (passiveAtList != null && passiveAtList.size() > 0) {
                String passiveAts = MdvnStringUtil.joinLong(passiveAtList, ",");
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
            throw new BusinessException(ErrorEnum.COMMENT_CREATE_FAILD, "创建或者回复评论失败");
        }

        //创建者返回对象
        Long creatorId = createCommentInfoResponse.getCommentInfo().getCreatorId();
        Staff staffInfo = this.rtrvStaffInfoById(creatorId);
        createCommentInfoResponse.getCommentInfo().setCreatorInfo(staffInfo);

        //被@的人返回对象
        if (request.getReplyId() != null) {
            Long passiveAt = createCommentInfoResponse.getReplyDetail().getCreatorId();
            createCommentInfoResponse.getReplyDetail().setCreatorInfo(this.rtrvStaffInfoById(passiveAt));
        }

        /**
         * 消息推送(创建comment)
         */
        this.serverPushByCreate(request);
        LOG.info("结束执行{} createCommentInfo()方法.", this.CLASS);

        return RestResponseUtil.success(createCommentInfoResponse);
    }

    /**
     * 点赞或踩
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse likeOrDislike(LikeCommentRequest request) throws BusinessException {
        LOG.info("开始执行{} likeOrDislikeUrl()方法.", this.CLASS);
        CreateCommentInfoResponse createCommentInfoResponse = new CreateCommentInfoResponse();
        try {
            String remark = request.getRemark();
            String commentId = request.getCommentId();
            String creatorId = String.valueOf(request.getCreatorId());
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
                    comment.setLikeIds(String.valueOf(creatorId));
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
                    comment.setDislikeIds(String.valueOf(creatorId));
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
            if (!StringUtils.isEmpty(comment.getLikeIds())) {
                commentInfo.setLikeIdList(MdvnStringUtil.stringToList(comment.getLikeIds()));
            }
            if (!StringUtils.isEmpty(comment.getDislikeIds())) {
                commentInfo.setDislikeIdList(MdvnStringUtil.stringToList(comment.getDislikeIds()));
            }
            createCommentInfoResponse.setCommentInfo(commentInfo);
            if (!StringUtils.isEmpty(comment.getReplyId())) {
                Comment comm = this.commentRepository.findByCommentId(comment.getReplyId());
                //赋值·
                CommentInfo replyDetail = new CommentInfo();
                BeanUtils.copyProperties(comm, replyDetail);
                replyDetail.setCreateTime(comm.getCreateTime().getTime());
                if (!StringUtils.isEmpty(comm.getLikeIds())) {
                    replyDetail.setLikeIdList(MdvnStringUtil.stringToList(comm.getLikeIds()));
                }
                if (!StringUtils.isEmpty(comm.getDislikeIds())) {
                    replyDetail.setDislikeIdList(MdvnStringUtil.stringToList(comm.getDislikeIds()));
                }
                createCommentInfoResponse.setReplyDetail(replyDetail);
            }
        } catch (Exception ex) {
            LOG.info("点赞或者踩评论失败");
            throw new BusinessException(ErrorEnum.LIKEORDISLIKE_COMMENT_FAILD, "点赞或者踩评论失败");
        }
        //创建者返回对象
        Long creatorId = createCommentInfoResponse.getCommentInfo().getCreatorId();
        createCommentInfoResponse.getCommentInfo().setCreatorInfo(this.rtrvStaffInfoById(creatorId));
        //被@的人返回对象
        String replyId = createCommentInfoResponse.getCommentInfo().getReplyId();
        if (!StringUtils.isEmpty(replyId)) {
            Long passiveAt = createCommentInfoResponse.getReplyDetail().getCreatorId();
            createCommentInfoResponse.getReplyDetail().setCreatorInfo(this.rtrvStaffInfoById(passiveAt));
        }
        LOG.info("结束执行{} likeOrDislike()方法.", this.CLASS);
        return RestResponseUtil.success(createCommentInfoResponse);
    }

    /**
     * 获取每个reqmnt或者story的评论（获取列表时需要）
     *
     * @param request
     * @return
     */
    @Override
    public List<CommentDetail> rtrvCommentInfos(RtrvCommentInfosRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvCommentInfos()方法.", this.CLASS);
        List<CommentDetail> commentDetails = new ArrayList<>();
        String projId = request.getProjId();
        String subjectId = request.getSubjectId();
        try {
            List<Comment> comments = this.commentRepository.findByProjIdAndSubjectIdAndIsDeleted(projId, subjectId, 0);
            for (int i = 0; i < comments.size(); i++) {
                CommentDetail commentDetail = new CommentDetail();
                Comment comment = comments.get(i);
                CommentInfo commentInfo = new CommentInfo();
                BeanUtils.copyProperties(comment, commentInfo);
                commentInfo.setCreateTime(comment.getCreateTime().getTime());
                if (!StringUtils.isEmpty(comment.getLikeIds())) {
                    commentInfo.setLikeIdList(MdvnStringUtil.stringToList(comment.getLikeIds()));
                }
                if (!StringUtils.isEmpty(comment.getDislikeIds())){
                    commentInfo.setDislikeIdList(MdvnStringUtil.stringToList(comment.getDislikeIds()));
                }
                commentDetail.setCommentInfo(commentInfo);
                String replyId = comment.getReplyId();
                if (!StringUtils.isEmpty(replyId)) {
                    Comment comm = this.commentRepository.findByCommentId(replyId);
                    //赋值·
                    CommentInfo replyDetail = new CommentInfo();
                    BeanUtils.copyProperties(comm, replyDetail);
                    replyDetail.setCreateTime(comm.getCreateTime().getTime());
                    if (!StringUtils.isEmpty(comm.getLikeIds())) {
                        replyDetail.setLikeIdList(MdvnStringUtil.stringToList(comm.getLikeIds()));
                    }
                    if (!StringUtils.isEmpty(comm.getDislikeIds())) {
                        replyDetail.setDislikeIdList(MdvnStringUtil.stringToList(comm.getDislikeIds()));
                    }
                    commentDetail.setReplyDetail(replyDetail);
                }
                commentDetails.add(commentDetail);
            }
        } catch (Exception ex) {
            LOG.info("获取评论信息失败");
            throw new BusinessException(ErrorEnum.RTRV_COMMENTINFO_FAILD, "获取评论信息失败");
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

    /**
     * 创建comment的消息推送
     *
     * @param request
     * @throws BusinessException
     */
    private void serverPushByCreate(CreateCommentInfoRequest request) throws BusinessException {
        try {
            Long initiatorId = request.getCreatorId();
            String serialNo = request.getSubjectId();
            String subjectType = "comment";
            String type = "at";
            //查找需要推送的人，有@就推送给@的人，无@推送给创建者
            List<Long> passiveAts = request.getPassiveAts();
            //查询所评论的需求或者story的创建者
            List<Long> staffIds = new ArrayList<>();
            String createId = rtrvCreatorId(serialNo);
            if (null != passiveAts && passiveAts.size() > 0) {//回复
                staffIds = passiveAts;
            } else {//不@人
                staffIds.add(Long.valueOf(createId));
            }
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("创建comment，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(创建comment)出现异常，异常信息：" + e);
        }
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
        String retrieveByIdUrl = webConfig.getRetrieveByIdUrl();
        SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
        singleCriterionRequest.setCriterion(String.valueOf(id));
        singleCriterionRequest.setStaffId(id);
        ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
        };
        ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
        RestResponse<Staff> restResponse = responseEntity.getBody();
        return restResponse.getData();
    }

}

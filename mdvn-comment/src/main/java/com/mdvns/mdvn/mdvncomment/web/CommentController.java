package com.mdvns.mdvn.mdvncomment.web;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RtrvCommentInfosRequest;
import com.mdvns.mdvn.common.bean.model.CommentDetail;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvncomment.domain.*;
import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;
import com.mdvns.mdvn.mdvncomment.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 评论控制层
 */
@CrossOrigin
@RestController
@RequestMapping(value = {"/comments", "/v1.0/comments"})
public class CommentController {

    private Logger LOG = LoggerFactory.getLogger(CommentController.class);
    private final String CLASS = this.getClass().getName();

    /*评论service注入*/
    @Autowired
    private CommentService commentService;


    /**
     * 创建评论
     * @param request
     * @return
     */
    @PostMapping(value = "/createCommentInfo")
    public RestResponse<?> createCommentInfo(@RequestBody CreateCommentInfoRequest request) throws BusinessException {
        LOG.info("开始执行{} createCommentInfo()方法.", this.CLASS);
        return this.commentService.createCommentInfo(request);
    }

    /**
     * 获取每个reqmnt或者story的评论（获取列表时需要）
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvCommentInfos")
    public List<CommentDetail> rtrvCommentInfos(@RequestBody RtrvCommentInfosRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvCommentInfos()方法.", this.CLASS);
        return this.commentService.rtrvCommentInfos(request);
    }

    /**
     * 点赞或踩
     * @param request
     * @return
     */
    @PostMapping(value = "/likeOrDislike")
    public RestResponse<?> likeOrDislike(@RequestBody LikeCommentRequest request) throws BusinessException {
        LOG.info("开始执行{} likeComment()方法.", this.CLASS);
        return this.commentService.likeOrDislike(request);
    }

    /**
     * 查询所评论的需求或者story的创建者（消息推送时用到）
     * @param subjectId
     * @return
     */
    @PostMapping(value = "/rtrvCreatorId")
    public String rtrvCreatorId(@RequestBody String subjectId) {
        LOG.info("开始执行{} rtrvCreatorId()方法.", this.CLASS);
        return this.commentService.rtrvCreatorId(subjectId);
    }

    /**
     * 查询某个评论的信息
     * @param commentId
     * @return
     */
    @PostMapping(value = "/rtrvCommentDetailInfo")
    public Comment rtrvCommentDetailInfo(@RequestBody String commentId) {
        LOG.info("开始执行{} rtrvCommentDetailInfo()方法.", this.CLASS);
        return this.commentService.rtrvCommentDetailInfo(commentId);
    }
}

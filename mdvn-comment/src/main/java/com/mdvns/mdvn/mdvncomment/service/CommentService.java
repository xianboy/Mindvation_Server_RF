package com.mdvns.mdvn.mdvncomment.service;



import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RtrvCommentInfosRequest;
import com.mdvns.mdvn.common.bean.model.CommentDetail;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvncomment.domain.*;
import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;

import java.util.List;

/**
 * 评论接口
 */

public interface CommentService {

    RestResponse<?> createCommentInfo(CreateCommentInfoRequest request) throws BusinessException;

    RestResponse<?> likeOrDislike(LikeCommentRequest request) throws BusinessException;

    List<CommentDetail> rtrvCommentInfos(RtrvCommentInfosRequest request) throws BusinessException;

    String rtrvCreatorId(String subjectId);

    Comment rtrvCommentDetailInfo(String commentId);
}

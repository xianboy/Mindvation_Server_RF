package com.mdvns.mdvn.mdvncomment.service;



import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.mdvncomment.domain.*;
import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;

import java.util.List;

/**
 * 评论接口
 */

public interface CommentService {

    RestResponse<?> createCommentInfo(CreateCommentInfoRequest request);

    RestResponse<?> likeOrDislike(LikeCommentRequest request);

    List<CommentDetail> rtrvCommentInfos(RtrvCommentInfosRequest request);

    String rtrvCreatorId(String subjectId);

    Comment rtrvCommentDetailInfo(String commentId);
}

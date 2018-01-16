package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RtrvCommentInfosRequest;
import com.mdvns.mdvn.common.bean.model.CommentDetail;
import com.mdvns.mdvn.common.bean.model.Staff;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CommentUtil {
    /**
     * 返回需求或者STORY的评论list
     * @param projSerialNo
     * @param serialNo
     * @param rCommentInfosUrl
     * @param rtrvStaffInfoByIdUrl
     * @return
     */
    public static List<CommentDetail> rtrvCommentInfos(String projSerialNo,String serialNo,String rCommentInfosUrl,String rtrvStaffInfoByIdUrl) {

        RtrvCommentInfosRequest rtrvCommentInfosRequest = new RtrvCommentInfosRequest();
        rtrvCommentInfosRequest.setProjId(projSerialNo);
        rtrvCommentInfosRequest.setSubjectId(serialNo);
        //实例化restTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference trReference = new ParameterizedTypeReference<List<CommentDetail>>() {
        };
        List<CommentDetail> comDetails = FetchListUtil.fetch(restTemplate, rCommentInfosUrl, rtrvCommentInfosRequest, trReference);
        for (int j = 0; j < comDetails.size(); j++) {
            //创建者返回对象
            Long creatorId = comDetails.get(j).getCommentInfo().getCreatorId();
            Staff staff = StaffUtil.rtrvStaffInfoById(creatorId, rtrvStaffInfoByIdUrl);
            comDetails.get(j).getCommentInfo().setCreatorInfo(staff);
            //被@的人返回对象
            if (comDetails.get(j).getCommentInfo().getReplyId() != null) {
                Long passiveAt = comDetails.get(j).getReplyDetail().getCreatorId();
                Staff passiveAtInfo = StaffUtil.rtrvStaffInfoById(passiveAt, rtrvStaffInfoByIdUrl);
                comDetails.get(j).getReplyDetail().setCreatorInfo(passiveAtInfo);
            }
        }
        return comDetails;
    }
}

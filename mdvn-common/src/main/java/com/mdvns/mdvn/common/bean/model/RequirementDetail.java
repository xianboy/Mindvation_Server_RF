package com.mdvns.mdvn.common.bean.model;

import com.mdvns.mdvn.common.bean.PageableResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RequirementDetail implements Serializable {

    private Long id;

    /*上层主体编号*/
    private String hostSerialNo;

    /*编号*/
    private String serialNo;

    /*状态*/
    private String status;

    /*进度*/
    private Double progress;

    /*概要*/
    private String summary;

    /*描述*/
    private String description;

    /*标签*/
    private List<TerseInfo> tags;

    /*优先级*/
    private Integer priority;

    /*模板id*/
    private Long templateId;

    /*过程方法*/
    private TerseInfo label;

    /*需求创建者对象信息*/
    private Staff creatorInfo;

    /*成员*/
    private List<RoleMember> roleMembers;

    /*开始结束时间*/
    private Long startDate, endDate;

    /*当前需求下所有story的story point 总和*/
    private Double storyPointAmount;

    /*其下的Story列表*/
    private PageableResponse<Story> stories;

    /*附件列表*/
    private List<AttchInfo> attchInfos;

    /*评论列表*/
    private List<CommentDetail> commentDetails;
    /*权限信息*/
    private List<StaffAuthInfo> staffAuthInfo;


    /**
     * 项目层级结构类型
     * possible values are : 2, 3, 4
     *
     * Explanation
     * 2 : Project -> Task
     * 3 : Project -> Requirement -> Task
     * 4 : Project -> Requirement -> Story -> Task
     */
    private Integer layerType;

}

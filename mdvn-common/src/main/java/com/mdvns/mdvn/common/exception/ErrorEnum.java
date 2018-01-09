package com.mdvns.mdvn.common.exception;

import lombok.*;

/**
 * 异常枚举类，定义异常信息
 * system:555~1000
 * department:30000~30100
 * staff:30100~30200
 * tag:30200~30300
 * file:30300~30400
 * template:30400~30500
 * project:30500~30600
 * requirement:30600~30700
 * story:30700~30800
 * task:30800~30900
 * attach:30900~31000
 * websocket:31100~31200
 * comment: 31200~31300
 * reward: 31300~31400
 */
public enum ErrorEnum {

    //sys error
    /*参数错误*/
    ILLEGAL_ARG("555"),
    /*更新请求和原数据相同, 请输入有效的更新数据*/
    THE_SAME_DATA("556"),
    /*数据不存在*/
    NOT_EXISTS("557"),

    /*数据已存在*/
    EXISTED("558"),
    /*获取id和name失败*/
    GET_BASE_INFO_FAILED("559"),

    //department
    DEPT_NOT_EXISTS("30000"),
    DEPT_EXISTS("30001"),


    //staff
    /*用户不存在*/
    STAFF_NOT_EXISTS("30100"),
    RTRV_AUTH_FAILED("30197"),
    REMOVE_AUTH_FAILED("30198"),
    ASSIGN_AUTH_FAILED("30199"),


    //tag
    /*标签不存在*/
    TAG_NOT_EXISTS("30200"),
    RETRIEVE_HOT_TAGS_FAILED("30201"),


    //template
    /*新建模板时, 子过程方法不能为空*/
    TEMPLATE_SYSTEM_ERROR("30499"),
    RETRIEVE_LABEL_FAILED("30498"),
    RETRIEVE_ROLEMEMBER_FAILED("30497"),
    //子过程方法为空
    SUB_LABEL_IS_NULL("30400"),

    /*自定义过程方法失败*/
    CUSTOM_LABEL_FAILED("30401"),

    /*模板角色不存在*/
    TEMPLATE_ROLE_NOT_EXISTS("30402"),

    /*过程方法不存在*/
    FUNCTION_LABEL_NOT_EXISTS("30403"),

    /*模板不存在*/
    TEMPLATE_NOT_EXISTS("30404"),
    DELIVERY_NOT_EXIST("30405"),

    //requirement
    /*需求对象不存在*/
    REQUIREMENT_NOT_EXISTS("30600"),

    //story
    /*story不存在*/
    STORY_NOT_EXISTS("30700"),

    //task
    /*task不存在*/
    TASK_NOT_EXISTS("30800"),

    //attach
    /*获取附件列表信息失败*/
    ATTACHES_RTRV_FAILED("30900"),
    ATTACHES_UPDATE_FAILED("30901"),
    ATTACHES_CREATE_FAILED("30902"),

    //websocket
    SERVER_PUSH_FAILED("31100"),

    //comment
    COMMENT_CREATE_FAILED("31200"),
    LIKEORDISLIKE_COMMENT_FAILED("31201"),
    RTRV_COMMENTINFO_FAILED("31202"),

    //reward
    REWARD_HAS_UNVEILED("31300"),
    ;


    private String code;

//    private String msg;

    ErrorEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }



}

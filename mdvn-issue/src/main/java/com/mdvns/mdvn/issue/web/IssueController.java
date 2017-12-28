package com.mdvns.mdvn.issue.web;


import com.mdvns.mdvn.common.beans.RestResponse;
import com.mdvns.mdvn.issue.domain.*;
import com.mdvns.mdvn.issue.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 评论控制层
 */
@CrossOrigin
@RestController
@RequestMapping(value = {"/issues", "/v1.0/issues"})
public class IssueController {

    private Logger LOG = LoggerFactory.getLogger(IssueController.class);
    private final String CLASS = this.getClass().getName();

    /*评论service注入*/
    @Autowired
    private IssueService issueService;


    /**
     * 发布求助
     * @param request
     * @return
     */
    @PostMapping(value = "/createIssueInfo")
    public RestResponse createIssueInfo(@RequestBody CreateIssueRequest request) {
        LOG.info("开始执行{} createIssueInfo()方法.", this.CLASS);
        return this.issueService.createIssueInfo(request);
    }
    /**
     * 创建answer
     * @param request
     * @return
     */
    @PostMapping(value = "/createIssueAnswerInfo")
    public RestResponse createIssueAnswerInfo(@RequestBody CreateIssueAnswerRequest request) {
        LOG.info("开始执行{} createIssueAnswerInfo()方法.", this.CLASS);
        return this.issueService.createIssueAnswerInfo(request);
    }

    /**
     * 获取某个reqmnt或者story的单个求助信息（同时返回该reqmnt或者story下的其他求助信息的key：id,value:是否解决）
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvIssueDetail")
    public RestResponse rtrvIssueDetail(@RequestBody RtrvIssueDetailRequest request) {
        LOG.info("开始执行{} rtrvIssueDetail()方法.", this.CLASS);
        return this.issueService.rtrvIssueDetail(request);
    }

    /**
     * 点赞answer
     * @param request
     * @return
     */
    @PostMapping(value = "/likeOrDislikeAnswer")
    public RestResponse likeOrDislikeAnswer(@RequestBody LikeOrDislikeAnswerRequest request) {
        LOG.info("开始执行{} likeOrDislikeAnswer()方法.", this.CLASS);
        return this.issueService.likeOrDislikeAnswer(request);
    }

    /**
     * 采纳（answer中isAdopt变为1，issue中isResolved变为1）
     */
    @PostMapping(value = "/adoptAnswer")
    public RestResponse adoptAnswer(@RequestBody adoptAnswerRequest request) {
        LOG.info("开始执行{} adoptAnswer()方法.", this.CLASS);
        return this.issueService.adoptAnswer(request);
    }



}

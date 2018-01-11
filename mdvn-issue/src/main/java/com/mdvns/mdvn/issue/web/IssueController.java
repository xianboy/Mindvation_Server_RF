package com.mdvns.mdvn.issue.web;


import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.issue.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * issue控制层
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
     * 获取悬赏榜信息列表(解决/未解决)
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvIssueList")
    public RestResponse<?> rtrvIssueList(@RequestBody SingleCriterionRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvIssueList()方法.", this.CLASS);
        return this.issueService.rtrvIssueList(request);
    }


}

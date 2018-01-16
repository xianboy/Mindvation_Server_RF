package com.mdvns.mdvn.tag.web;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.tag.domain.entity.Tag;
import com.mdvns.mdvn.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@CrossOrigin
@RestController
@RequestMapping(value = {"/tags", "/v1.0/tags"})
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 新建tag
     * @param tag tag
     * @return RestResponse
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated Tag tag) throws BusinessException {
        return this.tagService.create(tag);
    }

    /**
     * 查询tag列表:支持分页
     * @param pageableQueryWithoutArgRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveAll")
    public RestResponse<?> retrieveAll(@RequestBody @Validated PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) {
        return this.tagService.retrieveAll(pageableQueryWithoutArgRequest);
    }

    /**
     * 获取指定name的部门详情
     * @param retrieveDetailRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveByName")
    public RestResponse<?> retrieveDetailByName(@RequestBody @Validated SingleCriterionRequest retrieveDetailRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.tagService.retrieveDetailByName(retrieveDetailRequest);
    }

    /**
     * 获取id、serialNo和name
     * @param retrieveTerseInfoRequest request
     * @param bindingResult bindingResult
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveTerseInfo")
    public RestResponse<?> retrieveTerseInfo(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.tagService.retrieveBaseInfo(retrieveTerseInfoRequest);
    }

    /**
     * 根据id集合获取tag对象集合
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @PostMapping(value = "/retrieveTagInfos")
    public RestResponse<?> retrieveTagInfos(@RequestBody @Validated RetrieveTerseInfoRequest retrieveTerseInfoRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.tagService.retrieveTagInfos(retrieveTerseInfoRequest);
    }

    /**
     * 根据Id查询tag
     * @param tagId
     * @return
     */
    @PostMapping(value = "/{tagId}/tag")
    public ResponseEntity<?> findById(@PathVariable Long tagId) {
        return this.tagService.findById(tagId);
    }

    /**
     * 查询一周内热门标签数据：支持分页
     * @param request
     * @return
     */
    @PostMapping(value = "/retrieveHotTagList")
    public RestResponse<?> retrieveHotTagList(@RequestBody SingleCriterionRequest request) throws BusinessException {
        return this.tagService.retrieveHotTagList(request);
    }
}

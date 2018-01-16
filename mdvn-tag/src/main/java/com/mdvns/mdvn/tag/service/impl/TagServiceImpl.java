package com.mdvns.mdvn.tag.service.impl;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.ConvertObjectUtil;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.PageableQueryUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.tag.domain.entity.Tag;
import com.mdvns.mdvn.tag.repository.TagRepository;
import com.mdvns.mdvn.tag.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Resource
    private TagRepository tagRepository;

    /**
     * 新建tag
     * @param tag 新建数据
     * @return  新建成功的tag
     * @throws BusinessException tag name已存在
     */
    @Transactional
    public RestResponse<?> create(Tag tag) throws BusinessException {
        //将名称包含的所有空格去掉
        String name = MdvnCommonUtil.trimAllSpace(tag.getName());
        //根据name查询
        Tag tg = this.tagRepository.findByName(name);
        //如果给定name的数据已存在,抛出异常
        MdvnCommonUtil.existingError(tg, "name", name);
        //设置name为去除空格后的name
        tag.setName(name);
        //设置编号
        tag.setSerialNo(buildSerialNo());
        //保存tag
        tg = this.tagRepository.saveAndFlush(tag);
        //构建response
        return RestResponseUtil.success(tg);
    }

    /**
     * 查询所有数据：支持分页
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) {
        //获取分页参数对象
        PageableCriteria pageableCriteria = pageableQueryWithoutArgRequest.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", pageableQueryWithoutArgRequest.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Tag> deptPage = this.tagRepository.findAll(pageRequest);
        //返回结果
        return RestResponseUtil.success(deptPage);
    }

    /**
     * 获取指定name的部门详情
     * @param retrieveDetailRequest request
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveDetailByName(SingleCriterionRequest retrieveDetailRequest) throws BusinessException {
        //获取request中的name
        String name = MdvnCommonUtil.trimAllSpace(retrieveDetailRequest.getCriterion());
        //查询name包含指定字符串的所有tag
        List<Tag> tags = this.tagRepository.findByNameContains(name);
        //如果查询数据不存在，返回空数组
        if (tags.isEmpty()) {
            tags = new ArrayList<>();
        }
        //返回结果
        return RestResponseUtil.success(tags);
    }

    /**
     * 获取指定id集合的标签基本信息
     * @param retrieveTerseInfoRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveBaseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest) {
        //根据request获取id集合
        List<Long> ids = retrieveTerseInfoRequest.getIds();
        List<Object[]> resultSet = this.tagRepository.findTerseInfoById(ids);
        List<TerseInfo> tags = ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet);
        return RestResponseUtil.success(tags);
    }

    /**
     * 构建编号
     * @return String
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.tagRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.TG + maxId;
    }

    /**
     * 获取指定Id的标签
     *
     * @param tagId
     * @return
     */
    @Override
    public ResponseEntity<?> findById(Long tagId) {
        Tag tag = this.tagRepository.findOne(tagId);
        return ResponseEntity.ok(tag);
    }

    /**
     * 查询一周内热门标签数据：支持分页(悬赏榜/求助)
     *
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveHotTagList(SingleCriterionRequest request) throws BusinessException {
        //获取分页参数对象
        PageableCriteria pageableCriteria = request.getPageableCriteria();
        //获取查询条件（悬赏、求助）
        String criterion = request.getCriterion();
        //创建pageableResponse对象
        PageableResponse pageableResponse = new PageableResponse();
        //判断有无分页参数
        List<Tag> tags = new ArrayList();
        try {
            if (null == pageableCriteria) {
                LOG.info("用户[{}]没有填写分页参数，故查标签这里不分页.", request.getStaffId());
                tags = this.tagRepository.findHotTagListInfo();
                pageableResponse.setTotalElements((long) tags.size());
            } else {
                Integer page = pageableCriteria.getPage() - MdvnConstant.ONE;
                Integer pageSize = pageableCriteria.getSize();
                Integer m = page * pageSize;
                Integer n = pageSize;
                if (criterion.equals("reward")) {
                    tags = this.tagRepository.findRewardHotTagsHavePageable(m, n);
                }
                if (criterion.equals("issue")) {
                    tags = this.tagRepository.findIssueHotTagsHavePageable(m, n);
                }
                pageableResponse.setNumber(page);
                pageableResponse.setNumberOfElements(tags.size());
                pageableResponse.setSize(pageSize);
                //返回总条数
                List tagsTotals = this.tagRepository.findHotTagListInfo();
                pageableResponse.setTotalElements((long) tagsTotals.size());
            }
        } catch (Exception ex) {
            LOG.info("查询一周内热门标签数据失败");
            throw new BusinessException(ErrorEnum.RETRIEVE_HOT_TAGS_FAILED, "查询一周内热门标签数据失败");
        }
        //分页查询
        pageableResponse.setContent(tags);
        //返回结果
        return RestResponseUtil.success(pageableResponse);
    }
>>>>>>> parent of d3bc18c... update staff and tag
}

package com.mdvns.mdvn.staff.service.impl;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.StaffTagScore;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.staff.config.WebConfig;
import com.mdvns.mdvn.staff.domain.RetrieveStaffRequest;
import com.mdvns.mdvn.staff.domain.RtrvStaffListByNameResponse;
import com.mdvns.mdvn.staff.domain.StaffDetail;
import com.mdvns.mdvn.staff.domain.StaffMatched;
import com.mdvns.mdvn.staff.domain.entity.Staff;
import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import com.mdvns.mdvn.staff.repository.StaffRepository;
import com.mdvns.mdvn.staff.repository.StaffTagRepository;
import com.mdvns.mdvn.staff.service.RetrieveService;
import com.mdvns.mdvn.staff.service.StaffTagService;
import com.mdvns.mdvn.staff.util.StaffUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);

    @Resource
    private StaffRepository staffRepository;

    @Resource
    private StaffTagRepository tagRepository;

    @Resource
    private WebConfig webConfig;

    @Autowired
    private StaffTagService staffTagService;


    /**
     * 获取指定id的staff详情
     *
     * @param singleCriterionRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveDetailById(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        //根据request获取id
        Long id = Long.valueOf(singleCriterionRequest.getCriterion());
        //根据id查询
        Staff staff = this.staffRepository.findOne(id);
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(staff, "id", singleCriterionRequest.getCriterion());
        //标签对象
        StaffDetail staffDetail = StaffUtil.buildDetailByStaff(staff);
        staffDetail.setTags(this.staffTagService.getStaffTagInfo(staffDetail.getId()));
        //返回结果
        return RestResponseUtil.success(staffDetail);
    }

    /**
     * 获取staff列表
     *
     * @param pageableQueryWithoutArgRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) throws BusinessException {
        //获取分页参数对象
        PageableCriteria pageableCriteria = pageableQueryWithoutArgRequest.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Staff> staffPage = this.staffRepository.findAll(pageRequest);
        List<Staff> staffList = staffPage.getContent();
        for (int i = 0; i < staffList.size(); i++) {
            staffList.get(i).setTags(this.staffTagService.getStaffTagInfo(staffList.get(i).getId()));
        }
        //返回结果
        return RestResponseUtil.success(staffPage);
    }

    /**
     * 获取指定id集合的id和name
     *
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveTerseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest) {
        //根据request获取id集合
        List<Long> ids = retrieveTerseInfoRequest.getIds();
        List<Object[]> resultSet = this.staffRepository.findTerseInfoById(ids);
        List<TerseInfo> tags = ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet);
        LOG.info("获取指定id集合的id和name完成：{}", tags.toString());
        return RestResponseUtil.success(tags);
    }

    /**
     * 根据id集合获取staff对象集合
     *
     * @param retrieveTerseInfoRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveStaffInfos(RetrieveTerseInfoRequest retrieveTerseInfoRequest) {
        //根据request获取id集合
        List<Long> ids = retrieveTerseInfoRequest.getIds();
        List<Staff> resultSet = this.staffRepository.findStaffInfosById(ids);
        LOG.info("根据id集合获取staff对象完成：{}", resultSet.toString());
        return RestResponseUtil.success(resultSet);
    }

    /**
     * 根据name查人
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveByName(SingleCriterionRequest retrieveRequest) {
        //如果没有指定isDeleted,默认为0
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        //查询name中包含指定字符串的人
        List<Staff> list = this.staffRepository.findDistinctByNameContainingAndIsDeleted(retrieveRequest.getCriterion(), isDeleted);
        return RestResponseUtil.success(list);
    }

    /**
     * 获取指定name的staff的详细信息，如果结果多于10条，只返回10条
     * 1. 如果name不为空，查询所有名字以name开头的staff,并获取其标签详情
     * 2. 如果name为空，就查询拥有标签集中标签最多的staff，以及标签详情
     * 3. 如果name 和 tags都为空，抛出请求参数不正确 异常
     *
     * @param request request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveByNameOrTags(RetrieveStaffRequest request) throws BusinessException {
        //name以startingStr开头
        String startingStr = request.getName();
        List<Long> tags = request.getTags();
        Integer isDeleted = (null==request.getIsDeleted())?MdvnConstant.ZERO:request.getIsDeleted();
        //实例化response
        RtrvStaffListByNameResponse rtrvStaffListByNameResponse = new RtrvStaffListByNameResponse();
        //如果startingStr为空，则按标签获取Staff
        if (StringUtils.isEmpty(startingStr)) {
            //1.查出拥有tags中任意一种标签的所有人
            List<StaffTag> staffList = rtrvStaffBytags(tags, isDeleted);
            //2.对staffId去重
            List<Long> idList = new ArrayList<>(distinctStaffId(staffList));
            //3.获取拥有指定标签集中标签最多的用户，按匹配度最多获取前十
            List<StaffTagScore> stsList = rtrvStaffTagScore(staffList, tags, idList);
            //4.根据StaffTagScore集合获取每员工的个人及匹配标签的详细信息
            List<StaffMatched> staffMatcheds = getStaffMatchedByScore(stsList);

            rtrvStaffListByNameResponse.setStaffMatched(staffMatcheds);
            rtrvStaffListByNameResponse.setTotalNumber((long) stsList.size());

            return RestResponseUtil.success(rtrvStaffListByNameResponse);
        }
        //startingStr不为空，则按查询name以startingStr开始的用户
        List<Staff> list = this.staffRepository.findDistinctByNameContainingAndIsDeleted(startingStr, isDeleted);
        return RestResponseUtil.success(list);
    }

    /**
     * 根据tagId集合查询StaffTag
     * @param tags tags
     * @param isDeleted isDeleted
     * @return List
     */
    private List<StaffTag> rtrvStaffBytags(List<Long> tags, Integer isDeleted) {
        return tagRepository.findByIsDeletedAndTagIdIn(isDeleted, tags);
    }

    /**
     * 按标签推荐staff，对标签进行斐波那契数列赋值，最后按分值倒叙排列
     *
     * @param stList tagId在tags中的所有StaffTag对象；当StaffTag具有多个tag时，会有相同的staffId的多个StaffTag存在
     * @param tags   按照标签查Staff的参数
     * @param idList tagId在tags中的所有StaffTag不重复的staffId集合
     *               计算过程：
     *               1. 遍历idList， 并以每一个不重复的staffId 实例化一个StaffTagScore对象
     *               2. 计算每一个StaffTagScore 的tagScore
     *               3. 按照tagScore排序，最多获取前十条数据
     * @return List
     */
    private List<StaffTagScore> rtrvStaffTagScore(List<StaffTag> stList, List<Long> tags, List<Long> idList) {
        List<StaffTagScore> stsList = new ArrayList<>();
        //1. 遍历idList， 并以每一个不重复的staffId 实例化一个StaffTagScore对象
        for (Long id : idList) {
            StaffTagScore sts = new StaffTagScore();
            sts.setStaffId(id);
            sts.setTagScore(Double.valueOf(MdvnConstant.ZERO));
            //2.遍历stList
            for (int i = 0; i < stList.size(); i++) {
                StaffTag st = stList.get(i);
                if (id.equals(st.getStaffId())) {
                    //2. 计算每一个StaffTagScore 的tagScore
                    sts = countTagScore(st, tags, sts);
                    LOG.info("第{}个staff, 标签score: {}", i, sts.getTagScore());
                }
            }
            stsList.add(sts);
        }
        return topTenAtMost(stsList);
    }
    /**
     * 根据StaffTagScore集合获取每员工的个人及匹配标签的详细信息
     *
     * @param stsList stsList
     * @return List
     */
    private List<StaffMatched> getStaffMatchedByScore(List<StaffTagScore> stsList) throws BusinessException {
        List<StaffMatched> matchedList = new ArrayList<>();
        for (StaffTagScore aStsList : stsList) {
            StaffMatched staffMatched = getStaffMatched(aStsList.getStaffId(), aStsList.getTagId());
            staffMatched.setRecommendation(aStsList.getTagScore());
            matchedList.add(staffMatched);
        }
        return matchedList;
    }
    /**
     * 获取指定id的员工及标签的详细信息
     *
     * @param staffId staffId
     * @param tags tags
     * @return StaffMatched
     */
    private StaffMatched getStaffMatched(Long staffId, List<Long> tags) throws BusinessException {
        //根据Id获取员工信息
        Staff staff = this.staffRepository.findOne(staffId);
        //根据多个标签id获取标签详细信息Url
        String retrieveTagsUrl = webConfig.getRetrieveTagsUrl();
        List<TerseInfo> tagList = RestTemplateUtil.retrieveTerseInfo(staffId, tags, retrieveTagsUrl);
        StaffMatched staffMatched = new StaffMatched();
        staffMatched.setStaff(staff);
        staffMatched.setTags(tagList);
        return staffMatched;
    }

    /**
     * StaffId 去重
     *
     * @param staffList staffList
     * @return Set
     */
    private Set<Long> distinctStaffId(List<StaffTag> staffList) {
        Set<Long> staffSet = new HashSet<>();
        for (StaffTag st : staffList) {
            staffSet.add(st.getStaffId());
        }
        return staffSet;
    }

    /**
     * 根据斐波那契梳理计算每个tag对应的分值
     *
     * @param st st
     * @param tags tags
     * @param sts sts
     * @return StaffTagScore
     */
    private StaffTagScore countTagScore(StaffTag st, List<Long> tags, StaffTagScore sts) {
        //如果tags只有一个元素
        if (tags.size() == MdvnConstant.ONE) {
            sts.setTagId(tags);
            sts.setTagScore(Double.valueOf(MdvnConstant.ZERO));
            return sts;
        }
        for (int j = 0; j < tags.size(); j++) {
            LOG.info("j的值是：{}", j);
            LOG.info("第{}个staff, 标签score: {}", st.getStaffId(), sts.getTagScore());
            if (st.getTagId().equals(tags.get(j))) {
                List<Long> tagList = (sts.getTagId() == null) ? new ArrayList<>() : sts.getTagId();
                tagList.add(st.getTagId());
                Collections.sort(tagList);
                sts.setTagId(tagList);
                Double tagScore = sts.getTagScore();
                sts.setTagScore(tagScore + Math.pow(MdvnConstant.ONE_HALF, j + MdvnConstant.ONE));
            }
        }
        return sts;
    }

    /**
     * 1.根据tagScore排序，降序
     * 2.最多取前十条数据
     *
     * @param stsList stsList
     * @return List
     */
    private List<StaffTagScore> topTenAtMost(List<StaffTagScore> stsList) {
        LOG.info("排序前的StaffTagScore：{}", stsList.toString());
        //对StaffTagScore 按照tagScore排序，从高到底
        Comparator<StaffTagScore> comparator = Comparator.comparing(StaffTagScore::getTagScore);
        stsList.sort(comparator.reversed());
        LOG.info("排序后的StaffTagScore：{}", stsList.toString());
        //取tag分值从高到底前10个staff数据
        List<StaffTagScore> sList = new ArrayList<>();
        int m = (stsList.size() > MdvnConstant.TEN) ? MdvnConstant.TEN : stsList.size();
        if (stsList.isEmpty()) {
            return new ArrayList<>();
        }
        //最多取前十条数据
        for (int i = 0; i < m; i++) {
            LOG.info("staffId：{}", stsList.get(i).getStaffId());
            sList.add(stsList.get(i));
        }
        return sList;
    }
}

package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.project.domain.entity.ProjectTag;
import com.mdvns.mdvn.project.repository.TagRepository;
import com.mdvns.mdvn.project.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Resource
    private TagRepository projectTagRepository;


    /**
     * 新建项目后，添加项目标签映射
     * @param projId projId
     * @param tags tags
     * @param creatorId creatorId
     * @return List
     */
    @Override
    @Transactional
    public List<ProjectTag> createProjectTag(Long creatorId, Long projId, List<Long> tags) {
        LOG.info("id为：[{}]的staff，准备创建id为：[{}] 的项目的标签id为：[{}]的映射信息.", creatorId, projId, tags.toString());
        List<ProjectTag> projectTagList = new ArrayList<>();
        //遍历leaders构建ProjectStaff
        for (Long tagId:tags) {
            ProjectTag projectTag = new ProjectTag();
            projectTag.setCreatorId(creatorId);
            projectTag.setProjId(projId);
            projectTag.setTagId(tagId);
            projectTag.setCreateTime(new Timestamp(System.currentTimeMillis()));
            projectTag.setIsDeleted(MdvnConstant.ZERO);
            projectTag = this.projectTagRepository.saveAndFlush(projectTag);
            projectTagList.add(projectTag);
        }
        LOG.info("保存映射信息完成");
        return projectTagList;
    }

    /**
     * 移除标签映射
     * @param staffId 当前用户id
     * @param projId 当前项目id
     * @param tags 需要移除的标签id
     * @return number
     */
    @Override
    @Modifying
    public Integer updateIsDeleted(Long staffId, Long projId, List<Long> tags, Integer isDeleted) {
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的项目的id为：[{}]的标签映射信息.", staffId, projId, tags.toString());
        return this.projectTagRepository.updateIsDeleted(isDeleted, projId, tags);
    }

    /**
     * 根据projId和tagId查询
     * @param staffId 当前用户id
     * @param projId projId
     * @param tagId tagId
     * @return pt
     */
    @Override
    public ProjectTag findByProjIdAndTagId(Long staffId, Long projId, Long tagId) {
        //调用repository查询
        return this.projectTagRepository.findByProjIdAndTagId(projId, tagId);
    }

    /**
     * 更新项目标签映射
     * @param staffId 当前用户id
     * @param projId 当前项目id
     * @param tags 需要更新的标签id
     */
    @Override
    public void updateTag(Long staffId, Long projId, AddOrRemoveById tags) throws BusinessException {
        //删除标签映射
        if (null != tags.getRemoveList()) {
            MdvnCommonUtil.emptyList(tags.getRemoveList(), ErrorEnum.ILLEGAL_ARG, "删除标签不能为空");
            updateIsDeleted(staffId, projId, tags.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增标签映射
        if (null != tags.getAddList()) {
            MdvnCommonUtil.emptyList(tags.getAddList(), ErrorEnum.ILLEGAL_ARG, "新增标签不能为空");
            List<Long> addTags = new ArrayList<>();
            List<Long> updateTags = new ArrayList<>();
            for (Long id : tags.getAddList()) {
                //如果标签映射不存在添加id到addTags,已存在则添加id到removeTags,
                ProjectTag pt = this.projectTagRepository.findByProjIdAndTagId(projId, id);
                if (null == pt) {
                    addTags.add(id);
                } else {
                    updateTags.add(id);
                }
            }
            //更新已存在映射的isDeleted为0
            if (updateTags.size()>0) {
                updateIsDeleted(staffId, projId, updateTags, MdvnConstant.ZERO);
            }
            //添加新映射
            if (addTags.size()>0) {
                createProjectTag(staffId, projId, addTags);
            }

        }
    }
}

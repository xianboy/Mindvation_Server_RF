package com.mdvns.mdvn.staff.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.staff.domain.CreateStaffRequest;
import com.mdvns.mdvn.staff.domain.LoginRequest;
import com.mdvns.mdvn.staff.domain.StaffDetail;
import com.mdvns.mdvn.staff.domain.UpdateStaffRequest;
import com.mdvns.mdvn.staff.domain.entity.Staff;
import com.mdvns.mdvn.staff.repository.StaffRepository;
import com.mdvns.mdvn.staff.service.CreateService;
import com.mdvns.mdvn.staff.service.StaffTagService;
import com.mdvns.mdvn.staff.util.StaffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.sql.Timestamp;

@Service
public class CreateServiceImpl implements CreateService {
    /* 日志常量 */
    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;


    @Autowired
    private StaffTagService staffTagService;

    /**
     * 登录
     *
     * @param loginRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> login(LoginRequest loginRequest) throws BusinessException {
        Staff staff = this.staffRepository.findByAccountAndPassword(loginRequest.getAccount(), loginRequest.getPassword());
        //staff为空，抛出异常
        if (null == staff) {
            LOG.error("登录名:[{}]或密码{[]}错误.", loginRequest.getAccount(), loginRequest.getPassword());
            throw new BusinessException(ErrorEnum.NOT_EXISTS, "登录名或密码错误.");
        }
        //构建response
        return RestResponseUtil.success(staff);
    }

    /**
     * 更新staff
     *
     * @param updateRequest request
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> updateStaff(UpdateStaffRequest updateRequest) throws BusinessException {
        //从request中获取id
        Long id = updateRequest.getId();
        //根据id查询staff
        Staff staff = this.staffRepository.findOne(id);
        //如果staff为空，抛出异常
        MdvnCommonUtil.notExistingError(staff, "id", updateRequest.getId().toString());
        //根据updateRequest构建staff
        staff = buildStaffByUpdateRequest(staff, updateRequest);
        //保存
        staff = this.staffRepository.saveAndFlush(staff);
        return RestResponseUtil.success(staff);
    }

    /**
     * 根据updateRequest构建staff
     *
     * @param staff
     * @param updateRequest
     * @return
     */
    private Staff buildStaffByUpdateRequest(Staff staff, UpdateStaffRequest updateRequest) {
        //设置deptId
        if (StringUtils.isEmpty(updateRequest.getDeptId())) {
            staff.setDeptId(updateRequest.getDeptId());
        }
        //设置positionId
        if (StringUtils.isEmpty(updateRequest.getPositionId())) {
            staff.setPositionId(updateRequest.getPositionId());
        }
        //设置positionLvl
        if (StringUtils.isEmpty(updateRequest.getPositionLvl())) {
            staff.setPositionLvl(updateRequest.getPositionLvl());
        }
        //设置Email
        if (StringUtils.isEmpty(updateRequest.getEmail())) {
            staff.setEmail(updateRequest.getEmail());
        }
        //设置Mobile
        if (StringUtils.isEmpty(updateRequest.getMobile())) {
            staff.setMobile(updateRequest.getMobile());
        }
        //设置Gender
        if (StringUtils.isEmpty(updateRequest.getGender())) {
            staff.setGender(updateRequest.getGender());
        }
        staff.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        return staff;
    }

    /**
     * 添加staff
     *
     * @param request request
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> create(CreateStaffRequest request) throws BusinessException {
        //从请求对象中获取登录账号
        String account = request.getAccount();
        Staff staff = this.staffRepository.findByAccount(account);
        //根据account查询, 如果用户已存在,抛出异常
        MdvnCommonUtil.existingError(staff, "account", account);
        //根据createStaffRequest构建staff对象
        staff = buildStaffByRequest(request);
        LOG.info("要保存的staff:{}", staff.toString());
        //保存staff
        staff = this.staffRepository.saveAndFlush(staff);
        //用户保存成功后, 添加标签映射
        Long staffId = staff.getId();
        LOG.info("保存后staff的Id：{}", staff.getId());
        if (!(null == request.getTags() || request.getTags().isEmpty())) {
            this.staffTagService.createStaffTag(staffId, request.getTags(), request.getCreatorId());
        }
        //根据Staff构建StaffResponse
        StaffDetail staffDetail = StaffUtil.buildDetailByStaff(staff);
        return RestResponseUtil.success(staffDetail);
    }

    /**
     * 根据createStaffRequest 构建Staff
     *
     * @param request request
     * @return staff
     */
    private Staff buildStaffByRequest(CreateStaffRequest request) {
        Staff staff = new Staff();
        //设置创建人Id
        staff.setCreatorId(request.getCreatorId());
        //设置编号
        staff.setSerialNo(buildSerialNo());
        //设置密码
        staff.setPassword(request.getPassword());
        //设置部门id
        staff.setDeptId(request.getDeptId());
        //设置姓名
        staff.setName(request.getName());
        //设置账号/登录名
        staff.setAccount(request.getAccount());
        //设置添加人
        staff.setCreatorId(request.getCreatorId());
        //设置职位id
        if (null != request.getPositionId()) {
            staff.setPositionId(request.getPositionId());
        }
        //设置职级
        if (null != request.getPositionLvl()) {
            staff.setPositionLvl(request.getPositionLvl());
        }
        //设置邮箱
        if (null != request.getEmail()) {
            staff.setEmail(request.getEmail());
        }
        //设置手机号
        if (null != request.getMobile()) {
            staff.setMobile(request.getMobile());
        }
        //设置性别
        staff.setGender(request.getGender());
        //设置状态
        staff.setStatus("active");
        //设置创建时间
        staff.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return staff;
    }

    /**
     * 构建员工编号
     *
     * @return String
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.staffRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = 0L;
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.E + maxId;
    }


    /**
     * 获取全部模块
     * @return
     */
   /* @Override
    public RetrieveStaffListResponse rtrvStaffList(RetrieveStaffListRequest request) {
        RetrieveStaffListResponse retrieveStaffListResponse = new RetrieveStaffListResponse();
        if (request.getPage() == null || request.getPageSize() == null) {
//            List<Staff> list = this.staffRepository.findAll();
            List<Staff> list = this.staffRepository.findAllByAccountIsNot("admin");
            retrieveStaffListResponse.setStaffs(list);
            retrieveStaffListResponse.setTotalNumber(Long.valueOf(list.size()));
            return retrieveStaffListResponse;
        } else {
            String sortBy = request.getSortBy();
            Integer page = request.getPage() - 1;
            Integer pageSize = request.getPageSize();
            sortBy = (sortBy == null) ? "uuId" : sortBy;
            PageRequest pageable = new PageRequest(page, pageSize, Sort.Direction.ASC, sortBy);
            Page<Staff> staffPage = null;
//            staffPage = this.staffRepository.findAll(pageable);
            staffPage = this.staffRepository.findAllByAccountIsNot("admin", pageable);
//            Long count = this.staffRepository.getStaffCount();
            retrieveStaffListResponse.setStaffs(staffPage.getContent());
            retrieveStaffListResponse.setTotalNumber(staffPage.getTotalElements());
            return retrieveStaffListResponse;
        }
    }

    *//**
     * 通过staffIdList获取staff对象列表
     * @param request
     * @return
     *//*
    @Override
    public List<Staff> rtrvStaffListById(RtrvStaffListByIdRequest request) {
        List<Staff> list = new ArrayList<>();
        for (int i = 0; i < request.getStaffIdList().size(); i++) {
            Staff staffInfo = this.staffRepository.findByStaffId(request.getStaffIdList().get(i));
            if (null == staffInfo) {
                LOG.error("该id的员工在员工库中不存在.", staffInfo);
                throw new BusinessException(staffInfo + "该id的员工在员工库中不存在.");
            } else {
                list.add(staffInfo);
            }
        }
        return list;
    }

    *//**
     * 通过staffId获取单条staff对象
     * @param staffId
     * @return
     *//*
    @Override
    public Staff rtrvStaffInfo(String staffId) {
        return this.staffRepository.findByStaffId(staffId);
    }


    @Override
    public List<StaffTag> rtrvStaffTagList(String staffId) {
        return staffTagRepository.findByStaffIdAndIsDeleted(staffId, 0);
    }

    @Override
    public Boolean updateStaffDetail(UpdateStaffDetailRequest request) {
        Boolean flag = false;
        Staff staff = staffRepository.findByStaffId(request.getStaffInfo().getStaffId());
        Staff updateStaff = request.getStaffInfo();
//        RtrvStaffDetailResponse response = new RtrvStaffDetailResponse();
        if (staff != null) {
            if (!updateStaff.getDeptId().equals(staff.getDeptId())) {
                staff.setDeptId(updateStaff.getDeptId());
            }
            if (!StringUtils.isEmpty(updateStaff.getPassword()) && !updateStaff.getPassword().equals(staff.getPassword())) {
                staff.setPassword(updateStaff.getPassword());
            }
            if (!StringUtils.isEmpty(updateStaff.getGender()) && !updateStaff.getGender().equals(staff.getGender())) {
                staff.setGender(updateStaff.getGender());
            }
            if (!StringUtils.isEmpty(updateStaff.getPositionId()) && !updateStaff.getPositionId().equals(staff.getPositionId())) {
                staff.setPositionId(updateStaff.getPositionId());
            }
            if (!StringUtils.isEmpty(updateStaff.getPositionLvl()) && !updateStaff.getPositionLvl().equals(staff.getPositionLvl())) {
                staff.setPositionLvl(updateStaff.getPositionLvl());
            }
            if (!StringUtils.isEmpty(updateStaff.getEmailAddr()) && !updateStaff.getEmailAddr().equals(staff.getEmailAddr())) {
                staff.setEmailAddr(updateStaff.getEmailAddr());
            }
            if (!StringUtils.isEmpty(updateStaff.getPhoneNum()) && !updateStaff.getPhoneNum().equals(staff.getPhoneNum())) {
                staff.setPhoneNum(updateStaff.getPhoneNum());
            }
            if (!StringUtils.isEmpty(updateStaff.getStatus()) && !updateStaff.getStatus().equals(staff.getStatus())) {
                staff.setStatus(updateStaff.getStatus());
            }

            staff = staffRepository.saveAndFlush(staff);

            flag = true;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<String> tagIds = request.getTagIds();
        List<StaffTag> staffTagList = staffTagRepository.findByStaffIdAndIsDeleted(request.getStaffInfo().getStaffId(), 0);
        if (tagIds != null && tagIds.size() == 0) {
            for (int i = 0; i < staffTagList.size(); i++) {
                staffTagList.get(i).setIsDeleted(1);
                staffTagList.get(i).setLastUpdateTime(now);
            }
            staffTagRepository.save(staffTagList);
        } else if (tagIds != null && tagIds.size() > 0) {
            List<StaffTag> oldStaffTag = staffTagRepository.findByStaffIdAndIsDeleted(request.getStaffInfo().getStaffId(), 0);
            List<StaffTag> rmvStaffTag = oldStaffTag;

            for (int i = 0; i < tagIds.size(); i++) {
                for (int j = 0; j < oldStaffTag.size(); j++) {
                    if (oldStaffTag.get(j).getTagId().equals(tagIds.get(i))) {
                        rmvStaffTag.remove(j);
                    }
                }
            }

            if (!rmvStaffTag.isEmpty()) {
                for (int i = 0; i < rmvStaffTag.size(); i++) {
                    rmvStaffTag.get(i).setIsDeleted(1);
                    rmvStaffTag.get(i).setLastUpdateTime(now);
                }
                staffTagRepository.save(oldStaffTag);
                flag = true;
            }

            oldStaffTag = staffTagRepository.findByStaffIdAndIsDeleted(request.getStaffInfo().getStaffId(), 0);
            List<String> addList = tagIds;
            for (int i = 0; i < tagIds.size(); i++) {
                for (int j = 0; j < oldStaffTag.size(); j++) {
                    if (tagIds.get(i).equals(oldStaffTag.get(j).getTagId())) {
                        addList.remove(i);
                    }
                }
            }

            if (!addList.isEmpty()) {
                List<StaffTag> addStaffTag = new ArrayList<>();
                for (int i = 0; i < addList.size(); i++) {
                    StaffTag staffTag = new StaffTag();
                    staffTag.setStaffId(staff.getStaffId());
                    staffTag.setTagId(addList.get(i));
                    staffTag.setIsDeleted(0);
                    staffTag.setLastUpdateTime(now);
                    addStaffTag.add(staffTag);
                }
                staffTagRepository.save(addStaffTag);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Boolean deleteStaff(String staffId) {
        Staff staff = staffRepository.findByStaffId(staffId);
        if (staff != null && !staff.getStatus().equals("cancellation")) {
            staff.setStatus("cancellation");
        }
        staffRepository.saveAndFlush(staff);
        return true;
    }


    *//**
     * 根据name模糊查询Staff，当name==null, 通过tag查询staff
     * @param request
     * @return
     * @throws SQLException
     *//*
    @Override
    public ResponseEntity<?> rtrvStaffListByStaffName(RtrvStaffListByNameRequest request) throws SQLException {
        String sortBy = (request.getSortBy() == null) ? "staffId" : request.getSortBy();
        //姓名
        String name = request.getName();
        //如果name没有值
        if (StringUtils.isEmpty(name)) {
            List<String> tags = request.getTags();
            if (tags == null) {
                LOG.error("标签参数为空:{}", tags);
            }
            return null;
        }
        //第几页
        Integer page = request.getPage();
        //需获取的数据条数
        Integer pageSize = request.getPageSize();
        page = (page == null || page < Integer.valueOf(ConstantEnum.ONE.getValue())) ? Integer.valueOf(ConstantEnum.ONE.getValue()) : page;
        pageSize = (pageSize == null || pageSize < Integer.valueOf(ConstantEnum.ONE.getValue())) ? Integer.valueOf(ConstantEnum.TEN.getValue()) : pageSize;
        //构建分页参数
        PageRequest pageable = new PageRequest(page, pageSize);
        //分页对象
        Page<Staff> staffPage;
        LOG.info("查询name以：{}开头的员工", name);
        staffPage = this.staffRepository.findByNameStartingWith(name, pageable);

        return ResponseEntity.ok(staffPage);

    }


    *//**
     * 根据指定account查询staff
     * @param account
     * @return
     *//*
    @Override
    public ResponseEntity<?> findByAccountAndPassword(String account, String password) {
        Staff staff = this.staffRepository.findByAccountAndPassword(account, password);
        return ResponseEntity.ok(staff);
    }

    *//**
     * 查找拥有标签集中最多标签的StaffTag
     * @param tags
     * @return List<StaffTag>
     *//*
    @Override
    public ResponseEntity<?> getStaffByTags(List<String> tags) {
        List<StaffTag> staffTags = this.staffTagRepository.findByTagIdIn(tags);
        LOG.info("拥有标签集中任意标签的Staff有：{}个", staffTags.size());
        return ResponseEntity.ok(staffTags);
    }

    *//**
     * 查询name以指定字符串开头的所有Staff
     * @param startingStr
     * @return
     *//*
    @Override
    public ResponseEntity<?> findByNameStartingWith(String startingStr) {

        List<Staff> staffPage = this.staffRepository.findByNameStartingWith(startingStr);
        LOG.info("查询name以指定字符串:{}开头的所有Staff...", startingStr);
        return ResponseEntity.ok(staffPage);
    }

    *//**
     * 查询staffId为指定id的所有tagId集合
     * @param staffId
     * @return
     *//*
    @Override
    public List<String> rtrvTagsByStaffId(String staffId) {

        return this.staffTagRepository.findTagIdByStaffId(staffId);
    }

*/
}

package com.mdvns.mdvn.staff.util;

import com.mdvns.mdvn.staff.domain.StaffDetail;
import com.mdvns.mdvn.staff.domain.entity.Staff;
import org.springframework.util.StringUtils;

public class StaffUtil {

    /**
     * 根据Staff构建StaffDetail
     * @param staff staff
     * @return StaffDetail
     */
    public static StaffDetail buildDetailByStaff(Staff staff) {
        StaffDetail staffDetail = new StaffDetail();
        //id
        staffDetail.setId(staff.getId());
        //账号/登录名
        staffDetail.setAccount(staff.getAccount());
        //头像
        if (!StringUtils.isEmpty(staff.getAvatar())) {
            staffDetail.setAvatar(staff.getAvatar());
        }
        //贡献值
        if (null != staff.getContribution()) {
            staffDetail.setContribution(staff.getContribution());
        }
        //添加时间
        staffDetail.setCreateTime(staff.getCreateTime().getTime());
        //添加当前用户的人
        staffDetail.setCreatorId(staff.getCreatorId());
        //效率值
        if (null != staff.getEffective()) {
            staffDetail.setEffective(staff.getEffective());
        }
        //邮箱
        if (null != staff.getEmail()) {
            staffDetail.setEmail(staff.getEmail());
        }
        //性别
        staffDetail.setGender(staff.getGender());
        //入职时间
        if (null !=staff.getHiredate()) {
            staffDetail.setHiredate(staff.getHiredate().getTime());
        }
        //手机号码
        if (null !=staff.getMobile()) {
            staffDetail.setMobile(staff.getMobile());
        }
        //姓名
        staffDetail.setName(staff.getName());
        //职级
        if (null!=staff.getPositionLvl()) {
            staffDetail.setPositionLvl(staff.getPositionLvl());
        }
        //推荐度
        if (null!=staff.getRecommendation()) {
            staffDetail.setRecommendation(staff.getRecommendation());
        }
        //编号
        staffDetail.setSerialNum(staff.getSerialNo());
        //工作饱和度
        if (null != staff.getWorkSaturation()) {
            staffDetail.setWorkSaturation(staff.getWorkSaturation());
        }
        //状态
        if (!StringUtils.isEmpty(staff.getStatus())) {
            staffDetail.setStatus(staff.getStatus());
        }
        //入职日期
        if (null!= staff.getHiredate()) {
            staffDetail.setHiredate(staff.getHiredate().getTime());
        }
        return staffDetail;
    }

}

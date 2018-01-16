package com.mdvns.mdvn.department.repository;

import com.mdvns.mdvn.department.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeptRepository extends JpaRepository<Department, Long>{

    //查询id的最大值
    @Query(value = "select max(id) from department", nativeQuery = true)
    Long getMaxId();

    //通过名称查部门
    Department findByName(String name);
}

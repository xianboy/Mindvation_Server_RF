package com.mdvns.mdvn.staff.repository;

import com.mdvns.mdvn.staff.domain.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    //根据account查询staff
    Staff findByAccount(String account);
    //查询id的最大值
    @Query(value = "select max(id) from staff", nativeQuery = true)
    Long getMaxId();

    //根据登录名和密码查询staff
    Staff findByAccountAndPassword(String account, String password);

    //获取指定id集合的id和name
    @Query("select s.id, s.serialNo, s.name from Staff s where s.id in ?1 order by s.id")
    List<Object[]> findTerseInfoById(List<Long> ids);
/*
    Page<Staff> findAll(Pageable pageable);

    Staff findByStaffId(String staffId);

    Page<Staff> findByNameStartingWith(String pswd, Pageable pageable);

    @Query(value="SELECT DISTINCT COUNT(*) FROM staff ", nativeQuery = true)
    Long getStaffCount();

    Page<Staff> findByNameLike(String name, Pageable pageable);


    Staff findByAccountAndPassword(String account, String password);

    Staff findFirstByName(String name);
    Staff findFirstByAccount(String account);

    List<Staff> findAllByAccountIsNot(String account);

    Page<Staff> findAllByAccountIsNot(String account, Pageable pageable);

//    @Query(value = "select * from staff where name like %?1", nativeQuery = true)
    List<Staff> findByNameLike(String name);


    List<Staff> findByNameStartingWith(String startingStr);
*/


}

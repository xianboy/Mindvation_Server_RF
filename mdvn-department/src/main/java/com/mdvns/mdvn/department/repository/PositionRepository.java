package com.mdvns.mdvn.department.repository;

import com.mdvns.mdvn.department.domain.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long>{
    //根据名称查询position
    Position findByName(String positionName);

    //根据id集合查询职位信息
    List<Position> findByIdIn(List<Long> positions);

    List<Position> findAllByHostIdAndIsDeleted(Long hostId, Integer isDeleted);
}

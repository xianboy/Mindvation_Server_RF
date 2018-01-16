package com.mdvns.mdvn.issue.repository;


import com.mdvns.mdvn.issue.domain.entity.IssueAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueAnswerRepository extends JpaRepository<IssueAnswer, Integer> {

    List<IssueAnswer> findByIssueId(String issueId);

    IssueAnswer findByAnswerId(String answerId);

    @Query(value="SELECT creator_id FROM story WHERE story_id = ?1 UNION ALL (SELECT creator_id FROM requirement_info WHERE reqmnt_id = ?1)", nativeQuery = true)
    String findCreateId(String subjectId);



}

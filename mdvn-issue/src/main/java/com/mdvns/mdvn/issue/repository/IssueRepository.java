package com.mdvns.mdvn.issue.repository;


import com.mdvns.mdvn.issue.domain.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    Issue findByIssueId(String issueId);

    Issue findFirstBySubjectId(String subjectId);

    List<Issue> findBySubjectId(String subjectId);

    /*已解决的*/
    @Query
    Page<Issue> findByIsResolvedAndIsDeleted(Pageable pageable, Integer isResolved, Integer isDeleted);

    @Query(value="SELECT creator_id FROM story WHERE story_id = ?1 UNION ALL (SELECT creator_id FROM requirement_info WHERE reqmnt_id = ?1)", nativeQuery = true)
    String findCreateId(String subjectId);



}

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

    /*查询每个员工回答过的所有求助*/
    @Query(value="SELECT * FROM issue WHERE issue_id IN(SELECT issue_id FROM issue_answer WHERE creator_id = ?1)", nativeQuery = true)
    List<Issue> findAllIssueByAnswerStaffId(Long creatorId);

    /*查询每个员工回答过的已解决的的求助*/
    @Query(value="SELECT * FROM issue WHERE issue_id IN(SELECT issue_id FROM issue_answer WHERE creator_id = ?1) AND is_resolved = 1", nativeQuery = true)
    List<Issue> findAllIssueListHaveResolved(Long creatorId);

    /*查询每个员工回答过的已被采纳的的求助*/
    @Query(value="SELECT * FROM issue WHERE issue_id IN(SELECT issue_id FROM issue_answer WHERE creator_id = ?1 AND is_adopt = 1) AND is_resolved = 1", nativeQuery = true)
    List<Issue> findAllIssueListHaveAdopt(Long creatorId);



}

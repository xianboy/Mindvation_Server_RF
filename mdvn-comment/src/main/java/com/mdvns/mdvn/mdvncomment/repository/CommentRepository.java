package com.mdvns.mdvn.mdvncomment.repository;


import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByProjIdAndSubjectIdAndIsDeleted(String projId, String subjectId, Integer isDeleted);
    Comment findByCommentId(String commentId);

    @Query(value="SELECT creator_id FROM story WHERE serial_no = ?1 UNION ALL (SELECT creator_id FROM requirement WHERE serial_no = ?1) UNION ALL (SELECT creator_id FROM issue_answer WHERE answer_id = ?1)", nativeQuery = true)
    String findCreateId(String subjectId);

}

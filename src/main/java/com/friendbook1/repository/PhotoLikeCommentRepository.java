package com.friendbook1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.friendbook1.entities.Comment;
import com.friendbook1.entities.PhotoLikeComment;
import java.util.List;


@Repository
public interface PhotoLikeCommentRepository extends JpaRepository<PhotoLikeComment, Integer> {
//	@Query("SELECT plc.comment FROM PhotoLikeComment plc WHERE plc.randomId = :randomId")
//	List<Comment> findCommentsByRandomId(@Param("randomId") int randomId);
}

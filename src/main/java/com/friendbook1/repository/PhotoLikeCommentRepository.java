package com.friendbook1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook1.entities.PhotoLikeComment;


@Repository
public interface PhotoLikeCommentRepository extends JpaRepository<PhotoLikeComment, Integer> {
}

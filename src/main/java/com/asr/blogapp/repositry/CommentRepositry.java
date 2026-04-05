package com.asr.blogapp.repositry;

import com.asr.blogapp.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepositry extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPostIdOrderByCreatedOn(int postId);

    List<PostComment> findTop5ByPostIdOrderByCreatedOnDesc(int postId);
}
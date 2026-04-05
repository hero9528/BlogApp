package com.asr.blogapp.service;

import com.asr.blogapp.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
  void addComment(CommentDto commentDto);

  List<CommentDto> getCommentsByPostId(int postId);

  List<CommentDto> getTop5CommentsByPostId(int postId);

  Page<CommentDto> getAllComments(Pageable pageable);

   void deleteComment(Long id);
}

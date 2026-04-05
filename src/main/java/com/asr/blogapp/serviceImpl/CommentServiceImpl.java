package com.asr.blogapp.serviceImpl;

import com.asr.blogapp.dto.CommentDto;
import com.asr.blogapp.entity.PostComment;
import com.asr.blogapp.entity.PostEntity;
import com.asr.blogapp.repositry.CommentRepositry;
import com.asr.blogapp.repositry.PostRepositry;
import com.asr.blogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepositry commentRepositry;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostRepositry postRepositry;

    @Override
    public void addComment(CommentDto commentDto) {

        PostEntity post = postRepositry.findById(
                commentDto.getPostId()).orElseThrow(() -> new RuntimeException("post not found"));

     PostComment commentEntity = modelMapper.map(commentDto, PostComment.class);

        commentEntity.setPost(post);
        commentRepositry.save(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(int postId) {

        List<PostComment> postComments = commentRepositry.findByPostIdOrderByCreatedOn(postId);
        List<CommentDto> commentDtos = postComments.stream().map(comment -> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public List<CommentDto> getTop5CommentsByPostId(int postId) {
        List<PostComment> postComments = commentRepositry.findTop5ByPostIdOrderByCreatedOnDesc(postId);
        return postComments.stream().map(comment -> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<CommentDto> getAllComments(Pageable pageable) {

        Page<PostComment> comments =  commentRepositry.findAll(pageable);
        Page<CommentDto> commentDtos = comments.map(c -> modelMapper.map(c, CommentDto.class));
        return commentDtos;
    }

    @Override
    public void deleteComment(Long id) {
        commentRepositry.deleteById(id);
    }
}
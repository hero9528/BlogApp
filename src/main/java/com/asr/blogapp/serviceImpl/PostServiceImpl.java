package com.asr.blogapp.serviceImpl;

import com.asr.blogapp.appconfig.SecurityHelper;
import com.asr.blogapp.dto.PostDto;
import com.asr.blogapp.entity.CategoryEntity;
import com.asr.blogapp.entity.MyUserEntity;
import com.asr.blogapp.entity.PostEntity;
import com.asr.blogapp.entity.Role;
import com.asr.blogapp.helper.Helper;
import com.asr.blogapp.repositry.UserRepositry;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import com.asr.blogapp.repositry.CategoryRepositry;
import com.asr.blogapp.repositry.PostRepositry;
import com.asr.blogapp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepositry  postRepositry;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired
    private UserRepositry userRepositry;

    @Override
    public PostDto savePost(PostDto postDto) {

        PostEntity postEntity = new PostEntity();

        postEntity.setId(postDto.getId());
        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setShortDescription(postDto.getShortDescription());
        postEntity.setUrl(postDto.getUrl());
        postEntity.setPostImageName(postDto.getPostImageName());
        postEntity.setCreatedOn(postDto.getCreatedOn());
        postEntity.setUpdatedOn(postDto.getUpdatedOn());

        // set post pending
        postEntity.setStatus("PENDING");
        // save post
        CategoryEntity categoryEntity = categoryRepositry.findById(postDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        postEntity.setCategory(categoryEntity);

        String username =  SecurityHelper.currentLoggedInUser();
        MyUserEntity currentLoggedUser = userRepositry.findByEmail(username);
        postEntity.setUser(currentLoggedUser);


        PostEntity savedPost = postRepositry.save(postEntity);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public Page<PostDto> getPostForCurrentUser(int pageNo, int size) {

       Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());

        String username = SecurityHelper.currentLoggedInUser();

        MyUserEntity loggedInUser = userRepositry.findByEmail(username);

        // check user role
       boolean isAdmin = loggedInUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
        Page<PostEntity> postPage;
       if (isAdmin) {

           postPage =  postRepositry.findAll(pageable);
       } else {

           postPage = postRepositry.findByUser(loggedInUser, pageable);
       }





        Page<PostDto> postPageDto = postPage.map(p -> modelMapper.map(p, PostDto.class));
        return postPageDto;
    }

    @Override
    public void approvePost(int postId, String status) {
        PostEntity post = postRepositry.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus("APPROVED");
        postRepositry.save(post);

    }

    @Override
    public void rejectPost(int postId, String status) {
        PostEntity post = postRepositry.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus("REJECTED");
        postRepositry.save(post);
    }

    @Override
    public PostDto getPostById(int postId) {
        PostEntity post = postRepositry.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void deletePost(int postId) {
        // First delete the image from the folder
        deleteImage(postId);
        // Then delete the post from the database
        postRepositry.deleteById(postId);
    }

    @Override
    public void updatePost(PostDto postDto) {
        PostEntity postEntity = postRepositry.findById(postDto.getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setShortDescription(postDto.getShortDescription());
        postEntity.setUrl(postDto.getUrl());
        postEntity.setUpdatedOn(postDto.getUpdatedOn());

        // Check if a new image is uploaded
        if (postDto.getPostImageName() != null && !postDto.getPostImageName().isEmpty()) {
            String oldImageName = postEntity.getPostImageName();

            // If there is an old image it's different from the new one, delete it from the folder
            if (oldImageName != null && !oldImageName.equals(postDto.getPostImageName())) {
                try {
                    Path oldImagePath = Path.of("postfiles", String.valueOf(postEntity.getId()), oldImageName);
                    Files.deleteIfExists(oldImagePath);
                } catch (IOException e) {
                    System.out.println("Error deleting old image: " + e.getMessage());
                }
            }
            // Set the new image name in the entity
            postEntity.setPostImageName(postDto.getPostImageName());
        }

        CategoryEntity categoryEntity = categoryRepositry.findById(postDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        postEntity.setCategory(categoryEntity);

        postRepositry.save(postEntity);
    }

    private void deleteImage(int postId) {
        PostEntity post = postRepositry.findById(postId).orElse(null);
        if (post != null && post.getPostImageName() != null) {
            try {
                // Construct the correct path: postfiles/{postId}/{imageName}
                Path imagePath = Path.of("postfiles", String.valueOf(postId), post.getPostImageName());
                Files.deleteIfExists(imagePath);
                
                // Also delete the empty folder: postfiles/{postId}
                Path dirPath = Path.of("postfiles", String.valueOf(postId));
                Files.deleteIfExists(dirPath);
            } catch (IOException e) {
                System.out.println("Error deleting image: " + e.getMessage());
            }
        }
    }

    @Override
    public Page<PostDto> getPostByUser(Pageable pageable) {
       Page<PostEntity> approvedPosta =  postRepositry.findByStatus("APPROVED", pageable);
       Page<PostDto> approvedDto = approvedPosta.map(p -> {
           PostDto postDto = modelMapper.map(p, PostDto.class);
           postDto.setTitle(Jsoup.clean(postDto.getTitle(), Safelist.none()));
           postDto.setShortDescription(Helper.htmlSenitizer(postDto.getShortDescription()));
           return postDto;
       });
       return approvedDto;
    }

    @Override
    public Page<PostDto> getPostByCategory(int categoryId, Pageable pageable) {

        Page<PostEntity> postPage =  postRepositry.findPostsByCategoryAndStatus(categoryId, "APPROVED", pageable);
        Page<PostDto> postPageDto = postPage.map(p -> {
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setTitle(Jsoup.clean(postDto.getTitle(), Safelist.none()));
            postDto.setShortDescription(Helper.htmlSenitizer(postDto.getShortDescription()));
            return postDto;
        });
        return postPageDto;
    }

    @Override
    public Page<PostDto> searchBlogPost(String search, Pageable pageable) {
        Page<PostEntity> postSearch = postRepositry.findPostBySearch(search, "APPROVED", pageable);
        Page<PostDto> searchPost = postSearch.map(p -> {
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setTitle(Jsoup.clean(postDto.getTitle(), Safelist.none()));
            postDto.setShortDescription(Helper.htmlSenitizer(postDto.getShortDescription()));
            return postDto;
        });
        return searchPost;
    }

    @Override
    public PostDto getFullViewPost(int id, String url) {

        PostEntity viewPost = postRepositry.findByIdAndUrl(id, url).orElseThrow(() -> new RuntimeException("Post Not Found Valid Id..."));


        return  modelMapper.map(viewPost, PostDto.class);
    }
}
package com.asr.blogapp.repositry;

import com.asr.blogapp.entity.MyUserEntity;
import com.asr.blogapp.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepositry extends JpaRepository<PostEntity, Integer> {

    Page<PostEntity> findByStatus(String status, Pageable pageable);

    @Query("SELECT p FROM PostEntity p Where p.category.id=:categoryId AND p.status =:status")
    Page<PostEntity> findPostsByCategoryAndStatus(@Param("categoryId") int categoryId, @Param("status") String status, Pageable pageable);

    // search user String in title
    @Query("SELECT p FROM PostEntity p Where (p.title LIKE concat('%',:search,'%') or p.shortDescription LIKE concat('%',:search,'%')) AND p.status =:status")
    Page<PostEntity> findPostBySearch(@Param("search") String search, @Param("status") String status, Pageable pageable);


    Optional<PostEntity> findByIdAndUrl(int id, String url);

   Page<PostEntity> findByUser(MyUserEntity myUser,  Pageable pageable);
}

package com.asr.blogapp.repositry;

import com.asr.blogapp.entity.MyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositry extends JpaRepository<MyUserEntity, Long> {

    MyUserEntity findByEmail(String email);

}

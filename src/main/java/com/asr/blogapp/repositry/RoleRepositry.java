package com.asr.blogapp.repositry;


import com.asr.blogapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositry extends JpaRepository<Role, Long> {

    Role findByName(String name);   // ✅ FIX
}

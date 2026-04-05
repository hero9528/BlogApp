package com.asr.blogapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String email;

    private String commentMsg;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @ManyToOne
    private PostEntity post;
}

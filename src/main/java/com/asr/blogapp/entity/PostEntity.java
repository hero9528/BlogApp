package com.asr.blogapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostEntity {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(nullable = false)
	    private String title;

	    @Lob
	    @Column(nullable = false, columnDefinition = "TEXT")
	    private String content;

	    private String url;

	    @Column(nullable = false)
	    private String shortDescription;

	    @CreationTimestamp
	    @Column(updatable = false)
	    private LocalDateTime createdOn;

	    @UpdateTimestamp
	    @Column(insertable = false)
	    private LocalDateTime updatedOn;

	    @Column(nullable = false)
	    private String status;


	    private String postImageName;

		@ManyToOne(fetch = FetchType.EAGER)
		private CategoryEntity category;


		@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
		private List<PostComment> comment = new ArrayList<>();

		@ManyToOne
		private MyUserEntity user;
}

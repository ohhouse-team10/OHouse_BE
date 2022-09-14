package com.sparta.todayhouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Likes> likes;

    public void update(PostRequestDto requestDto){
        this.content = requestDto.getContent();
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj) return false;

        Post post = (Post) obj;
        return this.id == post.getId();
    }

    public void plusViews() { views++; }
}

package com.sparta.todayhouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Post post;


    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Member member;
}

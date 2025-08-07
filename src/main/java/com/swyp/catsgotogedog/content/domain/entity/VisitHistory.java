package com.swyp.catsgotogedog.content.domain.entity;

import com.swyp.catsgotogedog.User.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class VisitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_id")
    private Content content;
}

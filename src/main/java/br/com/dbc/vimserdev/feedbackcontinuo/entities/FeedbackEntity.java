package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "feedback")
public class FeedbackEntity {

    @Id
    @Column(name = "feedback_id")
    private final String feedbackId = UUID.randomUUID().toString();

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(name = "feedback_user_id", insertable = false, updatable = false)
    private String feedbackUserId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity feedbackEntityGiven;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_user_id")
    private UserEntity feedbackEntityReceived;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "feedback_tags",
            joinColumns = @JoinColumn(name = "feedback_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tags;

}

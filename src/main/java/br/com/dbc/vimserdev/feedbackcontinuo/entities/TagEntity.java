package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tags")
public class TagEntity {

    @Id
    @Column(name = "tag_id")
    private final String tagId = UUID.randomUUID().toString();

    @Column(name = "name")
    private String tagName;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<FeedbackEntity> feedbacks;
}

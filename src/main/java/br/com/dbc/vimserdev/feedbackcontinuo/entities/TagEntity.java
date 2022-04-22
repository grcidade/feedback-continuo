package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tags")
public class TagEntity {

    @Id
    @Column(name = "tag_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(name = "name")
    private String tagName;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<FeedbackEntity> feedbacks;
}

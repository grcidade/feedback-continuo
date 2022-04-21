package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tags")
public class TagEntity {

    @Id
    @Column(name = "id_role", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(name = "name")
    private String tagName;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<FeedbackEntity> feedbacks;
}

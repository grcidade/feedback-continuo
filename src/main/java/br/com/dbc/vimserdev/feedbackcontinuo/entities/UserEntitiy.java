package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class UserEntitiy {

    @Id
    @Column(name = "user_id")
    private final String userId = UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "passwords")
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "feedbackEntityGiven", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FeedbackEntity> feedbacksGiven;

    @OneToMany(mappedBy = "feedbackEntityReceived", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FeedbackEntity> feedbackEntities;

}

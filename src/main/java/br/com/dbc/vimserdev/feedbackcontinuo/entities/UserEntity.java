package br.com.dbc.vimserdev.feedbackcontinuo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class UserEntity implements UserDetails {

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
    private byte[] profileImage;

    @JsonIgnore
    @OneToMany(mappedBy = "feedbackEntityGiven", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FeedbackEntity> feedbacksGiven;

    @JsonIgnore
    @OneToMany(mappedBy = "feedbackEntityReceived", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FeedbackEntity> feedbackEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

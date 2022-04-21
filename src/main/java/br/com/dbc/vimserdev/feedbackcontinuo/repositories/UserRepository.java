package br.com.dbc.vimserdev.feedbackcontinuo.repositories;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntitiy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntitiy, String> {

    boolean findByEmail(String email);
    Optional<UserEntitiy> findByEmailAndPassword(String email, String password);
}

package br.com.dbc.vimserdev.feedbackcontinuo.repositories;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Optional<TagEntity> findByName(String name);
}

package br.com.dbc.vimserdev.feedbackcontinuo.repositories;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, String> {

    Page<FeedbackEntity> findByUserId(Pageable pageable, String id);

    Page<FeedbackEntity> findByFeedbackUserId(Pageable pageable, String id);
}

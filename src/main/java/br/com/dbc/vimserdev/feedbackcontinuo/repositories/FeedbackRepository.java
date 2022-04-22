package br.com.dbc.vimserdev.feedbackcontinuo.repositories;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, String> {
}

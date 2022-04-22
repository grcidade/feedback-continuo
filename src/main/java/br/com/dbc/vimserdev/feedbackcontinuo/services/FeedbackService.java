package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final ObjectMapper mapper;

    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws BusinessRuleException {
        UserEntity user = userService.getLoged();

        UserEntity received = userService.getReceveidUser(createDTO.getFeedbackUserId());
        if (createDTO.getIsAnonymous() == null) {
            createDTO.setIsAnonymous(false);
        }

        FeedbackEntity entity = mapper.convertValue(createDTO, FeedbackEntity.class);
        entity.setFeedbackEntityGiven(user);
        entity.setUserId(user.getUserId());
        entity.setFeedbackEntityReceived(received);

        FeedbackEntity created = feedbackRepository.save(entity);

        FeedbackDTO createdDTO = mapper.convertValue(created, FeedbackDTO.class);
        createdDTO.setUserId(user.getUserId());

        return createdDTO;
    }

    public List<FeedbackCompleteDTO> getReceivedFeedbacks() throws BusinessRuleException {
        UserEntity user = userService.getLoged();

        return user.getFeedbackEntities().stream()
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getUserId());
                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(gived.getProfileImage())
                                .message(feedback.getMessage())
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    public List<FeedbackCompleteDTO> getGivedFeedbacks() throws BusinessRuleException {
        UserEntity user = userService.getLoged();

        return user.getFeedbacksGiven().stream()
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getFeedbackUserId());
                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(gived.getProfileImage())
                                .message(feedback.getMessage())
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.EmailHandlerDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;
    private final TagService tagService;
    private final ObjectMapper mapper;

    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws BusinessRuleException, JsonProcessingException {
        UserEntity user = userService.getLogedUserEntity();

        if (createDTO.getFeedbackUserId().equals(user.getUserId())) {
            throw new BusinessRuleException("Não é possível atribuir feedbacks a si mesmo.", HttpStatus.UNAUTHORIZED);
        }

        UserEntity received = userService.getUserById(createDTO.getFeedbackUserId());

        Set<TagEntity> tags = new HashSet<>();
        createDTO.getTags().forEach(tag -> {
            TagEntity tagEntity = tagService.findTag(tag);
            tags.add(tagEntity);
        });

        FeedbackEntity entity = FeedbackEntity.builder()
                .message(createDTO.getMessage())
                .isAnonymous(createDTO.getIsAnonymous() == null ? false : createDTO.getIsAnonymous())
                .tags(tags)
                .userId(user.getUserId())
                .feedbackUserId(received.getUserId())
                .feedbackEntityGiven(user)
                .feedbackEntityReceived(received)
                .build();

        FeedbackEntity created = feedbackRepository.save(entity);

        // Kafka
        EmailHandlerDTO emailHandlerDTO = EmailHandlerDTO.builder()
                .to(received.getEmail())
                .from(user.getName())
                .build();

        kafkaProducerService.send(emailHandlerDTO);

        FeedbackDTO createdDTO = mapper.convertValue(created, FeedbackDTO.class);
        createdDTO.setUserId(user.getUserId());
        createdDTO.setTags(createDTO.getTags());
        return createdDTO;
    }

    public Page<FeedbackCompleteDTO> getReceivedFeedbacks(Integer page) throws BusinessRuleException {
        UserEntity user = userService.getLogedUserEntity();

        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "createdAt");

        return feedbackRepository.findByFeedbackUserId(pageable, user.getUserId())
                .map(feedback -> {
                    try {
                        UserEntity receveid = userService.getUserById(feedback.getUserId());

                        String profileImage;
                        if (receveid.getProfileImage() == null) {
                            profileImage = null;
                        } else {
                            profileImage = Base64.getEncoder().encodeToString(receveid.getProfileImage());
                        }

                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(feedback.getIsAnonymous() ? "Anônimo" : receveid.getName())
                                .profileUserImage(feedback.getIsAnonymous() ? null : profileImage)
                                .message(feedback.getMessage())
                                .tags(getTags(feedback.getTags()))
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Page<FeedbackCompleteDTO> getGivedFeedbacks(Integer page) throws BusinessRuleException {
        UserEntity user = userService.getLogedUserEntity();

        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "createdAt");

        return feedbackRepository.findByUserId(pageable, user.getUserId())
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getFeedbackUserId());

                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(gived.getProfileImage() == null ? null : Base64.getEncoder().encodeToString(gived.getProfileImage()))
                                .message(feedback.getMessage())
                                .tags(getTags(feedback.getTags()))
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private List<String> getTags(Set<TagEntity> tags) {
        List<String> tagsList = new ArrayList<>();
        tags.forEach(tagsEntity -> {
            String tag = tagsEntity.getName().toUpperCase().replace(" ", "_");
            tagsList.add(tag);
        });
        return tagsList;
    }
}

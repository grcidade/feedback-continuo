package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.enums.Tags;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final TagService tagService;
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

        Set<TagEntity> tags = new HashSet<>();
        createDTO.getTags().forEach(tag -> {
            TagEntity tagEntity = tagService.findTag(tag);
            tags.add(tagEntity);
        });

        entity.setTags(tags);

        FeedbackEntity created = feedbackRepository.save(entity);

        FeedbackDTO createdDTO = mapper.convertValue(created, FeedbackDTO.class);
        createdDTO.setUserId(user.getUserId());
        createdDTO.setTags(createDTO.getTags());

        return createdDTO;
    }

    public List<FeedbackCompleteDTO> getReceivedFeedbacks() throws BusinessRuleException {
        UserEntity user = userService.getLoged();

        return user.getFeedbackEntities().stream()
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getUserId());
                        // TODO - arrumar essa monstruosidade
                        List<String> tags = new ArrayList<>();
                        feedback.getTags().forEach(tagsEntity -> {
                            String tag = tagsEntity.getName().toUpperCase().replace(" ", "_");
                            tags.add(tag);
                        });

                        if (feedback.getIsAnonymous()) {
                            UserEntity anonymous = userService.getUserById("aadebf96-ea3c-4719-b6d2-f38f50ab9cf6");
                            gived.setName(anonymous.getName());
                            gived.setProfileImage(anonymous.getProfileImage());
                        }

                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(gived.getProfileImage())
                                .message(feedback.getMessage())
                                .tags(tags)
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
                        // TODO - arrumar essa monstruosidade
                        List<String> tags = new ArrayList<>();
                        feedback.getTags().forEach(tagsEntity -> {
                            String tag = tagsEntity.getName().toUpperCase().replace(" ", "_");
                            tags.add(tag);
                        });

                        if (feedback.getIsAnonymous()) {
                            UserEntity anonymous = userService.getUserById("aadebf96-ea3c-4719-b6d2-f38f50ab9cf6");
                            gived.setName(anonymous.getName());
                            gived.setProfileImage(anonymous.getProfileImage());
                        }

                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(gived.getProfileImage())
                                .message(feedback.getMessage())
                                .tags(tags)
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}

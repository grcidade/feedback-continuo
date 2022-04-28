package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ObjectMapper mapper;

    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws BusinessRuleException {
        UserEntity user = userService.getLogedUserEntity();

        UserEntity received = userService.getUserById(createDTO.getFeedbackUserId());

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

    public Page<FeedbackCompleteDTO> getReceivedFeedbacks(Integer page) {
        UserEntity user = userService.getLogedUserEntity();

        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "createdAt");

        return feedbackRepository.findByFeedbackUserId(pageable, user.getUserId())
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getUserId());
                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(feedback.getIsAnonymous()?"Anônimo":gived.getName())
                                .profileUserImage(feedback.getIsAnonymous()?null: Base64.getEncoder().encodeToString(gived.getProfileImage()))
                                .message(feedback.getMessage())
                                .tags(getTags(feedback.getTags()))
                                .createdAt(feedback.getCreatedAt())
                                .build();
                    } catch (BusinessRuleException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Page<FeedbackCompleteDTO> getGivedFeedbacks(Integer page) {
        UserEntity user = userService.getLogedUserEntity();

        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "createdAt");

        return feedbackRepository.findByUserId(pageable, user.getUserId())
                .map(feedback -> {
                    try {
                        UserEntity gived = userService.getUserById(feedback.getFeedbackUserId());

                        return FeedbackCompleteDTO.builder()
                                .feedbackId(feedback.getFeedbackId())
                                .userName(gived.getName())
                                .profileUserImage(Base64.getEncoder().encodeToString(gived.getProfileImage()))
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

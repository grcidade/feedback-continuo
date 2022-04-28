package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.TagDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.enums.Tags;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ObjectMapper mapper;

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream().map(tag -> mapper.convertValue(tag, TagDTO.class)).toList();
    }

    public TagEntity findTag(Tags tag) {
        Optional<TagEntity> searched = tagRepository.findByName(tag.getName());
        if (searched.isEmpty()) {
            return tagRepository.saveAndFlush(TagEntity.builder().name(tag.getName()).build());
        }
        return searched.get();
    }
}

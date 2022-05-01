package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.enums.Tags;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.TagRepository;
import br.com.dbc.vimserdev.feedbackcontinuo.services.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    public void deveSalvarTagNoBanco(){

        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());

        tagService.findTag(Tags.AGILE);

        verify(tagRepository, times(1)).saveAndFlush(any(TagEntity.class));

    }

    @Test
    public void deveDevolverATag(){

        TagEntity tag = TagEntity.builder()
                .name("teste")
                .tagId(1)
                .build();

        when(tagRepository.findByName(anyString())).thenReturn(Optional.ofNullable(tag));

        TagEntity tagEntity = tagService.findTag(Tags.AGILE);

        assertEquals(tag, tagEntity);

    }

}

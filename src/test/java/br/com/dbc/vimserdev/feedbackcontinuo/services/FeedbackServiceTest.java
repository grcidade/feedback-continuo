package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.EmailHandlerDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.FeedbackEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.enums.Tags;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserService userService;

    @Mock
    private TagService tagService;

    @Mock
    private EmailHandlerProducerService emailHandlerProducerService;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test(expected = BusinessRuleException.class)
    public void deveDarErroFeedbackProprioUsuario() throws BusinessRuleException, JsonProcessingException {

        List<Tags> tags = Arrays.asList(Tags.JAVA, Tags.AWS, Tags.CSS);

        UserEntity user = getUserEntity();
        FeedbackCreateDTO feedbackCreateDTO = FeedbackCreateDTO.builder()
                .isAnonymous(false)
                .message("teste")
                .tags(tags)
                .feedbackUserId(user.getUserId()).build();

        when(userService.getLogedUserEntity()).thenReturn(user);

        feedbackService.create(feedbackCreateDTO);

    }

    @Test
    public void deveSalvarUsuario() throws BusinessRuleException, JsonProcessingException {

        List<Tags> tags = Arrays.asList(Tags.JAVA, Tags.AWS, Tags.CSS);

        UserEntity user = getUserEntity();
        FeedbackCreateDTO feedbackCreateDTO = FeedbackCreateDTO.builder()
                .isAnonymous(false)
                .message("teste")
                .tags(tags)
                .feedbackUserId("1").build();

        FeedbackDTO feedbackDTO = new FeedbackDTO();

        when(userService.getLogedUserEntity()).thenReturn(user);
        when(userService.getUserById(anyString())).thenReturn(user);
        when(tagService.findTag(any(Tags.class))).thenReturn(TagEntity.builder().build());
        when(mapper.convertValue(any(), eq(FeedbackDTO.class))).thenReturn(feedbackDTO);
        doNothing().when(emailHandlerProducerService).send(any(EmailHandlerDTO.class));

        feedbackService.create(feedbackCreateDTO);

        verify(feedbackRepository, times(1)).save(any(FeedbackEntity.class));

    }

    @Test
    public void deveMandarEmailSalvarUsuario() throws BusinessRuleException, JsonProcessingException {

        List<Tags> tags = Arrays.asList(Tags.JAVA, Tags.AWS, Tags.CSS);

        UserEntity user = getUserEntity();
        FeedbackCreateDTO feedbackCreateDTO = FeedbackCreateDTO.builder()
                .isAnonymous(false)
                .message("teste")
                .tags(tags)
                .feedbackUserId("1").build();

        FeedbackDTO feedbackDTO = new FeedbackDTO();

        when(userService.getLogedUserEntity()).thenReturn(user);
        when(userService.getUserById(anyString())).thenReturn(user);
        when(tagService.findTag(any(Tags.class))).thenReturn(TagEntity.builder().build());
        when(mapper.convertValue(any(), eq(FeedbackDTO.class))).thenReturn(feedbackDTO);

        feedbackService.create(feedbackCreateDTO);

        verify(emailHandlerProducerService, times(1)).send(any((EmailHandlerDTO.class)));

    }

    @Test
    public void deveTestarFeedbacksRecebidosUsuarios() throws BusinessRuleException {

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        Page<FeedbackEntity> feedbackEntityPage = new PageImpl(feedbackEntities);

        UserEntity user = getUserEntity();

        when(feedbackRepository.findByFeedbackUserId(any(), anyString())).thenReturn(feedbackEntityPage);
        when(userService.getLogedUserEntity()).thenReturn(user);

        feedbackService.getReceivedFeedbacks(0);

        verify(feedbackRepository, times(1)).findByFeedbackUserId(any(), anyString());
        assertEquals(feedbackEntityPage, feedbackRepository.findByFeedbackUserId(any(), anyString()));

    }

    @Test
    public void deveTestarFeedbacksDadosUsuarios() throws BusinessRuleException {

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        Page<FeedbackEntity> feedbackEntityPage = new PageImpl(feedbackEntities);

        UserEntity user = getUserEntity();

        when(feedbackRepository.findByUserId(any(), anyString())).thenReturn(feedbackEntityPage);
        when(userService.getLogedUserEntity()).thenReturn(user);

        feedbackService.getGivedFeedbacks(0);

        verify(feedbackRepository, times(1)).findByUserId(any(), anyString());
        assertEquals(feedbackEntityPage, feedbackRepository.findByUserId(any(), anyString()));

    }

    private UserEntity getUserEntity(){
        return UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password("Senha@123")
                .name("Joao")
                .profileImage(null).build();
    }

    private FeedbackCompleteDTO getFeedbackCompleto(){
        return FeedbackCompleteDTO.builder()
                .feedbackId("1")
                .userName("a")
                .profileUserImage(null)
                .message("a")
                .tags(null)
                .createdAt(LocalDateTime.now())
                .build();
    }


}

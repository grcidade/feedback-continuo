package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ForgotPasswordHandlerDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ForgotPasswordHandlerProducerService passwordHandlerProducerService;

    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);


    @InjectMocks
    private UserService userService;

    @Before
    public void init(){
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");
    }


    @Test
    public void deveDarErroEmail() {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("joao@gmail.com")
                .build();

        try{
            userService.create(userCreateDTO, null);
        }catch (BusinessRuleException e){
            assertEquals("Email inválido ou já existente.", e.getMessage());
        }
    }

    @Test
    public void deveDarErroSenha() {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("a@dbccompany.com.br")
                .password("1234")
                .build();

        try{
            userService.create(userCreateDTO, null);
        }catch (BusinessRuleException e){
            assertEquals("Senha fraca demais.", e.getMessage());
        }
    }

    @Test
    public void deveTestarCriacaoUsuario() throws BusinessRuleException {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("joao@dbccompany.com.br")
                .name("joao")
                .password("Senha@123")
                .build();

        userService.create(userCreateDTO, null);

        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    public void deveDarErroSenhaIncompativel(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged("Senha@1", "123");
        }catch (BusinessRuleException e){
            assertEquals("Senha antiga incompatível.", e.getMessage());
        }
    }

    @Test
    public void deveDarErroSenhaFraca(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged("Senha@123", "123");
        }catch (BusinessRuleException e){
            assertEquals("Senha fraca demais", e.getMessage());
        }
    }

    @Test
    public void deveDarErroSenhaAtualIgual(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged("Senha@123", "Senha@123");
        }catch (BusinessRuleException e){
            assertEquals("Essa já é sua senha atual!", e.getMessage());
        }
    }

    @Test
    public void deveChamarSalvarNoTrocarSenha() throws BusinessRuleException {

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        userService.changePasswordUserLoged("Senha@123", "Senha@12345");

        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void deveDarErroEmailMetodoEsqueciSenha() throws JsonProcessingException {
        try{
            userService.forgotPassword("joao@dbccompany.com");
        }catch (BusinessRuleException e){
            assertEquals("Email inválido.",e.getMessage());
        }
    }

    @Test
    public void deveDarErroUsuarioNaoEncontradoMetodoEsqueciSenha() throws JsonProcessingException {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try{
            userService.forgotPassword("joao@dbccompany.com.br");
        }catch (BusinessRuleException e){
            assertEquals("Usuário não encontrado.",e.getMessage());
        }
    }

    @Test
    public void deveChamarSalvarMetodoEsqueciSenha() throws BusinessRuleException, JsonProcessingException {

        UserEntity user = UserEntity.builder()
                .email("joao@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        doNothing().when(passwordHandlerProducerService).send(any(ForgotPasswordHandlerDTO.class));

        userService.forgotPassword("joao@dbccompany.com.br");

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void deveChamarProducerServiceMetodoEsqueciSenha() throws BusinessRuleException, JsonProcessingException {

        UserEntity user = UserEntity.builder()
                .email("joao@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.forgotPassword("joao@dbccompany.com.br");

        verify(passwordHandlerProducerService, times(1)).send(any(ForgotPasswordHandlerDTO.class));
    }

    @Test
    public void deveTestarChamadaFindByEmail(){
        userService.findByEmail(anyString());
        verify(userRepository,times(1)).findByEmail(anyString());
    }

    @Test
    public void deveTestarChamadaAuthentication(){

        SecurityContextHolder.setContext(securityContext);

        userService.getLogedUserId();
        verify(authentication,times(1)).getPrincipal();
    }

    @Test
    public void deveChamarFindAllByUserSemUserLogado(){

        SecurityContextHolder.setContext(securityContext);

        userRepository.findAllByUserIdIsNot(anyString());

        verify(userRepository, times(1)).findAllByUserIdIsNot(anyString());

    }

    @Test
    public void deveDevolverNullUserLogado(){
        SecurityContextHolder.setContext(securityContext);
        assertNull(userService.getLogedUser());
    }

}

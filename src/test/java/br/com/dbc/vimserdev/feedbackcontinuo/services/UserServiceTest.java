package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ChangePasswordDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ForgotPasswordHandlerDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;
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
    public void shouldGiveErrorEmail() {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("joao@dbccompany.com")
                .build();

        try{
            userService.create(userCreateDTO, null);
        }catch (BusinessRuleException e){
            assertEquals("Email inválido ou já existente.", e.getMessage());
        }
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldGiveErrorPassword() throws BusinessRuleException {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("a@dbccompany.com.br")
                .password("64789510Ae")
                .build();

            userService.create(userCreateDTO, null);

    }

    @Test
    public void shouldTestCreationUser() throws BusinessRuleException {

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .email("joao@dbccompany.com.br")
                .name("joao")
                .password("64789510Ae!")
                .build();

        userService.create(userCreateDTO, null);

        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    public void shouldGiveErrorPasswordIncompatible(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        ChangePasswordDTO passwordDTO = ChangePasswordDTO.builder()
                .oldPassword("Senha@1")
                .newPassword("123")
                .build();


        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged(passwordDTO);
        }catch (BusinessRuleException e){
            assertEquals("Senha antiga incompatível.", e.getMessage());
        }
    }

    @Test
    public void shouldGiveErrorWeakPassword(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        ChangePasswordDTO passwordDTO = ChangePasswordDTO.builder()
                .oldPassword("Senha@123")
                .newPassword("123")
                .build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged(passwordDTO);
        }catch (BusinessRuleException e){
            assertEquals("Senha fraca demais", e.getMessage());
        }
    }

    @Test
    public void shouldGiveErrorCurrentPasswordEquals(){

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        ChangePasswordDTO passwordDTO = ChangePasswordDTO.builder()
                .oldPassword("Senha@123")
                .newPassword("Senha@123")
                .build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        try{
            userService.changePasswordUserLoged(passwordDTO);
        }catch (BusinessRuleException e){
            assertEquals("Essa já é sua senha atual!", e.getMessage());
        }
    }

    @Test
    public void shouldCallSaveInChangePassword() throws BusinessRuleException {

        UserEntity user = UserEntity.builder()
                .email("a@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        ChangePasswordDTO passwordDTO = ChangePasswordDTO.builder()
                .oldPassword("Senha@123")
                .newPassword("Senha@12345")
                .build();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        userService.changePasswordUserLoged(passwordDTO);

        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void shouldGiveErrorEmailMethodForgotPassword() throws JsonProcessingException {
        try{
            userService.forgotPassword("joao@dbccompany.com");
        }catch (BusinessRuleException e){
            assertEquals("Email inválido.",e.getMessage());
        }
    }

    @Test
    public void shouldGiveErrorUserNotFoundMethodForgotPassword() throws JsonProcessingException {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try{
            userService.forgotPassword("joao@dbccompany.com.br");
        }catch (BusinessRuleException e){
            assertEquals("Usuário não encontrado.",e.getMessage());
        }
    }

    @Test
    public void shouldCallSaveMethodForgotPassword() throws BusinessRuleException, JsonProcessingException {

        UserEntity user = UserEntity.builder()
                .email("joao@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        doNothing().when(passwordHandlerProducerService).send(any(ForgotPasswordHandlerDTO.class));

        userService.forgotPassword("joao@dbccompany.com.br");

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void shouldCallProducerServiceMethodForgotPassword() throws BusinessRuleException, JsonProcessingException {

        UserEntity user = UserEntity.builder()
                .email("joao@dbccompany.com.br")
                .password(new BCryptPasswordEncoder().encode("Senha@123")).build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.forgotPassword("joao@dbccompany.com.br");

        verify(passwordHandlerProducerService, times(1)).send(any(ForgotPasswordHandlerDTO.class));
    }

    @Test
    public void shouldTestCallFindByEmail(){
        userService.findByEmail(anyString());
        verify(userRepository,times(1)).findByEmail(anyString());
    }

    @Test
    public void shouldTestCallAuthentication(){

        SecurityContextHolder.setContext(securityContext);

        userService.getLogedUserId();
        verify(authentication,times(1)).getPrincipal();
    }

    @Test
    public void shouldCallFindAllByUserSemUserLoggedIn(){

        SecurityContextHolder.setContext(securityContext);

        userRepository.findAllByUserIdIsNot(anyString());

        verify(userRepository, times(1)).findAllByUserIdIsNot(anyString());

    }

    @Test
    public void shouldReturnNullUserLoggedIn(){
        SecurityContextHolder.setContext(securityContext);
        assertNull(userService.getLogedUser());
    }

    @Test
    public void shouldReturnUserLoggedIn(){

        UserEntity user = UserEntity.builder()
                .name("joao")
                .password("Senha@123")
                .email("a@dbccompany.com.br").build();
        UserDTO userPassed = buildUserDTO(user);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getLogedUser();

        assertEquals(userPassed.getUserId(), userDTO.getUserId());

    }

    @Test
    public void shouldTestSwapImage() throws BusinessRuleException {

        SecurityContextHolder.setContext(securityContext);

        UserEntity user = UserEntity.builder()
                .name("joao")
                .password("Senha@123")
                .email("a@dbccompany.com.br").build();

        when(userRepository.getById(anyString())).thenReturn(user);

        userService.changeProfileImageUserLoged(getImagem());

        verify(userRepository,times(1)).save(any(UserEntity.class));

    }

    @Test(expected = BusinessRuleException.class)
    public void shouldGiveErrorFileTypeSwapImage() throws BusinessRuleException {

        SecurityContextHolder.setContext(securityContext);

        UserEntity user = UserEntity.builder()
                .name("joao")
                .password("Senha@123")
                .email("a@dbccompany.com.br").build();

        when(userRepository.getById(anyString())).thenReturn(user);

        userService.changeProfileImageUserLoged(getImagemRuim());

    }

    private UserDTO buildUserDTO(UserEntity userToTransform){
        return UserDTO.builder()
                .userId(userToTransform.getUserId())
                .email(userToTransform.getEmail())
                .name(userToTransform.getName())
                .profileImage(userToTransform.getProfileImage() != null ? Base64.getEncoder().encodeToString(userToTransform.getProfileImage()) : null).build();
    }

    private MultipartFile getImagem(){

        return new MultipartFile() {
            @Override
            public String getName() {
                return "abd";
            }

            @Override
            public String getOriginalFilename() {
                return "abd.jpeg";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IllegalStateException {

            }
        };

    }

    private MultipartFile getImagemRuim(){

        return new MultipartFile() {
            @Override
            public String getName() {
                return "abd.pdf";
            }

            @Override
            public String getOriginalFilename() {
                return "abd.pdf";
            }

            @Override
            public String getContentType() {
                return "pdf";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IllegalStateException {

            }
        };

    }

}

package br.com.dbc.vimserdev.feedbackcontinuo;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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

}

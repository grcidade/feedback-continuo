package br.com.dbc.vimserdev.feedbackcontinuo.security;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

//    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        Optional<LoginEntity> optionalLogin = loginService.findByLogin(login);
//
//        if (optionalLogin.isPresent()) {
//            return optionaLogin.get();
//        }

        throw new UsernameNotFoundException("Usuário não encontrado!");
    }
}
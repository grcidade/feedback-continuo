package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntitiy;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public UserDTO create(UserCreateDTO userCreateDTO) throws BusinessRuleException {
        if (!isValidEmail(userCreateDTO.getEmail()) || userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email inválido ou já existente.");
        }

        UserEntitiy entity = mapper.convertValue(userCreateDTO, UserEntitiy.class);
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));

        UserEntitiy created = userRepository.save(entity);

        return mapper.convertValue(created, UserDTO.class);
    }

    public Optional<UserEntitiy> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidEmail(String email) {
        String[] partitioned = email.split("@");
        return partitioned[1].equals("dbccompany.com.br");
    }

    public String getLogedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}

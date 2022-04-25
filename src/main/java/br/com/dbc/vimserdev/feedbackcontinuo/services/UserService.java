package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public UserDTO create(UserCreateDTO userCreateDTO) throws BusinessRuleException {
        if (!isValidEmail(userCreateDTO.getEmail()) || userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email inválido ou já existente.");
        }
        if(!isValidPassword(userCreateDTO.getPassword())){
            throw new BusinessRuleException("Senha inválida");
        }

        if (userCreateDTO.getProfileImage() == null) {
            UserEntity anonymous = getUserById("aadebf96-ea3c-4719-b6d2-f38f50ab9cf6");
            userCreateDTO.setProfileImage(anonymous.getProfileImage());
        }

        UserEntity entity = mapper.convertValue(userCreateDTO, UserEntity.class);
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));

        UserEntity created = userRepository.save(entity);

        return mapper.convertValue(created, UserDTO.class);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String getLogedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    public List<UserDTO> getAllUsersExceptLogedUser(){
        return userRepository.findAllByUserIdIsNotAndUserIdIsNot(getLogedUserId(), "aadebf96-ea3c-4719-b6d2-f38f50ab9cf6")
                .stream().map(userEntitiy -> mapper.convertValue(userEntitiy, UserDTO.class)).toList();
    }

    public UserDTO getLogedUser() throws BusinessRuleException {
        String userIdLoged = getLogedUserId();
        Optional<UserDTO> userLoged = userRepository.findById(userIdLoged).map(userEntity -> mapper.convertValue(userEntity, UserDTO.class));
        if(userLoged.isPresent()){
            return userLoged.get();
        }else {
            throw new BusinessRuleException("Ninguém logado");
        }
    }

    protected UserEntity getReceveidUser(String id) throws BusinessRuleException {
        UserEntity loged = getLogedUserEntity();
        UserEntity target = userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Usuário não econtrado"));
        if (loged.equals(target)) {
            throw new BusinessRuleException("Não é possível atribuir feedbacks a si mesmo.");
        }
        return target;
    }

    protected UserEntity getLogedUserEntity() throws BusinessRuleException {
        String userIdLoged = getLogedUserId();
        Optional<UserEntity> userLoged = userRepository.findById(userIdLoged);
        if(userLoged.isPresent()){
            return userLoged.get();
        }else {
            throw new BusinessRuleException("Ninguém logado");
        }
    }

    protected UserEntity getUserById(String id) throws BusinessRuleException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Id inválido"));
    }

    private boolean isValidEmail(String email) {
        String[] partitioned = email.split("@");
        return partitioned[1].equals("dbccompany.com.br");
    }

    private boolean isValidPassword(String password) {

        // Tamanho: Min 8 - Max 20;
        // 1 letra minúscula;
        // 1 letra maiúscula;
        // 1 caracter especial;
        // 1 digito;
        Pattern regexToValidThePassword = Pattern.compile("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$");

        Matcher testPasswordWithRegex = regexToValidThePassword.matcher(password);

        return testPasswordWithRegex.matches();
    }
}

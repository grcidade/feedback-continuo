package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UpdateUserImageProfileDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public void create(UserCreateDTO userCreateDTO) throws BusinessRuleException {
        if (!isValidEmail(userCreateDTO.getEmail()) || userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email inválido ou já existente.", HttpStatus.UNAUTHORIZED);
        }
        if(!isValidPassword(userCreateDTO.getPassword())){
            throw new BusinessRuleException("Senha fraca demais", HttpStatus.BAD_REQUEST);
        }

        if (userCreateDTO.getProfileImage() == null) {
            UserEntity anonymous = getUserById("aadebf96-ea3c-4719-b6d2-f38f50ab9cf6");
            userCreateDTO.setProfileImage(anonymous.getProfileImage());
        }

        UserEntity entity = mapper.convertValue(userCreateDTO, UserEntity.class);
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));

        UserEntity created = userRepository.save(entity);

        mapper.convertValue(created, UserDTO.class);
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

    public UserDTO getLogedUser() {
        String userIdLoged = getLogedUserId();
        Optional<UserDTO> userLoged = userRepository.findById(userIdLoged).map(userEntity -> mapper.convertValue(userEntity, UserDTO.class));
        return userLoged.orElse(null);
    }

    public void changePasswordUserLoged(String newPassword) throws BusinessRuleException {
        if(!isValidPassword(newPassword)){
            throw new BusinessRuleException("Senha fraca demais", HttpStatus.BAD_REQUEST);
        }
        UserEntity userToUpdate = getLogedUserEntity();
        if(new BCryptPasswordEncoder().matches(newPassword, userToUpdate.getPassword())){
            throw new BusinessRuleException("Essa já é sua senha atual!", HttpStatus.BAD_REQUEST);
        }
        userToUpdate.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(userToUpdate);
    }

    public void changeProfileImageUserLoged(String newProfileImage) {
        Optional<UserEntity> userToUpdate = userRepository.findById(getLogedUserId());
        if (userToUpdate.isPresent()) {
        	UserEntity user = userToUpdate.get();
        	user.setProfileImage(newProfileImage);
        	userRepository.save(user);
        }
    }

    protected UserEntity getLogedUserEntity() {
        String userIdLoged = getLogedUserId();
        Optional<UserEntity> userLoged = userRepository.findById(userIdLoged);
        return userLoged.orElse(null);
    }

    protected UserEntity getUserById(String id) throws BusinessRuleException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Usuário não encontrado", HttpStatus.NOT_FOUND));
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

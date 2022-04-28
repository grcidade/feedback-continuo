package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(UserCreateDTO userCreateDTO, MultipartFile profileImage) throws BusinessRuleException {
        if (!isValidEmail(userCreateDTO.getEmail()) || userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email inválido ou já existente.", HttpStatus.UNAUTHORIZED);
        }
        if(!isValidPassword(userCreateDTO.getPassword())){
            throw new BusinessRuleException("Senha fraca demais", HttpStatus.BAD_REQUEST);
        }

        UserEntity userCreated = UserEntity.builder()
                .name(userCreateDTO.getName())
                .email(userCreateDTO.getEmail())
                .password(new BCryptPasswordEncoder().encode(userCreateDTO.getPassword()))
                .profileImage(profileImage!=null?convertImageToByte(profileImage):null).build();

        userRepository.save(userCreated);
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
        return userRepository.findAllByUserIdIsNot(getLogedUserId())
                .stream().map(this::buildUserDTO).toList();
    }

    public UserDTO getLogedUser() {
        String userIdLoged = getLogedUserId();
        Optional<UserDTO> userLoged = userRepository.findById(userIdLoged).map(this::buildUserDTO);
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

    public void changeProfileImageUserLoged(MultipartFile newProfileImage) throws BusinessRuleException {
        UserEntity userToUpdate = userRepository.getById(getLogedUserId());
        userToUpdate.setProfileImage(convertImageToByte(newProfileImage));
        userRepository.save(userToUpdate);
    }

    protected UserEntity getLogedUserEntity() {
        String userIdLoged = getLogedUserId();
        Optional<UserEntity> userLoged = userRepository.findById(userIdLoged);
        return userLoged.orElse(null);
    }

    protected UserEntity getUserById(String id) throws BusinessRuleException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    private UserDTO buildUserDTO(UserEntity userToTransform){
        return UserDTO.builder()
                .userId(userToTransform.getUserId())
                .email(userToTransform.getEmail())
                .name(userToTransform.getName())
                .profileImage(userToTransform.getProfileImage()!=null? Base64.getEncoder().encodeToString(userToTransform.getProfileImage()):null).build();
    }

    private byte[] convertImageToByte(MultipartFile profileImage) throws BusinessRuleException {
        try {
            String fileExtension = FilenameUtils.getExtension(profileImage.getOriginalFilename());
            if (!Arrays.asList("jpg", "jpeg", "png").contains(fileExtension)) {
                throw new BusinessRuleException("Tipo de arquivo não suportado", HttpStatus.NOT_ACCEPTABLE);
            }
            return profileImage.getBytes();
        }catch (IOException e){
            throw new BusinessRuleException("Erro ao salvar imagem", HttpStatus.EXPECTATION_FAILED);
        }
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

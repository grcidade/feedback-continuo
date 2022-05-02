package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ChangePasswordDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ForgotPasswordHandlerDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.entities.UserEntity;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ForgotPasswordHandlerProducerService handlerProducerService;

    public void create(UserCreateDTO userCreateDTO, MultipartFile profileImage) throws BusinessRuleException {
        if (!isValidEmail(userCreateDTO.getEmail()) || userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email inválido ou já existente.", HttpStatus.UNAUTHORIZED);
        }

        if(!isValidPassword(userCreateDTO.getPassword())){
            throw new BusinessRuleException("Senha fraca demais.", HttpStatus.BAD_REQUEST);
        }

        UserEntity created = UserEntity.builder()
                .name(userCreateDTO.getName())
                .email(userCreateDTO.getEmail())
                .password(new BCryptPasswordEncoder().encode(userCreateDTO.getPassword()))
                .profileImage(profileImage != null ? convertImageToByte(profileImage) : null).build();

        userRepository.save(created);
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

    public void changePasswordUserLoged(ChangePasswordDTO changePasswordDTO) throws BusinessRuleException {
        UserEntity userToUpdate = getLogedUserEntity();

        if (!new BCryptPasswordEncoder().matches(changePasswordDTO.getOldPassword(), userToUpdate.getPassword())) {
            throw new BusinessRuleException("Senha antiga incompatível.", HttpStatus.UNAUTHORIZED);
        }

        if(!isValidPassword(changePasswordDTO.getNewPassword())){
            throw new BusinessRuleException("Senha fraca demais", HttpStatus.BAD_REQUEST);
        }

        if(new BCryptPasswordEncoder().matches(changePasswordDTO.getNewPassword(), userToUpdate.getPassword())){
            throw new BusinessRuleException("Essa já é sua senha atual!", HttpStatus.BAD_REQUEST);
        }

        userToUpdate.setPassword(new BCryptPasswordEncoder().encode(changePasswordDTO.getNewPassword()));
        userRepository.save(userToUpdate);
    }

    public void changeProfileImageUserLoged(MultipartFile newProfileImage) throws BusinessRuleException {
        UserEntity userToUpdate = userRepository.getById(getLogedUserId());
        userToUpdate.setProfileImage(convertImageToByte(newProfileImage));
        userRepository.save(userToUpdate);
    }

    public void forgotPassword(String email) throws BusinessRuleException, JsonProcessingException {
        if (!isValidEmail(email)) {
            throw new BusinessRuleException("Email inválido.", HttpStatus.BAD_REQUEST);
        }

        Optional<UserEntity> userExists = findByEmail(email);

        if (userExists.isEmpty()) {
            throw new BusinessRuleException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        String code = UUID.randomUUID().toString();

        UserEntity user = userExists.get();
        user.setPassword(new BCryptPasswordEncoder().encode(code));

        userRepository.save(user);

        ForgotPasswordHandlerDTO handlerDTO = ForgotPasswordHandlerDTO.builder()
                .to(user.getEmail())
                .code(code)
                .build();

        handlerProducerService.send(handlerDTO);
    }

    protected UserEntity getLogedUserEntity() throws BusinessRuleException {
        return userRepository.findById(getLogedUserId()).orElseThrow(() -> new BusinessRuleException("Erro inesperado.", HttpStatus.BAD_REQUEST));
    }

    protected UserEntity getUserById(String id) throws BusinessRuleException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    private UserDTO buildUserDTO(UserEntity userToTransform){
        return UserDTO.builder()
                .userId(userToTransform.getUserId())
                .email(userToTransform.getEmail())
                .name(userToTransform.getName())
                .profileImage(userToTransform.getProfileImage() != null ? Base64.getEncoder().encodeToString(userToTransform.getProfileImage()) : null).build();
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
        if (password.length() < 6) return false;

        boolean findNumber = false;
        boolean findUpperCase = false;
        boolean findLowerCase = false;
        boolean findSymbol = false;
        for (char letter : password.toCharArray()) {
            if (letter >= '0' && letter <= '9') {
                findNumber = true;
            } else if (letter >= 'A' && letter <= 'Z') {
                findUpperCase = true;
            } else if (letter >= 'a' && letter <= 'z') {
                findLowerCase = true;
            } else {
                findSymbol = true;
            }
        }
        return findNumber && findUpperCase && findLowerCase && findSymbol;
    }
}

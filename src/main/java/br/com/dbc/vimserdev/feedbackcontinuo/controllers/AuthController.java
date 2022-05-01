package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.LoginDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation.AuthAPI;
import br.com.dbc.vimserdev.feedbackcontinuo.security.TokenService;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Api(value = "Auth")
public class AuthController implements AuthAPI {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                );

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = tokenService.getToken(authenticate);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PostMapping(path = "/sign-up", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> register(@Valid @ModelAttribute UserCreateDTO userCreateDTO, @RequestPart(required = false) MultipartFile profileImage) throws BusinessRuleException {
        userService.create(userCreateDTO, profileImage);
        LoginDTO login = new LoginDTO();
        login.setEmail(userCreateDTO.getEmail());
        login.setPassword(userCreateDTO.getPassword());
        return login(login);
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassowrd(@PathVariable("email") String email) throws BusinessRuleException, JsonProcessingException {
        userService.forgotPassword(email);
        return new ResponseEntity<>("Foi enviado um código para o seu email.", HttpStatus.OK);
    }
}

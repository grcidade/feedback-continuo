package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

}

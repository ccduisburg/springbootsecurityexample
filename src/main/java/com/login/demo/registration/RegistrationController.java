package com.login.demo.registration;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping
    public String regitration(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
    @GetMapping(path="confirm")
    public String confirm(@RequestParam("token")String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping(path="paswordforgot")
    public String passwordforgot(@RequestParam("email")String email){
        return registrationService.passwordforgot(email);
    }
}
package com.login.demo.appuser;

import com.login.demo.registration.Loginrequest;
import com.login.demo.registration.token.ConfirmationToken;
import com.login.demo.registration.token.ConfirmationTokenRepository;
import com.login.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUD_MSG="user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUD_MSG)));
    }
    public String signUpUser(AppUser appUser){
        boolean userExists=userRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if(userExists){
            //TODO eger email confirm olmadiysa confirmation email gönder
            throw new IllegalStateException(" e mail already exist");
        }
       String encodedPassword= bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);

        String token= UUID.randomUUID().toString();
        ConfirmationToken confirmationToken= new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO:Send Email

         return token;
    }
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public String loginUser(Loginrequest loginrequest) {
        String token= UUID.randomUUID().toString();
        Optional<AppUser> userExists=userRepository.findByEmail(loginrequest.getEmail());

        if(!userExists.isPresent()){

            throw new IllegalStateException(" email or username not exist");
        }
        String encodedPassword= bCryptPasswordEncoder.encode(loginrequest.getPassword());
       if(userExists.get().getPassword().equals(encodedPassword)){

       }
       return token;
    }
}

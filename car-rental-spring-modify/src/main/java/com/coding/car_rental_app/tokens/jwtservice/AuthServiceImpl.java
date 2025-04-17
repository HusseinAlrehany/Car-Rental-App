package com.coding.car_rental_app.tokens.jwtservice;

import com.coding.car_rental_app.dtos.AuthenticationResponse;
import com.coding.car_rental_app.dtos.SignUpRequest;
import com.coding.car_rental_app.dtos.SigninRequest;
import com.coding.car_rental_app.dtos.UserDTO;
import com.coding.car_rental_app.entity.User;
import com.coding.car_rental_app.enums.UserRole;
import com.coding.car_rental_app.exceptions.ValidationException;
import com.coding.car_rental_app.mapper.Mapper;
import com.coding.car_rental_app.repository.UserRepository;
import com.coding.car_rental_app.tokens.utils.JwtUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    //to create admin account
    @PostConstruct
    public void createAdminAccount(){
        User adminUser = userRepository.findByUserRole(UserRole.ADMIN);
        if(null == adminUser){
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setName("admin");
            admin.setUserRole(UserRole.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));

            userRepository.save(admin);
        }
    }

    @Override
    public UserDTO signUp(SignUpRequest signUpRequest) {
        if(hasUserWithEmail(signUpRequest.getEmail())){
           throw new ValidationException("User Already Exists With This Email");
        }
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));

        User dbUser = userRepository.save(user);

        return mapper.getUserDTO(dbUser);
    }

    @Override
    public AuthenticationResponse signIn(SigninRequest signinRequest, HttpServletResponse httpServletResponse) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signinRequest.getEmail(),
                    signinRequest.getPassword()
            ));
        } catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Invalid username or password");
        }

        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(
                signinRequest.getEmail()
        );

        Optional<User> optionalUser = userRepository.findFirstByEmail(signinRequest.getEmail());
        final String jwtToken = jwtUtils.generateToken(userDetails);
        if (optionalUser.isEmpty()) {
              throw new UsernameNotFoundException("No User Found!");
        }

        //storing the JWT HttpOnly cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        AuthenticationResponse response = new AuthenticationResponse();
        response.setJwtToken(jwtToken);
        response.setUserId(optionalUser.get().getId());
        response.setUserRole(optionalUser.get().getUserRole());

        return response;
    }
    @Cacheable(value = "users", key = "#email")
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}

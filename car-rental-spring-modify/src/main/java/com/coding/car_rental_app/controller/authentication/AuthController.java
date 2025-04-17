package com.coding.car_rental_app.controller.authentication;

import com.coding.car_rental_app.apiresponse.ApiResponse;
import com.coding.car_rental_app.dtos.AuthenticationResponse;
import com.coding.car_rental_app.dtos.SignUpRequest;
import com.coding.car_rental_app.dtos.SigninRequest;
import com.coding.car_rental_app.dtos.UserDTO;
import com.coding.car_rental_app.repository.UserRepository;
import com.coding.car_rental_app.tokens.jwtservice.AppUserDetailsService;
import com.coding.car_rental_app.tokens.jwtservice.AuthService;
import com.coding.car_rental_app.tokens.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//the body should be JSON not plain text
//to avoid issues in frontend like
//(SyntaxError: JSON.parse: unexpected character at line 1 column 1 of the JSON data)
@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;


   @PostMapping("/signup")
   public ResponseEntity<ApiResponse<UserDTO>> signUp(@RequestBody SignUpRequest signUpRequest){
                UserDTO userDTO = authService.signUp(signUpRequest);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new ApiResponse<>("SignUp Successful"));
   }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninRequest signinRequest, HttpServletResponse httpServletResponse){
       AuthenticationResponse authenticationResponse = authService.signIn(signinRequest, httpServletResponse);
       return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

       try{

           ResponseCookie deletedCookie = ResponseCookie.from("jwt", "")
                   .httpOnly(true)
                   .secure(false)
                   .sameSite("Strict")
                   .path("/")
                   .maxAge(0)
                   .build();

           response.addHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());
           return ResponseEntity.ok().body(Map.of("message", "Logged Out Successfully"));
       } catch(Exception  ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("message", "Logout Failed!!"));
       }

    }

}

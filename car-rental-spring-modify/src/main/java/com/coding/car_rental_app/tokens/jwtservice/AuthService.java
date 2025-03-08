package com.coding.car_rental_app.tokens.jwtservice;
import com.coding.car_rental_app.dtos.AuthenticationResponse;
import com.coding.car_rental_app.dtos.SignUpRequest;
import com.coding.car_rental_app.dtos.SigninRequest;
import com.coding.car_rental_app.dtos.UserDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {


    UserDTO signUp(SignUpRequest signUpRequest);
    boolean hasUserWithEmail(String email);

    AuthenticationResponse signIn(SigninRequest signinRequest, HttpServletResponse httpServletResponse);

}

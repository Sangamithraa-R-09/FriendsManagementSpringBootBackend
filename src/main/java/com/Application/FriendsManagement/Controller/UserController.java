package com.Application.FriendsManagement.Controller;

import com.Application.FriendsManagement.DTO.*;

import com.Application.FriendsManagement.JwtUtil.JwtService;
import com.Application.FriendsManagement.Model.RefreshToken;
import com.Application.FriendsManagement.Model.Users;
import com.Application.FriendsManagement.Repo.UserRepo;
import com.Application.FriendsManagement.Service.RefreshTokenService;
import com.Application.FriendsManagement.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepo;

    private final UserService service;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterDTO request, final HttpServletRequest httpServletRequest
            ) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(service.register(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDTO> authenticate(
            @RequestBody LoginDTO request
    ){
        System.out.println("Controller method");
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<ResponseDTO> verifyEmail(@RequestParam("token") String verificationToken){
        return service.verifyEmail(verificationToken);
    }

    @PostMapping("/refreshToken")
    public LoginResponseDTO refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        System.out.println("before expiry check");
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())

                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsers)
                .map(Users -> {
                    String accessToken = jwtService.generateToken(Users.getEmail());
                    return LoginResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();

                }).orElseThrow(()-> new RuntimeException(
                        "Refresh token is not in database"
                ));

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseDTO> resetPassword(HttpServletRequest request, @RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException{
        return this.service.resetPassword(request,email);
    }

}

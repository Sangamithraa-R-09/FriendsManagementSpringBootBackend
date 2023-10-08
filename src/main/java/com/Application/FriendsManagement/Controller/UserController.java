package com.Application.FriendsManagement.Controller;

import com.Application.FriendsManagement.DTO.*;

import com.Application.FriendsManagement.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterDTO request, final HttpServletRequest httpServletRequest
            ) throws MessagingException, UnsupportedEncodingException {
        RegisterResponseDTO user=service.register(request);
        //publisher.publishEvent(new RegistrationVerificationEvent(user,applicationUrl(httpServletRequest)));
        return ResponseEntity.ok(new RegisterResponseDTO("Registered Successfully, kindly verify"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDTO> authenticate(
            @RequestBody LoginDTO request
    ){
        System.out.println("Controller method");
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<ResponseDTO> verifyEmail(@RequestParam("token")String verificationToken){
        service.verifyEmail(verificationToken);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"Successfully verified!!",null));
    }



}

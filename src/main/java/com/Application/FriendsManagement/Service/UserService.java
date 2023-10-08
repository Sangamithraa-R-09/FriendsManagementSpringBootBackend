package com.Application.FriendsManagement.Service;
import com.Application.FriendsManagement.DTO.*;
import com.Application.FriendsManagement.Exception.UserNotVerifiedException;
import com.Application.FriendsManagement.JwtUtil.JwtService;
import com.Application.FriendsManagement.Model.Role;

import com.Application.FriendsManagement.Repo.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Application.FriendsManagement.Model.Users;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class UserService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepo repo;

    public UserService(UserRepo userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private final AuthenticationManager authenticationManager;

    public RegisterResponseDTO register(RegisterDTO request) throws MessagingException, UnsupportedEncodingException {
        Optional<Users> userdetails=repo.findByEmail(request.getEmail());
        if(userdetails.isEmpty()){
            String verificationToken= UUID.randomUUID().toString();
            Instant now = Instant.now();
            Instant expiryInstant = now.plus(1, ChronoUnit.MINUTES);
            var user = Users.builder()
                    .firstname(request.getFirstname())
                    .email(request.getEmail())
                    .mobile(request.getMobile())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .status("Registered")
                    .verificationToken(verificationToken)
                    .expirydate(Date.from(expiryInstant))
                    .build();
            userRepository.save(user);
            sendVerificationEmail(user,verificationToken);
            return RegisterResponseDTO.builder()
                    .message("Successfully registered")
                    .build();
        }
        else{
            return RegisterResponseDTO.builder().message("User already exists").build();
        }
    }

    private void sendVerificationEmail(Users user, String verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url= "/verifyEmail?token="+verificationToken;
        String toAddress=user.getEmail();
        String fromAddress="mithraarathinasamy5@gmail.com";
        String senderName="User Verification Service";
        String subject="Please verify your registration";
        String content="<p> Hi, "+ user.getFirstname()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom(fromAddress,toAddress);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content,true);
        javaMailSender.send(message);
    }

    public LoginResponseDTO authenticate(LoginDTO request) {
        System.out.println("Before Authentication");
        Optional<Users> users=repo.findByEmail(request.getEmail());
        if(users.isPresent()) {
            if (users.get().getStatus().equals("Verified")) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
                System.out.println(request.getEmail() + " = > " + request.getPassword());
                if (authentication.isAuthenticated()) {
                    return LoginResponseDTO.builder()
                            .token(jwtService.generateToken(request.getEmail()))
                            .build();
                } else {
                    throw new UsernameNotFoundException("No user found");
                }
            } else {
                throw new UserNotVerifiedException("User is not verified. Kindly verify your email");
            }
        }

        else{
            throw new UsernameNotFoundException("User not found");
        }

    }

    public ResponseEntity<ResponseDTO>  verifyEmail(String verificationToken) {
        Optional<Users> users=repo.findByVerificationToken(verificationToken);
        if(users.isPresent()){
            Users users1=users.get();
            Instant now=Instant.now();
            Instant expiryDateInstant=users1.getExpirydate().toInstant();
            if(expiryDateInstant.isBefore(now)) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Verification token has expired", null));
            }
            else{
                users1.setStatus("Verified");
                repo.save(users1);
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"Successfully Verified!!",null));
            }
        }
        else{
            throw new NoSuchElementException("User is invalid");
        }
    }
}
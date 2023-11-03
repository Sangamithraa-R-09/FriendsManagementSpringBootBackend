package com.Application.FriendsManagement.Service;

import com.Application.FriendsManagement.Model.RefreshToken;
import com.Application.FriendsManagement.Repo.RefreshTokenRepo;
import com.Application.FriendsManagement.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserRepo userRepo;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder().users(userRepo.findByEmail(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired So signin again");
        }
        return token;
    }
}


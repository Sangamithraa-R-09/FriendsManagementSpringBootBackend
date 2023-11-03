package com.Application.FriendsManagement.Repo;

import com.Application.FriendsManagement.Model.PasswordResetToken;
import com.Application.FriendsManagement.Model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken,Integer> {

    Optional<PasswordResetToken> findByToken(String token);
}

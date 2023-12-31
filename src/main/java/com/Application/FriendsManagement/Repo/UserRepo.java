package com.Application.FriendsManagement.Repo;

import com.Application.FriendsManagement.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
   Optional<Users> findByEmail(String email);

    Optional<Users> findByVerificationToken(String verificationToken);

    Users findByResetPassword(String token);
}

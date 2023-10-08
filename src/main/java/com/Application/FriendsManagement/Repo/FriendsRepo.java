package com.Application.FriendsManagement.Repo;

import com.Application.FriendsManagement.Model.FriendsList;
import com.Application.FriendsManagement.Model.FriendsListPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Relation;
import java.util.Optional;

@Repository
public interface FriendsRepo extends JpaRepository<FriendsList, FriendsListPk> {

    Optional<FriendsList> findBySenderUserIdAndReceiverUserId(Long sender,Long receiver);
}

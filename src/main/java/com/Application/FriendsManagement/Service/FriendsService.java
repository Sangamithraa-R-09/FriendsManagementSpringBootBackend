package com.Application.FriendsManagement.Service;

import com.Application.FriendsManagement.DTO.ResponseDTO;
import com.Application.FriendsManagement.DTO.SenderReceiverDTO;
import com.Application.FriendsManagement.Model.FriendsList;
import com.Application.FriendsManagement.Model.Users;
import com.Application.FriendsManagement.Repo.FriendsRepo;
import com.Application.FriendsManagement.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendsService {
    @Autowired
    FriendsRepo repo;
    @Autowired
    UserRepo userRepo;

    public Boolean isRelationExist(SenderReceiverDTO senderReceiverDTO) {
        Optional<FriendsList> check = repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
        return check.isPresent();
    }

    public Boolean isReverseExist(SenderReceiverDTO senderReceiverDTO) {
        SenderReceiverDTO reverseDTO = new SenderReceiverDTO(senderReceiverDTO.getReceiver(), senderReceiverDTO.getSender());
        return isRelationExist(reverseDTO);
    }

    public String isUsersValid(Long userId1, Long userId2) {
        Boolean user1 = userRepo.findById(userId1).isPresent();

        Boolean user2 = userRepo.findById(userId2).isPresent();

        String temp = "";

        if (!user1 && !user2) {
            temp += userId1 + " and " + userId2 + "doesnot exist in the UserTable";
            return temp;
        } else if (!user1) {
            temp += userId1 + " doesnot exist in the Usertable";
            return temp;
        } else if (!user2) {
            temp += userId2 + " doesnot exist in the Usertable";
            return temp;
        } else {
            return temp;
        }
    }

    public ResponseEntity<ResponseDTO> friendRequest(SenderReceiverDTO senderReceiverDTO) {
        Users senderId = userRepo.findById(senderReceiverDTO.getSender()).orElse(null);
        Users receiverId = userRepo.findById(senderReceiverDTO.getReceiver()).orElse(null);

        if (isRelationExist(senderReceiverDTO)) {
            Optional<FriendsList> friendsList = repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
            if (friendsList.isPresent()) {
                FriendsList friendsRow = friendsList.get();
                String friendStatus = friendsRow.getFriendStatus();
                if (friendStatus.equals("requested")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Already requested", null));
                } else if (friendStatus.equals("accepted")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "You are already a friend", null));
                } else if (friendStatus.equals("unfollow")) {
                    friendsRow.setFriendStatus("requested");
                    repo.save(friendsRow);
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request has been sent", null));
                }
            }
        } else if (isReverseExist(senderReceiverDTO)) {
            Long sender = senderReceiverDTO.getReceiver();
            Long receiver = senderReceiverDTO.getSender();
            Optional<FriendsList> friendsList = repo.findBySenderUserIdAndReceiverUserId(sender, receiver);
            if (friendsList.isPresent()) {
                FriendsList friendsRow = friendsList.get();
                String friendStatus = friendsRow.getFriendStatus();
                if (friendStatus.equals("requested")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Already requested", null));
                } else if (friendStatus.equals("accepted")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "You are already a friend", null));
                } else if (friendStatus.equals("unfollow")) {
                    friendsRow.setFriendStatus("requested");
                    repo.save(friendsRow);
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request has been sent", null));
                }
            }
        } else {
            FriendsList newFriend = new FriendsList();
            newFriend.setSender(senderId);
            newFriend.setReceiver(receiverId);
            newFriend.setFriendStatus("requested");
            repo.save(newFriend);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request has been sent", null));
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST, "Bad Request", null));
    }


    public ResponseEntity<ResponseDTO> friendAccept(SenderReceiverDTO senderReceiverDTO) {
        Users senderId = userRepo.findById(senderReceiverDTO.getSender()).orElse(null);
        Users receiverId = userRepo.findById(senderReceiverDTO.getReceiver()).orElse(null);

        if (isRelationExist(senderReceiverDTO)) {
            Optional<FriendsList> friendsList = repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
            if (friendsList.isPresent()) {
                FriendsList friendsRow = friendsList.get();
                String friendStatus = friendsRow.getFriendStatus();
                if (friendStatus.equals("accepted")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Already you are a friend", null));
                } else if (friendStatus.equals("unfollow")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request is not made", null));
                } else if (friendStatus.equals("requested")) {
                    friendsRow.setFriendStatus("accepted");
                    repo.save(friendsRow);
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request has been accepted", null));
                }
            }
        } else if (isReverseExist(senderReceiverDTO)) {
            Long sender = senderReceiverDTO.getReceiver();
            Long receiver = senderReceiverDTO.getSender();
            Optional<FriendsList> friendsList = repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
            if (friendsList.isPresent()) {
                FriendsList friendsRow = friendsList.get();
                String friendStatus = friendsRow.getFriendStatus();
                if (friendStatus.equals("accepted")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Already you are a friend", null));
                } else if (friendStatus.equals("unfollow")) {
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request is not made", null));
                } else if (friendStatus.equals("requested")) {
                    friendsRow.setFriendStatus("accepted");
                    repo.save(friendsRow);
                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Request has been accepted", null));
                }
            }
        } else {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Such relation doesn't exists", null));
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST, "Bad Request", null));
    }


    public ResponseEntity<ResponseDTO> followingStatus(SenderReceiverDTO senderReceiverDTO) {
        if (isRelationExist(senderReceiverDTO)) {
            Optional<FriendsList> friendsList = repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
            FriendsList friendsList1 = friendsList.get();
            String followingStatus = friendsList1.getFollowingStatus();
            if (followingStatus==null) {
                friendsList1.setFollowingStatus("following");
                repo.save(friendsList1);
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Now"+senderReceiverDTO.getSender() +"is following "+senderReceiverDTO.getReceiver(), null));
            }
           else if (followingStatus.equals("following")) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "Already you are following "+senderReceiverDTO.getReceiver(), null));
           }
        }
        else {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST, "No such relation exists", null));
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST, "", null));
    }

    public ResponseEntity<ResponseDTO> followerStatus(SenderReceiverDTO senderReceiverDTO) {
        if(isRelationExist(senderReceiverDTO)){
            Optional<FriendsList> friendsList=repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
            FriendsList friendsList1= friendsList.get();
            String followerStatus=friendsList1.getFollowerStatus();
            if(followerStatus==null){
                friendsList1.setFollowerStatus("follower");
                repo.save(friendsList1);
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"Now"+friendsList1.getReceiver()+" is following "+friendsList1.getSender(),null));
            }
            else if(followerStatus.equals("follower")){
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"Already you are following"+friendsList1.getSender(),null));
            }
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"No such relation exists",null));
    }

    public ResponseEntity<ResponseDTO> unfriend(SenderReceiverDTO senderReceiverDTO) {
        if(isRelationExist(senderReceiverDTO)){
            Optional<FriendsList> friendsList=repo.findBySenderUserIdAndReceiverUserId(senderReceiverDTO.getSender(),senderReceiverDTO.getReceiver());
            FriendsList friendsList1=friendsList.get();
            String status=friendsList1.getFriendStatus();
            if(status.equals("accepted")){
                friendsList1.setFriendStatus("unfriend");
                repo.save(friendsList1);
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"You have unfriend "+senderReceiverDTO.getReceiver(),null));
            }
            else if(status.equals("requested") || status.equals("unfriend")){
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,"You are not even a friend.So, you cannot unfriend",null));
            }
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST,"No such relation exists",null));
    }
}


package com.Application.FriendsManagement.Model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@IdClass(FriendsListPk.class)

public class FriendsList {

   @Id
   @Column(name = "SENDER")
   private Users sender;
   @Id
   @Column(name ="RECEIVER")
   private Users receiver;

   @Column(name = "FRIEND_STATUS")
   private String friendStatus;

   @Column(name="FOLLOWING_STATUS")
   private String followingStatus;
   @Column(name="FOLLOWER_STATUS")
   private String followerStatus;

   public Users getSender() {
      return sender;
   }

   public void setSender(Users sender) {
      this.sender = sender;
   }

   public Users getReceiver() {
      return receiver;
   }

   public void setReceiver(Users receiver) {
      this.receiver = receiver;
   }

   public String getFriendStatus() {
      return friendStatus;
   }

   public void setFriendStatus(String friendStatus) {
      this.friendStatus = friendStatus;
   }

   public String getFollowingStatus() {
      return followingStatus;
   }

   public void setFollowingStatus(String followingStatus) {
      this.followingStatus = followingStatus;
   }

   public String getFollowerStatus() {
      return followerStatus;
   }

   public void setFollowerStatus(String followerStatus) {
      this.followerStatus = followerStatus;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      FriendsList that = (FriendsList) o;
      return Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sender, receiver);
   }
}

package com.Application.FriendsManagement.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class FriendsListPk implements Serializable {
    @ManyToOne(targetEntity = Users.class,cascade = CascadeType.ALL)
    @JoinColumn(name="SENDER",referencedColumnName = "USER_ID")
    private Users sender;

    @ManyToOne(targetEntity = Users.class,cascade = CascadeType.ALL)
    @JoinColumn(name="RECEIVER",referencedColumnName = "USER_ID")
    private Users receiver;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsListPk that = (FriendsListPk) o;
        return Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver);
    }
}

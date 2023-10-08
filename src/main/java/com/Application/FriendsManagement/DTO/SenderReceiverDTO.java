package com.Application.FriendsManagement.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenderReceiverDTO {

    private Long Sender;

    private Long Receiver;

    public SenderReceiverDTO(Long sender, Long receiver) {
        Sender = sender;
        Receiver = receiver;
    }
}

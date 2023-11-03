package com.Application.FriendsManagement.Controller;

import com.Application.FriendsManagement.DTO.SenderReceiverDTO;
import com.Application.FriendsManagement.DTO.ResponseDTO;
import com.Application.FriendsManagement.Service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendsController {
    @Autowired
    FriendsService service;

    @GetMapping("/testing")
    public ResponseEntity<String> display(){
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/requested")
    public ResponseEntity<ResponseDTO> friendRequest(@RequestBody SenderReceiverDTO senderReceiverDTO)
    {
        String response=service.isUsersValid(senderReceiverDTO.getSender(),senderReceiverDTO.getReceiver());
        if(response.equals("")){
            return service.friendRequest(senderReceiverDTO);
        }
        else{
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST,response,null));
        }
    }

    @PostMapping("/accepted")
    public ResponseEntity<ResponseDTO> friendAccept(@RequestBody SenderReceiverDTO senderReceiverDTO){
        String response=service.isUsersValid(senderReceiverDTO.getSender(),senderReceiverDTO.getReceiver());
        if(response.equals("")){
            return service.friendAccept(senderReceiverDTO);
        }
        else{
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST,response,null));
        }
    }

    @PostMapping("/following")
    public ResponseEntity<ResponseDTO> followingStatus(@RequestBody SenderReceiverDTO senderReceiverDTO){
        String response=service.isUsersValid(senderReceiverDTO.getSender(),senderReceiverDTO.getReceiver());
        if(response.equals("")){
            return service.followingStatus(senderReceiverDTO);
        }
        else{
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST,response,null));
        }
    }

    @PostMapping("/follower")
    public ResponseEntity<ResponseDTO> followerStatus(@RequestBody SenderReceiverDTO senderReceiverDTO){
        String response=service.isUsersValid(senderReceiverDTO.getSender(), senderReceiverDTO.getReceiver());
        if(response.equals("")){
            return service.followerStatus(senderReceiverDTO);
        }
        else{
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK,response,null));
        }
    }

    @PostMapping("/unfriend")
    public ResponseEntity<ResponseDTO> unfriend(@RequestBody SenderReceiverDTO senderReceiverDTO){
        String response= service.isUsersValid(senderReceiverDTO.getSender(),senderReceiverDTO.getReceiver());
        if(response.equals("")){
            return service.unfriend(senderReceiverDTO);
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST,"No such relation exists",null));
    }

}

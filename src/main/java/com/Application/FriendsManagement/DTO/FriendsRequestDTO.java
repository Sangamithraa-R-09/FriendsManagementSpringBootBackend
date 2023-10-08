package com.Application.FriendsManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRequestDTO {
    private String sender;
    private String receiver;
}

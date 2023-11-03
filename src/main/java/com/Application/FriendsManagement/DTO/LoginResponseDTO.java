package com.Application.FriendsManagement.DTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String accessToken;

    private String token;

}

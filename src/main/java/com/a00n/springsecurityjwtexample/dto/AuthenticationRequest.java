package com.a00n.springsecurityjwtexample.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;

}
package me.inhohwang.dto;


import lombok.Data;

@Data
public class CreateAccessTokenRequest {
    private String refreshToken;
}

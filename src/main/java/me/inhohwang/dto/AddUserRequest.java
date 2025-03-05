package me.inhohwang.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class AddUserRequest {
    private String email;
    private String password;
}

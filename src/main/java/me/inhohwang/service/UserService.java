package me.inhohwang.service;

import lombok.RequiredArgsConstructor;
import me.inhohwang.dto.AddUserRequest;
import me.inhohwang.repository.UserRepository;
import me.inhohwang.springbootdeveloper.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }
}

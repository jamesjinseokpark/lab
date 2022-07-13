package com.doiet.lab.service;

import com.doiet.lab.model.UserEntity;
import com.doiet.lab.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null || userEntity.getEmail() ==null){
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)){
            log.warn("Email already exists{}" , email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password , final PasswordEncoder encoder){

        final UserEntity originalUer = userRepository.findByEmail(email);

        // matches 메소드를 이용해 패스워드 일치여부 확인
        if(originalUer != null && encoder.matches(password, originalUer.getPassword())){
            return originalUer;
        }

        return null;
    }
}

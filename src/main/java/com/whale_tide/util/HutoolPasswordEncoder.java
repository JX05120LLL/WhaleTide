package com.whale_tide.util;


import cn.hutool.crypto.digest.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HutoolPasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }



}

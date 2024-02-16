package com.loniquiz.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {

    @Test
    @DisplayName("비밀키 해시값 생성")
    void makeSecretKey() {

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        String s = Base64.getEncoder().encodeToString(bytes);
        System.out.println("s = " + s);
    }

}
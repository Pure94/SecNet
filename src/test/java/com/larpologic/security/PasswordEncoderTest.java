package com.larpologic.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTest {

    @Test
    @Disabled
    public void testPasswordHashing() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String plaintextPassword = "adminpass";

        String hashedPassword = passwordEncoder.encode(plaintextPassword);
        System.out.println("Zahashowane hasło dla 'adminpass': " + hashedPassword);

        boolean passwordMatches = passwordEncoder.matches(plaintextPassword, hashedPassword);
        assertTrue(passwordMatches, "Hasło powinno pasować do hasha");
    }
}
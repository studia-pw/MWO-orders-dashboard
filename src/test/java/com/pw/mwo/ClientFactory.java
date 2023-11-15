package com.pw.mwo;

import com.pw.mwo.domain.Client;

public class ClientFactory {
    static Client makeClientCase1() {
        return Client.builder()
                .name("John")
                .surname("Doe")
                .email("john.doe123@example.com")
                .build();
    }

    static Client makeClientCase2() {
        return Client.builder()
                .name("Jane")
                .surname("Smith")
                .email("jane.smith@example.com")
                .build();
    }

    static Client makeClientCase3() {
        return Client.builder()
                .name("Alice")
                .surname("Johnson")
                .email("alice.johnson@example.com")
                .build();
    }
}

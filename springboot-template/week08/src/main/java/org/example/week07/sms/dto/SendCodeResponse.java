package org.example.week07.sms.dto;

public record SendCodeResponse(
        String phone,
        int ttlSeconds,
        String codePlain
) {
}
package com.nivara.nivarabackend.service.impl;

import java.net.URI;

import org.springframework.stereotype.Service;
import com.nivara.nivarabackend.service.SmsService;

import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class Fast2SmsServiceImpl implements SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    @Override
    public void sendOtp(String mobile, String otp) {

        try {
            System.out.println("fastsms");
            String message = "Your OTP is " + otp + ". Do not share it.";

            String jsonBody = """
                {
                  "route": "otp",
                  "sender_id": "FSTSMS",
                  "message": "%s",
                  "language": "english",
                  "flash": 0,
                  "numbers": "%s"
                }
                """.formatted(message, mobile);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.fast2sms.com/dev/bulkV2"))
                    .header("authorization", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Fast2SMS Response: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Failed to send SMS: " + e.getMessage(), e);
        }
    }
}

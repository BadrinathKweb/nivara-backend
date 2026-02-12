package com.nivara.nivarabackend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.nivara.nivarabackend.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@Primary
public class TwilioWhatsappServiceImpl implements SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Override
    public void sendOtp(String mobile, String otp) {

        try {
            System.out.println(" Sending WhatsApp OTP...");

            Twilio.init(accountSid, authToken);

            String message = "Your OTP is " + otp + ". Do not share it.";

            Message.creator(
                    new PhoneNumber("whatsapp:+91" + mobile),
                    new PhoneNumber(fromNumber),
                    message
            ).create();

            System.out.println("WhatsApp OTP sent to " + mobile);

        } catch (Exception e) {

            System.out.println(" Twilio send failed");
            e.printStackTrace();
            System.out.println("Fallback OTP (dev only): " + otp);
        }
    }
}


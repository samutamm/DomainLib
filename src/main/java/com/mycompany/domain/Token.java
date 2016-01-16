package com.mycompany.domain;

import java.util.Calendar;
import java.util.UUID;

public class Token {
    String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }
    
    public static Token generate(String salt, int minutes){
        String randompart = UUID.randomUUID().toString();
        String encryptedTime = generateTimePart(minutes, salt);
        Token t = new Token(randompart + encryptedTime);
        return t;
    }

    private static String generateTimePart(int seconds, String encryptKey) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        Encrypter encrypter = new Encrypter(encryptKey);
        long timeInMillis = cal.getTimeInMillis();
        String encryptedTime = encrypter.encrypt(String.valueOf(timeInMillis));
        return encryptedTime;
    }
    
    public static boolean isValid(String encryptKey, String tokenValue) {
        String timePart = tokenValue.substring(36);
        Encrypter encrypter = new Encrypter(encryptKey);
        String decrypted = encrypter.decrypt(timePart);
        return Long.parseLong(decrypted) >= System.currentTimeMillis();
    }
}

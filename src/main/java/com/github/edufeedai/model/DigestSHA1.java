package com.github.edufeedai.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestSHA1 implements Digest{


    @Override
    public String digest(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = messageDigest.digest(message.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

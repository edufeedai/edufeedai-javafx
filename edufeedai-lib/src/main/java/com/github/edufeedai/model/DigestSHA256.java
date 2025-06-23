package com.github.edufeedai.model;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestSHA256 implements Digest {
    MessageDigest messageDigest = null;
    @Override
    public String digest(String message) throws DigestException {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new DigestException(e);
        }
        byte[] hashBytes = messageDigest.digest(message.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

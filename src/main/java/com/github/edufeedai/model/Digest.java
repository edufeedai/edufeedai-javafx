package com.github.edufeedai.model;

import java.security.NoSuchAlgorithmException;

public interface Digest {

    String digest( String message) throws NoSuchAlgorithmException;

}

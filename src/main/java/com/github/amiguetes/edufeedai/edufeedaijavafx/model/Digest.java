package com.github.amiguetes.edufeedai.edufeedaijavafx.model;

import java.security.NoSuchAlgorithmException;

public interface Digest {

    String digest( String message) throws NoSuchAlgorithmException;

}

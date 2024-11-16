package com.github.edufeedai.model;

import java.security.DigestException;

public interface Digest {

    String digest( String message) throws DigestException;

}

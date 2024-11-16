package com.github.edufeedai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class DigestSHA1Test {
    @Test
    void testDigest() {

        Digest digest = new DigestSHA1();

        String expected = "0a4d55a8d778e5022fab701977c5d840bbc486d0";

        String target = "Hello World";
        String actual = null;

        try {
            actual = digest.digest(target);
            assertEquals(expected, actual);;
        } catch (Exception e) {
            fail(e);
        }

    }
}

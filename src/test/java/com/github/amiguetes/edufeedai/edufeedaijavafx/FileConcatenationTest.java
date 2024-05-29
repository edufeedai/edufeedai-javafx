package com.github.amiguetes.edufeedai.edufeedaijavafx;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileConcatenationTest {

    String inputDirectory = "";

    @Test
    void serialize() {

        FileConcatenation concatenation = new FileConcatenation(inputDirectory);
        try {
            concatenation.serialize();
        } catch (IOException e) {
            fail(e);
        }

    }
}
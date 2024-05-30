package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.DigestImplementation;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileConcatenationTest {

    String inputDirectory = Dotenv.load().get("TEST_DIR");

    @Test
    void serialize() {

        FileConcatenation concatenation = new FileConcatenation(inputDirectory, new DigestImplementation());
        try {
            concatenation.serialize("Ahora eres un evaluador");
        } catch (IOException e) {
            fail(e);
        }

    }
}
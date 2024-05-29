package com.github.amiguetes.edufeedai.edufeedaijavafx;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextSerializerTest {

    String assessmentDirectory = Dotenv.load().get("ASSESSMENT_TEST_DIR");

    @Test
    void packageFiles() {

        TextSerializer serializer = new TextSerializer(assessmentDirectory);
        try {
            serializer.packageFiles();
        } catch (Exception e) {
            fail(e);
        }

    }
}
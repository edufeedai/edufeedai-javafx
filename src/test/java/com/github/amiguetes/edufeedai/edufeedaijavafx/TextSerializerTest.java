package com.github.amiguetes.edufeedai.edufeedaijavafx;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextSerializerTest {

    String inputDirectory = "";
    String outputFilePath = "";

    @Test
    void searchAndSerializeRecursive() throws IOException {

        TextSerializer textSerializer = new TextSerializer(inputDirectory, outputFilePath);
        textSerializer.serialize();

    }
}
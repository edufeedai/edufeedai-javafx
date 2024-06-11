package com.github.amiguetes.edufeedai.edufeedaijavafx;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.AssessmentGradingArgumentsProvider;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.AssessmentGradingConfig;

import io.github.cdimascio.dotenv.Dotenv;

class TextSerializerTest {

    String assessmentDirectory = Dotenv.load().get("ASSESSMENT_TEST_DIR");

    @ParameterizedTest
    @ArgumentsSource(AssessmentGradingArgumentsProvider.class)
    void packageFiles(AssessmentGradingConfig config) {

        TextSerializer serializer = new TextSerializer(assessmentDirectory);

        serializer.deleteAllStudentsJSONOpenAIJob();

        try {
            serializer.packageFiles(config.getInstruction());
        } catch (Exception e) {
            fail(e);
        }

        try {
            serializer.generateJSONL(5);
        } catch (Exception e) {
            fail(e);
        }

    }
}
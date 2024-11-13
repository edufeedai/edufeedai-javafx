package com.github.edufeedai.javafx;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.github.edufeedai.javafx.model.AssessmentGradingArgumentsProvider;
import com.github.edufeedai.javafx.model.AssessmentGradingConfig;

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
            serializer.generateJSONL(6);
        } catch (Exception e) {
            fail(e);
        }

    }
}
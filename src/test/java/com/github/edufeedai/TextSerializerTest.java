package com.github.edufeedai;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.github.edufeedai.model.AssessmentGradingArgumentsProvider;
import com.github.edufeedai.model.AssessmentGradingConfig;

import io.github.cdimascio.dotenv.Dotenv;

class TextSerializerTest {

    String assessmentDirectory = Dotenv.load().get("ASSESSMENT_TEST_DIR");

    @ParameterizedTest
    @ArgumentsSource(AssessmentGradingArgumentsProvider.class)
    void packageFiles(AssessmentGradingConfig config) {

        TextSerializer serializer = new TextSerializer(assessmentDirectory);

        serializer.deleteAllStudentJsonFiles();

        try {
            serializer.packageFiles(config.getInstruction());
        } catch (Exception e) {
            fail(e);
        }

        try {
            serializer.generateJsonl(6);
        } catch (Exception e) {
            fail(e);
        }

    }
}